import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Merkle {

	public static void main(String args[]) {
		PuzzleCreator alicesPuzzles = new PuzzleCreator();
		alicesPuzzles.puzzleList = alicesPuzzles.createPuzzles();
		alicesPuzzles.encryptPuzzlesToFile("izzytest.bin");

		PuzzleCracker bob = new PuzzleCracker("izzytest.bin");
		
		Puzzle bobsPuzzle = bob.crack(4);

//		System.out.println(alicesPuzzles.puzzleList.get(45));
		System.out.println(bobsPuzzle);
		System.out.println(bobsPuzzle.getPuzzleNumber());
		SecretKey secretKey = alicesPuzzles.findKey(bobsPuzzle.getPuzzleNumber());
		
		try {

			//encrypt a message with the secret key discussed
			Cipher encryptionCipher = Cipher.getInstance("DES");
			encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			String message = "Jaffa Cakes Rule";
			byte[] messageBytes = message.getBytes("UTF8");
			byte[] enc = encryptionCipher.doFinal(messageBytes);
			String encString = new String(Base64.getEncoder().encodeToString(enc));
			System.out.println("Encryped message " + encString);
			
			//Bob decrypt the secret key
			bob.decryptMessage(enc);

		} catch (NoSuchAlgorithmException e) {
			System.out.println("No Such Algorithm:" + e.getMessage());
			return;
		} catch (NoSuchPaddingException e) {
			System.out.println("No Such Padding:" + e.getMessage());
			return;
		} catch (InvalidKeyException e) {
			System.out.println("Invalid Key:" + e.getMessage());
			return;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
