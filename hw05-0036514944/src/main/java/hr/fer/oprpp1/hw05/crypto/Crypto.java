package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Class which has 3 functions: checksha (for checking hash code of a file), encrypt (encrypting a file), decrypt
 * (decrypting a file).
 */
public class Crypto {

    private static final int BLOCK_SIZE = 4096;

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        if (args.length < 2 || args.length > 3)
            throw new IllegalArgumentException("Crypto accepts at least 2 and max 3 arguments, first of which is: checksha, encrypt or decrypt, you gave " + args.length + " arguments");

        // TODO Maybe change this so it uses Template Method Pattern:
        /*
        UserInput();
        ProcessInput();
        Message();
         */
        switch (args[0]) {
            case "checksha" -> {
                if (args.length != 2)
                    throw new IllegalArgumentException("checksha function accepts 2 arguments, you gave " + args.length + " arguments");
                Path fileToCheckSha = Path.of(args[1]);
                checksha(fileToCheckSha);
            }
            case "encrypt", "decrypt" -> {
                if (args.length != 3)
                    throw new IllegalArgumentException("encrypt and decrypt accepts 2 arguments, you gave " + args.length + " arguments");
                Path fileToProcess = Path.of(args[1]);
                String nameOfProcessedFile = args[2];
                if (args[0].equals("encrypt"))
                    encryptOrDecrypt(fileToProcess, nameOfProcessedFile, true);
                else if (args[0].equals("decrypt"))
                    encryptOrDecrypt(fileToProcess, nameOfProcessedFile, false);
            }
            default -> throw new IllegalArgumentException("Invalid arguments given for Crypto. It has functions: checksha, encrypt and decrypt. Try again");
        }

    }

    /**
     * Method which calculates and checks the SHA-256 file digest.
     *
     * @param fileToCheckSha check its SHA-256
     * @throws NoSuchAlgorithmException if given invalid Digest algorithm
     */
    private static void checksha(Path fileToCheckSha) throws NoSuchAlgorithmException {
        System.out.print("Please provide expected sha-256 digest for " + fileToCheckSha.getFileName() + ":\n> ");
        Scanner sc = new Scanner(System.in);
        String expected_SHA256_Digest = sc.next();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (InputStream is = Files.newInputStream(fileToCheckSha)) {
            byte[] buffer = new byte[BLOCK_SIZE];
            int readBytes;

            // Until you read all of the file
            while ((readBytes = is.read(buffer)) != -1) {
                digest.update(buffer, 0, readBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file");
        }

        String calculated_SHA256_Digest = Util.bytetohex(digest.digest());
        boolean digestEqual = expected_SHA256_Digest.equals(calculated_SHA256_Digest);

        System.out.println(digestEqual ?
                "Digesting completed. Digest of hw05test.bin matches expected digest."
                :
                "Digesting completed. Digest of " + fileToCheckSha.getFileName() +
                        " does not match the expected digest. Digest was: " + calculated_SHA256_Digest);
    }

    /**
     * Method to encrypt/decrypt given file using the AES crypto-algorithm and the 128-bit encryption key.
     *
     * @param fileToProcess encrypt/decrypt this file
     * @param nameOfProcessedFile output of encrypted/decrypted file
     * @param toEncrypt if method is encrypting or decrypting
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static void encryptOrDecrypt(Path fileToProcess, String nameOfProcessedFile, boolean toEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
        String keyText = sc.next();         // What user provided for password
        System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
        String ivText = sc.next();          // What user provided for initialization vector

        SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(toEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

        try (InputStream is = Files.newInputStream(fileToProcess);
             OutputStream os = Files.newOutputStream(Path.of(nameOfProcessedFile), StandardOpenOption.CREATE)) {

            byte[] buffer = new byte[BLOCK_SIZE];
            int readBytes;

            // Until you read all of the file
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(cipher.update(buffer, 0, readBytes));
            }
            os.write(cipher.doFinal());

            System.out.println(toEncrypt ?
                    "Encryption completed. Generated file " + nameOfProcessedFile + " based on file " + fileToProcess.getFileName() + "."
                    :
                    "Decryption completed. Generated file " + nameOfProcessedFile + " based of file " + fileToProcess.getFileName() + ".");
        } catch (IOException e) {
            throw new RuntimeException("Error while encrypting or decrypting");
        }
    }

}
