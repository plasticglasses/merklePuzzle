import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PuzzleCreator {
	private static Cipher ecipher;

	static ArrayList<Puzzle> puzzleList;

	public PuzzleCreator() {

	}

	/**
	 * A createPuzzles method that generates and returns an ArrayList of 4096 Puzzle
	 * objects. Arraylist is returned in a random order
	 */
	public ArrayList<Puzzle> createPuzzles() {

		ArrayList<Puzzle> merklePuzzles = new ArrayList<Puzzle>();

		// create 4096 unique puzzles
		for (int i = 0; i < 4096; i++) {
			try {
				Puzzle thisPuzzle = new Puzzle(i, CryptoLib.createKey(createRandomKey()));
				addToArray(thisPuzzle, merklePuzzles); //add to array in different order so that the array is not in puzzleNumber Order for security
				
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
	 * Add puzzle to array in random order so array is not in puzzle number order 
	 */
	private void addToArray(Puzzle thisPuzzle, ArrayList<Puzzle> merklePuzzles) {
		
		Random rand = new Random();

		// Obtain a number between [0 - size].
		int n = rand.nextInt(merklePuzzles.size()+1);
		merklePuzzles.add(n, thisPuzzle);
	}

	/**
	 * A createRandomKey method that returns a byte array that can be used to form a
	 * DES key. This byte array should be in the above specified format (final 48
	 * bits should be zeros).
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
		desKeyArray = desKey.getEncoded();

		for (int i = 0; i < desKeyArray.length; i++) {
			if (i > 1) {
				desKeyArray[i] = 0;
			}
		}

		return desKeyArray;
	}

	/**
	 * An encryptPuzzle method that takes a byte array representing a key and a
	 * puzzle object and encrypts the puzzles byte representation into a byte array
	 * representing the encrypted puzzle. The method should return this byte array.
	 * Note that if completed correctly the resulting byte array will be 32 bytes in
	 * length.
	 */
	public byte[] encryptPuzzle(byte[] keyArray, Puzzle thisPuzzle) {

		// use generated key to encrypt puzzle
		SecretKey desKey = new SecretKeySpec(keyArray, 0, keyArray.length, "DES");

		try {
			// encrypt using DES algorithm
			ecipher = Cipher.getInstance("DES");
			ecipher.init(Cipher.ENCRYPT_MODE, desKey);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] puzzleBytes = (thisPuzzle.getPuzzleAsBytes());
		byte[] encryptedPuzzle = null;

		try {
			// use random key to DES encrypt the puzzle
			encryptedPuzzle = ecipher.doFinal(puzzleBytes);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encryptedPuzzle;
	}

	/**
	 * An encryptPuzzlesToFile method that takes as a parameter a String
	 * representing a filename (e.g. 'puzzles.bin') and encrypts all 4096 puzzles
	 * and also writes them to a binary file (the byte data as it is, no spaces or
	 * new lines) with given name.
	 */
	public void encryptPuzzlesToFile(String filename) {

		deleteExistingFile(filename);

		for (int i = 0; i < 4096; i++) {
			Puzzle thisPuzzle = puzzleList.get(i);
			byte[] puzzleByteArray = thisPuzzle.getPuzzleAsBytes();
			byte[] sKeyArray = createRandomKey();
			byte[] encryptedPuzzle = encryptPuzzle(sKeyArray, thisPuzzle);

			FileOutputStream fos;
			try {
			
				fos = new FileOutputStream(new File(filename), true); //append to file rather than overwrite
				fos.write(encryptedPuzzle);

				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void deleteExistingFile(String filename) {
		try {
			File f = new File("filename"); // file to be delete
			f.delete(); // returns Boolean value
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * A findKey method that take as a parameter an int representing a puzzle number
	 * and returns the key for that puzzle as a SecretKey object.
	 */
	public SecretKey findKey(int puzzleNumber) {
		SecretKey key = null;
		for (Puzzle puzzle : puzzleList) {
			if (puzzle.getPuzzleNumber() == puzzleNumber) {
				key = puzzle.getKey();
			}
		}
		return key;
	}

	public static void main(String[] args) {
		// TEST 5
		//		PuzzleCreator myPuzzle = new PuzzleCreator();
		//		System.out.println(myPuzzle.createPuzzles().size());

		// TEST 9
		//		PuzzleCreator myPuzzle = new PuzzleCreator();
		//		System.out.println(myPuzzle.createRandomKey().length);

		// TEST 10
		//		PuzzleCreator myPuzzle = new PuzzleCreator();
		//		byte[] sKeyArray = myPuzzle.createRandomKey();
		//		for (byte value : sKeyArray) {
		//	         System.out.print(" v " + value);
		//	      }

		//		//TEST 12
		//		PuzzleCreator myPuzzle = new PuzzleCreator();
		//		
		//		ArrayList<Puzzle> myPuzzles = myPuzzle.createPuzzles();
		//		for (Puzzle puzzle : myPuzzles) {
		//			byte[] sKeyArray = myPuzzle.createRandomKey();
		//			System.out.println(myPuzzle.encryptPuzzle(sKeyArray, puzzle).length);
		//		}
		// test 15
		PuzzleCreator myPuzzle = new PuzzleCreator();
		myPuzzle.createPuzzles();
//		puzzleList = myPuzzle.createPuzzles();
//		myPuzzle.encryptPuzzlesToFile("izzytest2.bin");
//		System.out.println(myPuzzle.findKey(34));

	}
}