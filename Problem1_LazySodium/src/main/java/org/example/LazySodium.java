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
 * -> Cryptobox key pair generates a byte array of size 32
 * by default
 * -> If I convert public key to String to write it in a file,
 * while during decryption it gives
 * SodiumException: Could not decrypt your message.
 * -> It because difference in the key
 * during conversion from string to Key to byte[]
 * -> Used byte array format to input and output the
 * keys from the file
 */
public class LazySodium {
    private static SodiumJava sodiumJava;
    private static LazySodiumJava lazySodiumJava;
    private static Box.Lazy box;
    private static String message;
    private static FileOutputStream fileOutputStream;
    private static FileInputStream fileInputStream;
    private static File file;
    private static String publicKeyFile;
    private static String privateKeyFile;

    public void init() {
        //  auto-loading of the prepackaged native C Libsodium library
//   from Lazysodium's resources folder
//   Initialize sodium
        sodiumJava = new SodiumJava();
//    Initialize lazysodium
        lazySodiumJava = new LazySodiumJava(sodiumJava, StandardCharsets.UTF_8);
//    Use Box.Lazy interface
        box = (Box.Lazy) lazySodiumJava;
        message = "This is a secret message";
        publicKeyFile = "publickey.txt";
        privateKeyFile = "privatekey.txt";

    }

    public void sealedBox() throws SodiumException {
        this.init();
//        Generate Keypair
        KeyPair keyPair = box.cryptoBoxKeypair();

//      Store the keys in respective files
        try {
            this.writeInFile(publicKeyFile, keyPair.getPublicKey());
            this.writeInFile(privateKeyFile, keyPair.getSecretKey());
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }

//      Server encrypts resource with client's public key
//        using sealed box
        String cipherText = this.encrypt(message);
        System.out.println("Encrypted Message: " + cipherText);
        System.out.println("Encrypted Message length: " + cipherText.length());

//      Client decrypts using the keypair
        String decryptedMessage = this.decrypt(cipherText);
        System.out.println("Decrypted Message: " + decryptedMessage);
        System.out.println("Decrypted Message length: "+ decryptedMessage.length());


    }


    private void writeInFile(String fileName, Key secretKey) throws IOException {
//      Convert Key to byte array to write in a file
        fileOutputStream = new FileOutputStream(fileName);
        fileOutputStream.write(secretKey.getAsBytes());

    }

    private Key getKeyFromFile(String fileName) throws FileNotFoundException {
        file = new File(fileName);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream = new FileInputStream(file);
        try {
            fileInputStream.read(bytes);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        Key keyFromBytes = Key.fromBytes(bytes);
        return keyFromBytes;
    }

    /**
     * encrypts using public key
     *
     * @param message
     * @returns encrypted message -> cipherText
     */
    private String encrypt(String message) {
        try {
            Key publicKey = this.getKeyFromFile(publicKeyFile);
            return box.cryptoBoxSealEasy(message, publicKey);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e);
        } catch (SodiumException e) {
            System.out.println("SodiumException: " + e);
        }
        return null;
    }

    /**
     * decrypts using private and public key pair
     *
     * @param cipherText
     * @returns decrypted cipherText -> message
     */
    private String decrypt(String cipherText) {
        try {
            Key publicKey = this.getKeyFromFile(publicKeyFile);
            Key privateKey = this.getKeyFromFile(privateKeyFile);
            KeyPair keyPair = new KeyPair(publicKey, privateKey);
            return box.cryptoBoxSealOpenEasy(
                    cipherText,
                    keyPair
            );
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException " + e);
        } catch (SodiumException e) {
            System.out.println("SodiumException: " + e);
        }
        return null;
    }

}
