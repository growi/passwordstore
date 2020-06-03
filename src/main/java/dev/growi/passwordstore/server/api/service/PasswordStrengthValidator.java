package dev.growi.passwordstore.server.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PasswordStrengthValidator {

    @Value("${passwordstore.security.password.strength.enforce.minlength:8}")
    private int minLength;

    @Value("${passwordstore.security.password.strength.enforce.numbers:true}")
    private boolean enforceNumbers;

    @Value("${passwordstore.security.password.strength.enforce.uppercase:true}")
    private boolean enforceUpperCase;

    @Value("${passwordstore.security.password.strength.enforce.lowercase:true}")
    private boolean enforceLowerCase;

    @Value("${passwordstore.security.password.strength.enforce.specialCharacters:true}")
    private boolean enforceSpecialCharacters;

    public boolean isValidPassword(String candidate){

        int lowerCaseCount = 0;
        int upperCaseCount = 0;
        int numberCount = 0;
        int specialCount = 0;

        if (candidate.length() < minLength) return false;

        for(int i = 0; i < candidate.length(); i++){
            Character c = candidate.charAt(i);

            if(Character.isLetter(c)){
                if(Character.isUpperCase(c)){
                    upperCaseCount++;
                }else if(Character.isLowerCase(c)){
                    lowerCaseCount++;
                }
            }else if(Character.isDigit(c)){
                numberCount++;
            }else{
                specialCount++;
            };
        }

        return (!enforceLowerCase || (enforceLowerCase && lowerCaseCount >0 ))
                && (!enforceUpperCase || (enforceUpperCase && upperCaseCount >0 ))
                && (!enforceNumbers || (enforceNumbers && numberCount >0 ))
                && (!enforceSpecialCharacters || (enforceSpecialCharacters && specialCount >0 ));

    }

}
