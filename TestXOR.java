import java.util.Arrays;
import java.nio.charset.StandardCharsets;

public class TestXOR {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Test...\n");
        System.out.println("Test 1: Answered Tests");
        byte[] pt1 = XOR.hexStringToByte("41747461636b206174206461776e21");
        byte[] k1 = XOR.hexStringToByte("494345");
        assert XOR.byteToHexString(XOR.DecryptOrEncrypt(pt1, k1)).equals("08373128202e6922316927243e2d64") : "Table 1 Row 1 Failed";
        
        byte[] pt2 = XOR.hexStringToByte("68656c6c6f");
        byte[] k2 = XOR.hexStringToByte("6b6579");
        assert XOR.byteToHexString(XOR.DecryptOrEncrypt(pt2, k2)).equals("030015070a") : "Table 1 Row 2 Failed";
        
        byte[] pt3 = XOR.hexStringToByte("00010203feff");
        byte[] k3 = XOR.hexStringToByte("a55a");
        assert XOR.byteToHexString(XOR.DecryptOrEncrypt(pt3, k3)).equals("a55ba7595ba5") : "Table 1 Row 3 Failed";
        System.out.println("------Passed");

        System.out.println("Test 2: Round-trip");
        byte[] message = "Cryptography is fun!".getBytes(StandardCharsets.UTF_8);
        byte[] rtKey = XOR.hexStringToByte("deadbeef");
        byte[] cipher = XOR.DecryptOrEncrypt(message, rtKey);
        assert Arrays.equals(message, XOR.DecryptOrEncrypt(cipher, rtKey)) : "Round-trip failed";
        System.out.println("------Passed");

        System.out.println("Test 3: Empty message");
        byte[] emptyMsg = new byte[0];
        assert XOR.DecryptOrEncrypt(emptyMsg, rtKey).length == 0 : "Empty message encryption failed";
        assert XOR.DecryptOrEncrypt(emptyMsg, rtKey).length == 0 : "Empty message DecryptOrEncryption failed";
        System.out.println("------Passed");

        System.out.println("Test 4: Message longer than key");
        byte[] longMsg = "This message is much longer than the key".getBytes();
        byte[] shortKey = XOR.hexStringToByte("ab");
        byte[] encryptedLong = XOR.DecryptOrEncrypt(longMsg, shortKey);
        assert Arrays.equals(longMsg, XOR.DecryptOrEncrypt(encryptedLong, shortKey)) : "Long message failed";
        System.out.println("------Passed");

        System.out.println("Test 5: Key longer than message");
        byte[] shortMsg = "Hi".getBytes();
        byte[] longKey = XOR.hexStringToByte("abcdef1234567890");
        byte[] encryptedShort = XOR.DecryptOrEncrypt(shortMsg, longKey);
        assert Arrays.equals(shortMsg, XOR.DecryptOrEncrypt(encryptedShort, longKey)) : "Long key failed";
        System.out.println("------Passed");

        System.out.println("Test 6: Multibyte UTF-8 characters");
        byte[] utf8Msg = "Hello 🌍".getBytes(StandardCharsets.UTF_8); 
        byte[] utf8Cipher = XOR.DecryptOrEncrypt(utf8Msg, rtKey);
        assert Arrays.equals(utf8Msg, XOR.DecryptOrEncrypt(utf8Cipher, rtKey)) : "Multibyte UTF-8 failed";
        System.out.println("------Passed");

        System.out.println("Test 7: Arbitrary non-text bytes");
        byte[] randomBytes = {(byte) 0xFF, (byte) 0x00, (byte) 0x80, (byte) 0x7F};
        byte[] randomCipher = XOR.xorEncode(randomBytes, rtKey);
        assert Arrays.equals(randomBytes, XOR.xorEncode(randomCipher, rtKey)) : "Non-text bytes failed";
        System.out.println("------Passed");

        System.out.println("Test 8: CLI Rejection tests (Checking for nonzero exit statuses)");
        testRejection(new String[]{"java", "-cp", ".", "XOR", "encrypt", "--key", "", "--text", "hi"}, "Empty key");
        testRejection(new String[]{"java", "-cp", ".", "XOR", "encrypt", "--key", "abc", "--text", "hi"}, "Odd-length hex");
        testRejection(new String[]{"java", "-cp", ".", "XOR", "encrypt", "--key", "abcz", "--text", "hi"}, "Invalid hex char");
        testRejection(new String[]{"java", "-cp", ".", "XOR", "encrypt", "--key", " ab ", "--text", "hi"}, "Whitespace");
        testRejection(new String[]{"java", "-cp", ".", "XOR", "keygen", "--length", "-5"}, "Negative length");
        testRejection(new String[]{"java", "-cp", ".", "XOR", "keygen", "--length", "0"}, "Zero length");
        testRejection(new String[]{"java", "-cp", ".", "XOR", "DecryptOrEncrypt", "--key", "00", "--ciphertext", "ff"}, "Invalid UTF-8 recovery");
        System.out.println("------Passed");
        
        System.out.println("Test 9: Key generation format and length");
        byte[] generatedKey = XOR.keyGen(15);
        String hexGen = XOR.byteToHexString(generatedKey);
        assert hexGen.length() == 30 : "Keygen length is incorrect"; 
        assert hexGen.matches("^[0-9a-f]+$") : "Keygen format is not lowercase hex";
        System.out.println("------Passed");
        System.out.println("\nSUCCESS: All tests passed");

        System.out.println("Bonus Test: Ciphertext Malleability");
        //Original message and random key (attacker does not know the key)
        byte[] originalPlaintext = "Pay Joe $100".getBytes(StandardCharsets.UTF_8);
        byte[] secretKey = XOR.keyGen(15); 
        byte[] targetCiphertext = XOR.DecryptOrEncrypt(originalPlaintext, secretKey);

        // Change "$100" to "$900"
        // The attacker targets the character '1' at index 9.
        int targetIndex = 9; 
        byte m_i = originalPlaintext[targetIndex];                                                          
        byte m_hat_i = (byte) '9';

        // Apply the malleability equation: c_i' = c_i XOR m_i XOR m_hat_i
        targetCiphertext[targetIndex] = (byte) (targetCiphertext[targetIndex] ^ m_i ^ m_hat_i);

        // Demonstrate Modified Decryption
        byte[] manipulatedPlaintext = XOR.DecryptOrEncrypt(targetCiphertext, secretKey);
        String manipulatedString = new String(manipulatedPlaintext, StandardCharsets.UTF_8);
        
        // Assert the change was successful without touching the key
        assert manipulatedString.equals("Pay Joe $900") : "Malleability attack failed!";
        System.out.println("------Passed (Result: " + manipulatedString + ")");
    }
    private static void testRejection(String[] args, String testName) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(args);
        Process process = pb.start();
        int exitCode = process.waitFor();
        assert exitCode != 0 : "Rejection test failed (returned 0 success status): " + testName;
    }
    
}