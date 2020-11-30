import java.io.File;
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
		
		//create a new set of puzzles
		PuzzleCreator alicesPuzzles = new PuzzleCreator();
		
		String filename = "merklePuzzlesTestOutput.bin";
		
		//check that this file doesnt already exist
		try {
			File f = new File(filename); // file to be delete
			f.delete(); // returns Boolean value
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		alicesPuzzles.puzzleList = alicesPuzzles.createPuzzles(); //make the 4096 puzzles
		alicesPuzzles.encryptPuzzlesToFile(filename); //encrypt puzzles and add all puzzles to file

		PuzzleCracker bob = new PuzzleCracker(filename); //make a new merklePuzzle player
		
		Puzzle bobsPuzzle = bob.crack(4095); //brute force puzzle 4095 in the list and get a puzzle in return

		//for debugging
//		System.out.println(alicesPuzzles.puzzleList.get(45));
//		System.out.println(bobsPuzzle);
//		System.out.println(bobsPuzzle.getPuzzleNumber());
		
		SecretKey secretKey = alicesPuzzles.findKey(bobsPuzzle.getPuzzleNumber()); //give player Alice the puzzle number so she can find the correct secret key to use to decrypt the message bob sent
		
		//encrypt and decrypt messages
		try {
			//Alice encrypt a message with the secret key discussed
			Cipher encryptionCipher = Cipher.getInstance("DES");
			encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			String message = "Jaffa Cakes Rule";
			byte[] messageBytes = message.getBytes("UTF8");
			byte[] enc = encryptionCipher.doFinal(messageBytes);
			String encString = new String(Base64.getEncoder().encodeToString(enc));
			System.out.println("Encryped message " + encString);
			
			//Bob decrypt the message Alice sent
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
