import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Merkle {

	public static void main(String args[]) {
		PuzzleCreator alicesPuzzles = new PuzzleCreator();
		alicesPuzzles.puzzleList = alicesPuzzles.createPuzzles();
		alicesPuzzles.encryptPuzzlesToFile("puzzles.bin");
		
		//BOB crack a puzzles
		PuzzleCracker bob = new PuzzleCracker();
		Puzzle bobsPuzzle = bob.crack(34);
		
		SecretKey aKey = alicesPuzzles.findKey(34);
		
		try {
			 
 
            Cipher ecipher = Cipher.getInstance("DES");
            Cipher dcipher = Cipher.getInstance("DES");
 
            // initialize the ciphers with the given key
 
  ecipher.init(Cipher.ENCRYPT_MODE, aKey);
 
  dcipher.init(Cipher.DECRYPT_MODE, aKey);
 
  String message = "Testing Merkle Puzzles";
  
  byte[] messageBytes = message.getBytes("UTF8");
	 
byte[] enc = ecipher.doFinal(messageBytes);

// encode to base64

String encString = new String(Base64.getEncoder().encodeToString(enc));

System.out.println("Encryped message " + encString);
bob.decryptMessage(enc);
 
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm:" + e.getMessage());
            return;
        }
        catch (NoSuchPaddingException e) {
            System.out.println("No Such Padding:" + e.getMessage());
            return;
        }
        catch (InvalidKeyException e) {
            System.out.println("Invalid Key:" + e.getMessage());
            return;
        }
		
	}
	
	
	
	
	
	
}
