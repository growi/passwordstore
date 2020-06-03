package dev.growi.passwordstore.server.api.service;

import org.springframework.stereotype.Service;

import javax.crypto.EncryptedPrivateKeyInfo;
import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class EncoderService {

    private static final String PADDING = "-----";
    private static final String ENCRYPTED_PRIVATE_KEY = "ENCRYPTED PRIVATE KEY";
    private static final String PUBLIC_KEY = "PUBLIC KEY";
    private static final String PRIVATE_KEY = "PRIVATE KEY";
    private static final String BEGIN = "BEGIN";
    private static final String END = "END";

    public String armorKey(EncryptedPrivateKeyInfo key) throws IOException {
        return armor(ENCRYPTED_PRIVATE_KEY, key.getEncoded());
    }

    public String armorKey(PublicKey key) {
        return armor(PUBLIC_KEY, key.getEncoded());
    }

    public String armorKey(PrivateKey key)  {
        return armor(PRIVATE_KEY, key.getEncoded());
    }

    private String armor(String spec, byte[] key){

        StringBuilder sb = new StringBuilder();
        sb.append(PADDING);
        sb.append(BEGIN);
        sb.append(' ');
        sb.append(spec);
        sb.append(PADDING);
        sb.append("\n");
        sb.append(toBase64Block(key, 64));
        sb.append("\n");
        sb.append(PADDING);
        sb.append(END);
        sb.append(' ');
        sb.append(PADDING);

        return sb.toString();

    }

    public String toBase64Block(byte[] bytes, int lineLength){

        String armoredBytes = Base64.getEncoder().encodeToString(bytes);
        StringBuilder sb = new StringBuilder();

        int i = 0;
        while (i + lineLength < armoredBytes.length()) {
            sb.append(armoredBytes.subSequence(i, i + lineLength));
            sb.append("\n");
            i += lineLength;
        }
        if (i < armoredBytes.length()) {
            sb.append(armoredBytes.subSequence(i, armoredBytes.length() - 1));
        }
        return sb.toString();
    }
}
