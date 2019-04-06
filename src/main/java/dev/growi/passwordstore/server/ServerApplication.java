package dev.growi.passwordstore.server;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchAlgorithmException;
import java.security.Security;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {

		Security.addProvider(new BouncyCastleProvider());
		Security.setProperty("crypto.policy", "unlimited");
		int maxKeySize = 0;

		try {
			maxKeySize = javax.crypto.Cipher.getMaxAllowedKeyLength("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		System.out.println("Max Key Size for AES : " + maxKeySize);

		SpringApplication.run(ServerApplication.class, args);
	}

}
