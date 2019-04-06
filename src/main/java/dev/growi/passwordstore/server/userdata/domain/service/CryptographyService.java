package dev.growi.passwordstore.server.userdata.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.security.*;
import java.security.spec.*;

@Service
public class CryptographyService {

    @Value("${passwordstore.security.rsa.key.size:2048}")
    private int keySize;

    @Value("${passwordstore.security.pbe.schema:PBEWITHSHA256AND256BITAES-CBC-BC}")
    private String pbeSchema;

    public KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");

        generator.initialize(keySize, SecureRandom.getInstanceStrong());
        KeyPair keyPair = generator.generateKeyPair();

        return keyPair;
    }

    public PublicKey createRSAPublicKey(byte[] bytes) throws InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException {
        KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
        PublicKey publicKey = fact.generatePublic(new X509EncodedKeySpec(bytes));

        return publicKey;
    }

    public PrivateKey createRSAPrivateKey(byte[] bytes) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
        PrivateKey privateKey = fact.generatePrivate(new PKCS8EncodedKeySpec(bytes));

        return privateKey;
    }

    public byte[] rsaEncrypt(PublicKey publicKey, byte[] message) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(message);
    }

    public byte[] rsaDecrypt(PrivateKey privateKey, byte [] encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encrypted);
    }

    public EncryptedPrivateKeyInfo pbeEncrypt(String password, PrivateKey privateKey) throws NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, IOException, InvalidParameterSpecException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException {

        int count = 20;// hash iteration count
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[8];
        random.nextBytes(salt);

        // Create PBE parameter set
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(pbeSchema);
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        Cipher pbeCipher = Cipher.getInstance(pbeSchema);

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

        // Encrypt the encoded Private Key with the PBE key
        byte[] ciphertext = pbeCipher.doFinal(privateKey.getEncoded());

        // Now construct  PKCS #8 EncryptedPrivateKeyInfo object
        AlgorithmParameters algparms = AlgorithmParameters.getInstance(pbeSchema);
        algparms.init(pbeParamSpec);
        EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, ciphertext);

        return  encinfo;
    }

    public PrivateKey pbeDecrypt(String password, byte[] encrypted) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException {

        EncryptedPrivateKeyInfo encryptPKInfo = new EncryptedPrivateKeyInfo(encrypted);
        Cipher cipher = Cipher.getInstance(encryptPKInfo.getAlgName());
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory secFac = SecretKeyFactory.getInstance(encryptPKInfo.getAlgName());
        Key pbeKey = secFac.generateSecret(pbeKeySpec);
        AlgorithmParameters algParams = encryptPKInfo.getAlgParameters();
        cipher.init(Cipher.DECRYPT_MODE, pbeKey, algParams);
        KeySpec pkcs8KeySpec = encryptPKInfo.getKeySpec(cipher);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(pkcs8KeySpec);
    }
}
