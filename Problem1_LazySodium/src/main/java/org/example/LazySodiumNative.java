package org.example;

import com.goterl.lazysodium.LazySodiumJava;
import com.goterl.lazysodium.SodiumJava;
import com.goterl.lazysodium.exceptions.SodiumException;
import com.goterl.lazysodium.interfaces.Box;
import com.goterl.lazysodium.utils.Key;
import com.goterl.lazysodium.utils.KeyPair;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * To know :
 * Difference between Box.java and SecretBox.java
 *
 * Key points:
 * Cryptobox key pair generates a byte array of size 32
 * by default
 */
public class LazySodiumNative {
    private static SodiumJava sodiumJava;
    private static LazySodiumJava lazySodiumJava;
    private static Box.Native box;
    private static String message;
    private static FileOutputStream fileOutputStream;
    private static BufferedReader bufferedReader;
    private static String publicKeyFile;
    private static String privateKeyFile;
    private static String keyPairFile;
    private static byte[] publicKey;
    private static byte[] privateKey;

    public void init() {
 //  auto-loading of the prepackaged native C Libsodium library
//   from Lazysodium's resources folder
//   Initialize sodium
        sodiumJava = new SodiumJava();
//    Initialize lazysodium
        lazySodiumJava = new LazySodiumJava(sodiumJava, StandardCharsets.UTF_8);
//    Use Box.Lazy interface
        box = (Box.Native) lazySodiumJava;
        message = "This is a secret message";
        publicKeyFile = "publickey.txt";
        privateKeyFile = "privatekey.txt";
        keyPairFile = "keyPairFile.txt";
        privateKey = new byte[32];
        publicKey = new byte[32];
    }

    public void sealedBox() throws SodiumException {
        this.init();
//        Generate Keypair
        box.cryptoBoxKeypair(publicKey,privateKey);

//      Store the keys in respective files
        try {
            this.writeInFile(publicKeyFile,publicKey);
//            this.writeInFile(privateKeyFile,keyPair.getSecretKey());
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }

//      Server encrypts resource with client's public key
//        using sealed box
//      String cipherText = this.encrypt(message);
//        System.out.println("Encrypted Message: "+ cipherText);
//        System.out.println("Encrypted Message length: "+ cipherText.length());






    }


    private void writeInFile(String fileName, byte[] secretKey) throws IOException {
//        Write byte array in file
        fileOutputStream = new FileOutputStream(fileName);
        fileOutputStream.write(secretKey);
    }

    private Key getKeyFromFile(String fileName) throws FileNotFoundException {
        bufferedReader = new BufferedReader(new FileReader(fileName));
        StringBuilder stringBuilder = new StringBuilder();
        String str;
        try {
            while((str = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(str);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        String key = stringBuilder.toString();
        return Key.fromPlainString(key);
    }

    /**
     * encrypts using public key
     * @param message
     * @returns encrypted message -> cipherText
     */
//    private String encrypt(String message)
//    {
//        try {
////            byte[]  publicKey = this.getKeyFromFile(publicKeyFile);
//            return box.cryptoBoxEasy();
//
//        } catch (FileNotFoundException e) {
//            System.out.println("FileNotFoundException: " + e);
//        } catch (SodiumException e) {
//            System.out.println("SodiumException: " + e);
//        }
//        return null;
//    }

    /**
     * decrypts using private and public key pair
     * @param cipherText
     * @returns decrypted cipherText -> message
     */
    private String decrypt(String cipherText)
    {
        return null;
    }

}
