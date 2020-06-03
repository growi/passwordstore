package dev.growi.passwordstore.server.shared.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.*;

@Service
public class CryptographyService {

    @Value("${passwordstore.security.rsa.key.size:2048}")
    private int rsaKeySize;

    @Value("${passwordstore.security.pbe.schema:PBEWITHSHA256AND256BITAES-CBC-BC}")
    private String pbeSchema;

    @Value("${passwordstore.security.aes.cypher:AES/GCM/NoPadding}")
    private String aesCypher;

    @Value("${passwordstore.security.aes.key.size:128}")
    private int aesKeySize;

    public KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");

        generator.initialize(rsaKeySize, SecureRandom.getInstanceStrong());
        return generator.generateKeyPair();
    }

    public PublicKey createRSAPublicKey(byte[] bytes) throws InvalidKeySpecException, NoSuchProviderException,
            NoSuchAlgorithmException {

        KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
        return fact.generatePublic(new X509EncodedKeySpec(bytes));
    }

    public PrivateKey createRSAPrivateKey(byte[] bytes) throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidKeySpecException {

        KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
        return fact.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    public byte[] encryptRSA(PublicKey publicKey, byte[] message) throws NoSuchPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(message);
    }

    public byte[] decryptRSA(PrivateKey privateKey, byte[] encrypted) throws NoSuchPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encrypted);
    }

    public EncryptedPrivateKeyInfo pbeEncrypt(String password, PrivateKey privateKey) throws NoSuchAlgorithmException,
            BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException,
            InvalidParameterSpecException, InvalidKeySpecException, InvalidAlgorithmParameterException,
            InvalidKeyException {

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

        return encinfo;
    }

    public EncryptedPrivateKeyInfo createPBEKeyInfo(byte[] ciphertext) throws IOException {

        return new EncryptedPrivateKeyInfo(ciphertext);

    }

    public PrivateKey pbeDecrypt(String password, byte[] encrypted) throws IOException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
            InvalidKeyException {

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

    public SecretKey generateAESKey() throws NoSuchAlgorithmException {

        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        int byteSize = aesKeySize / 8;
        byte[] key = new byte[byteSize];
        secureRandom.nextBytes(key);

        return createAESKey(key);
    }

    public SecretKey createAESKey(byte[] key){
        return new SecretKeySpec(key, "AES");
    }

    public byte[] encryptAES(SecretKey secretKey, byte[] plainText)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {

        SecureRandom secureRandom = SecureRandom.getInstanceStrong();

        byte[] iv = new byte[12]; //NEVER REUSE THIS IV WITH SAME KEY
        secureRandom.nextBytes(iv);

        final Cipher cipher = Cipher.getInstance(aesCypher, "BC");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv); //128 bit auth tag length
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        byte[] cipherText = cipher.doFinal(plainText);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        byte[] cipherMessage = byteBuffer.array();

        return cipherMessage;
    }

    public byte[] decryptAES(SecretKey key, byte[] cipherMessage) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
        int ivLength = byteBuffer.getInt();
        if(ivLength < 12 || ivLength >= 16) { // check input parameter
            throw new IllegalArgumentException("invalid iv length");
        }
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        final Cipher cipher = Cipher.getInstance(aesCypher);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getEncoded(), "AES"),
                new GCMParameterSpec(128, iv));

        byte[] plainText= cipher.doFinal(cipherText);

        return plainText;
    }

}
