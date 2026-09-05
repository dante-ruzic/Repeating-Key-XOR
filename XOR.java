import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
public class XOR 
{
    //Command line interface for XOR encryption and decryption.
    public static void main (String[] args)
    {
        //Error handling for command line argument, "no command given". Results in error code 1 for CTRL+F lookup.
        if (args.length ==0)
            errorMSG("No command was given, please provide one of the following: keygen, encrypt, decrypt.",1);
        //Command line argument parsing.
        String command = args[0];
        try 
        {
            //Command line argument parsing for keygen, encrypt, and decrypt commands. Results in error code 2-5 for CTRL+F lookup.
            if (command.equals("keygen"))
            {
                //Command line argument parsing for keygen command. Results in error code 2 for CTRL+F lookup.
                if (args.length==3&&args[1].equals("--length"))
                {
                    int length = Integer.parseInt(args[2]);
                    byte[] key = keyGen(length);
                    System.out.println(byteToHexString(key));
                } else
                    errorMSG("Command Usage: keygen --length [postive integer]",2);
            } else if (command.equals("encrypt"))
            {
                //Command line argument parsing for encrypt command. Results in error code 3 for CTRL+F lookup.
                if (args.length ==5)
                {
                    //Variable creatioon for both key and text Strings.
                    String keyStr = null;
                    String textStr = null;
                    //Code for parsing the command line arguments of --key and --text.
                    for (int i =1;i<args.length;i+=2)
                    {
                        if (args[i].equals("--key"))
                            keyStr = args[i+1];
                        else if (args[i].equals("--text"))
                            textStr=args[i+1];
                    }
                    //Error handling for null arguments of --key and --text.
                    if (keyStr ==null || textStr==null)
                        errorMSG("Command Usage: encrypt --key [hex] --text [UTF-8]",3);
                    //Byte variable creation for both key and text Strings, leads to XOR encryption and outputs in hex format.
                    byte[] key=hexStringToByte(keyStr);
                    byte[] text = textStr.getBytes(StandardCharsets.UTF_8);
                    byte[] cipherBytes = DecryptOrEncrypt(text, key);
                    System.out.print(byteToHexString(cipherBytes));
                } else
                    errorMSG("Command Usage: encrypt --key [hex] --text [UTF-8]",3);
            } else if (command.equals("decrypt"))
            {
                //Command line argument parsing for decrypt commands. Results in error code 4 for CTRL+F lookup.
                if (args.length ==5)
                {
                    //Variable creation for both key and ciphertext Strings.
                    String keyStr = null;
                    String cipherStr = null;
                    //Code for parsing the command line arguments of --key and --ciphertext.
                    for (int i =1;i<args.length;i+=2)
                    {
                        if (args[i].equals("--key"))
                            keyStr = args[i+1];
                        else if (args[i].equals("--ciphertext"))
                            cipherStr=args[i+1];
                    }
                    //Error handling for null arguments of --key and --ciphertext.
                    if (keyStr ==null || cipherStr==null)
                        errorMSG("Command Usage: decrypt --key [hex] --ciphertext [hex]",4);
                    //Byte variable creation for both key and ciphertext Strings, leads to XOR decryption and outputs in UTF-8 format.
                    byte[] key=hexStringToByte(keyStr);
                    byte[] cipher = hexStringToByte(cipherStr);
                    byte[] bytes = DecryptOrEncrypt(cipher,key);
                    System.out.print(decodeUTF8(bytes));
                //Error message for invalid arguments.
                } else
                    errorMSG("Command Usage: decrypt --key [hex] --ciphertext [hex]",4);
            } else
            {
                errorMSG("Unknown Command",5);
            }
        //Error handling for invalid number format in command line arguments. Results in error code 6 for CTRL+F lookup.
        } catch (NumberFormatException x)
        {
            errorMSG("Invalid format",6);
        }
    }
    //XOR encryption and decryption function.
    public static byte[] xorEncode(byte[] input, byte[] key)
    {
        if(key.length<=0)
            errorMSG("Key must be filled.",7);
        byte[] end = new byte[input.length];
        for (int i=0;i<input.length;i++)
            end[i] = (byte)(input[i]^key[i%key.length]);
        return end;
    }
    //Function for both decryption and encryption.
    public static byte[] DecryptOrEncrypt (byte[] input, byte[] key)
    {
        return xorEncode(input, key);
    }
    //Function for converting hex String to byte array.
    public static byte[] hexStringToByte(String input)
    {
        //Error handling for invalid hex String. Results in error code 8-9 for CTRL+F lookup.
        if (input.length()%2!=0)
            errorMSG("Hex String has to be even length", 8);
        else if (!input.matches("^[0-9a-fA-F]*$"))
            errorMSG("invalid characters.", 9);
        //Convert hex string to byte array.
        byte[] result = new byte[input.length()/2];
        //Loop through the hex string and convert each pair of characters to a byte.
        for (int i=0;i<input.length();i+=2)
        {
            int top = Character.digit(input.charAt(i),16);
            int bottom = Character.digit(input.charAt(i+1),16);
            result[i/2]=(byte)((top<<4)+bottom);
        }
        return result;
    }
    //Function for converting a byte array to a hex string.
    public static String byteToHexString(byte[] input)
    {
        //Stri
        StringBuilder hexString = new StringBuilder();
        for (byte x : input)
        {
            String hex = Integer.toHexString(0xff &x);
            if (hex.length()==1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().toLowerCase();
    }
    //Function for generating a random key of specified length.
    public static byte[] keyGen(int length)
    {
        if (length<=0)
            errorMSG("Key must be an integer above 0.", 10);
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[length];
        random.nextBytes(key);
        return key;
    }
    public static void errorMSG(String input, int function)
    {
        System.err.println(input);
        System.exit(function);
    }
    public static String decodeUTF8(byte[] input)
    {
        try
        {
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            decoder.onMalformedInput(CodingErrorAction.REPORT);
            decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
            return decoder.decode(ByteBuffer.wrap(input)).toString();
        } catch (CharacterCodingException x)
        {
            errorMSG("Decrypted Byte not UTF-8",11);
            return null;
        }
    }
}
