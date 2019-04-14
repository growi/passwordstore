package dev.growi.passwordstore.server.api.service;

import org.springframework.stereotype.Service;

import javax.crypto.EncryptedPrivateKeyInfo;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class EncoderService {

    public String armorKey(EncryptedPrivateKeyInfo key) throws IOException {

        String padding = "-----";
        String begin = "BEGIN ENCRYPTED PRIVATE KEY";
        String end = "END ENCRYPTED PRIVATE KEY";

        StringBuilder sb = new StringBuilder();
        sb.append(padding);
        sb.append(begin);
        sb.append(padding);
        sb.append("\n");
        sb.append(toBase64Block(key.getEncoded(), 64));
        sb.append("\n");
        sb.append(padding);
        sb.append(end);
        sb.append(padding);

        return sb.toString();

    }

    public String armorKey(PublicKey key) {

        String padding = "-----";
        String begin = "BEGIN PUBLIC KEY";
        String end = "END PUBLIC KEY";

        StringBuilder sb = new StringBuilder();
        sb.append(padding);
        sb.append(begin);
        sb.append(padding);
        sb.append("\n");
        sb.append(toBase64Block(key.getEncoded(), 64));
        sb.append("\n");
        sb.append(padding);
        sb.append(end);
        sb.append(padding);

        return sb.toString();

    }

    public String armorKey(PrivateKey key)  {

        String padding = "-----";
        String begin = "BEGIN PRIVATE KEY";
        String end = "END PRIVATE KEY";

        StringBuilder sb = new StringBuilder();
        sb.append(padding);
        sb.append(begin);
        sb.append(padding);
        sb.append("\n");
        sb.append(toBase64Block(key.getEncoded(), 64));
        sb.append("\n");
        sb.append(padding);
        sb.append(end);
        sb.append(padding);

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
