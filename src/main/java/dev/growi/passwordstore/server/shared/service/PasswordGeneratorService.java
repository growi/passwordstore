package dev.growi.passwordstore.server.shared.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class PasswordGeneratorService {

    public String generate(){
        return RandomStringUtils.randomPrint(25);
    }
}
