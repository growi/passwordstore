package dev.growi.passwordstore.server.core.base.mapping;

import dev.growi.passwordstore.server.core.base.mapping.annotation.MappedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValueMapper<T> {

    @Autowired
    private ApplicationContext appContext;

    private Map<Class<?>, ArrayList<Mapping>> classMapping = new HashMap<>();


    public ValueMapper(){
    }

    private void init(Class<?> fromClass, Class<?> toClass) throws NoSuchFieldException, NoSuchMethodException, ClassNotFoundException {

        Class<?> clazz = toClass.getSuperclass();

        if(clazz != null){
            init(fromClass , clazz);
        }
        Field[] fields = toClass.getDeclaredFields();

        for(int i = 0; i < fields.length; i++){
            Field toField = fields[i];

            MappedValue annotation = toField.getDeclaredAnnotation(MappedValue.class);

            if(annotation != null){
                Field fromField ;
                Class<?> mappedClass = Class.forName(annotation.mappedClass());
                String mappedValue = annotation.mappedValue();

                Class<?> transformer = null;
                String transformMethhod = null;
                if(!annotation.transformer().isEmpty()) {
                    transformer = Class.forName(annotation.transformer());
                    transformMethhod = annotation.method();
                }

                if(mappedClass.isAssignableFrom(fromClass)) {
                    fromField = fromClass.getDeclaredField(mappedValue);

                    Mapping mapping = new Mapping();
                    mapping.toField = toField;
                    mapping.fromField = fromField;
                    if(transformer != null) {
                        mapping.transformer = appContext.getBean(transformer);
                        mapping.transformMethod = transformer.getMethod(transformMethhod, fromField.getType());
                    }

                    if (!classMapping.containsKey(fromClass)) {
                        classMapping.put(fromClass, new ArrayList());
                    }
                    List<Mapping> mappings = classMapping.get(fromClass);
                    mappings.add(mapping);
                }
            }
        }
    }

    public <F extends Object> T  map(F from , T to) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        if(!classMapping.containsKey(from.getClass())) {
            init(from.getClass(), to.getClass());
        }
        List<Mapping> mappedFields = classMapping.get(from.getClass());
        map(from, to, mappedFields);



        return to;
    }

    private void map(Object from, Object to, List<Mapping> mappedFields) throws IllegalAccessException, InvocationTargetException {

        for(Mapping mappedField : mappedFields){
            Field fromField = mappedField.fromField;
            Field toField = mappedField.toField;

            if(!fromField.isAccessible()){
                fromField.setAccessible(true);
            }

            if(!toField.isAccessible()){
                toField.setAccessible(true);
            }

            Object value = fromField.get(from);

            if(mappedField.transformer != null){
                value = mappedField.transformMethod.invoke(mappedField.transformer, value);
            }

            toField.set(to, value);
        }

    }

    private class Mapping{

        public Field fromField;
        public Field toField;
        public Object transformer;
        public Method transformMethod;
    }
}
