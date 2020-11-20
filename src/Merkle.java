import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Merkle {

	/**
	 * A constructor that takes no parameters
	 */
	Merkle(){
	
	}
	
	/**
	 * A createPuzzles method that generates and returns an ArrayList of 4096 Puzzle objects.
	 */
	public ArrayList<Puzzle> createPuzzles(){
		
		ArrayList<Puzzle> merklePuzzles = new ArrayList<Puzzle>();
		
		//create 4096 unique puzzles
		for (int i = 0; i < 4096; i++) {
			try {
				Puzzle thisPuzzle = new Puzzle(i, CryptoLib.createKey(createRandomKey()));
				merklePuzzles.add(thisPuzzle);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 	
		}
		
		return merklePuzzles;
	}
	
	/**
	 * A createRandomKey method that returns a byte array that can be used to form a DES
	 * key. This byte array should be in the above specified format (final 48 bits should be zeros).
	 */
	public byte[] createRandomKey() {
		byte[] desKeyArray = null;
		
		KeyGenerator keygenerator = null;
		try {
			keygenerator = KeyGenerator.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SecretKey desKey = keygenerator.generateKey();
        desKeyArray=desKey.getEncoded();	
		
		return desKeyArray;
	}
	
	/**
	 * An encryptPuzzle method that takes a byte array representing a key and a puzzle object
	 * and encrypts the puzzles byte representation into a byte array representing the encrypted
	 * puzzle. The method should return this byte array. Note that if completed correctly the
	 * resulting byte array will be 32 bytes in length.
	 */
	public byte[] encryptPuzzle(byte[] keyArray, Puzzle thisPuzzle) {
		
		byte[] encryptedPuzzle = null;
		
		return encryptedPuzzle;
	}
	
	/**
	 * An encryptPuzzlesToFile method that takes as a parameter a String representing a filename (e.g. “puzzles.bin”) and encrypts all 4096 puzzles and also writes them to a binary
	 * file (the byte data as it is, no spaces or new lines) with given name.
	 */
	public void encryptPuzzlesToFile(String filename) {
		
	}
	
	
	/**
	 * A findKey method that take as a parameter an int representing a puzzle number and
	 * returns the key for that puzzle as a SecretKey object.
	 */
	public SecretKey findKey(int puzzleNumber) {
		
		SecretKey key = null;
		return key;
	}
	
	public static void main(String[] args) {
		//TEST 5
		Merkle myMerklePuzzles = new Merkle();
		 ArrayList<Puzzle> puzzles = myMerklePuzzles.createPuzzles();
		 for(Puzzle puzzle: puzzles) {
			 System.out.println(puzzle.toString());
		 }
		 
	}
	
}
