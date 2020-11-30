import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class to enable creation of 4096 encrypted unique puzzles and capability save them to a binary file.
 * @author lizks
 * @since 2020-11-30
 */

public class PuzzleCreator {
	
	/**
	 * A Cipher used to encrypt puzzles
	 */
	private static Cipher ecipher;
	
	/**
	 * the arrayList holding 4096 puzzles
	 */
	private ArrayList<Puzzle> puzzleList;

	
	/**
	 * Create an empty arraList
	 */
	public PuzzleCreator() {
		this.puzzleList = createPuzzles();
	}


	/**
	 * get the puzzleList
	 * @return puzzleList a list of 4096 puzzles
	 */
	public ArrayList<Puzzle> getPuzzleList() {
		return puzzleList;
	}


	/**
	 * Set the puzzle list for this set of puzzles
	 * @param puzzleList return an ArrayList of puzzles
	 */
	public void setPuzzleList(ArrayList<Puzzle> puzzleList) {
		this.puzzleList = puzzleList;
	}
	
	
	/**
	 * A createPuzzles method that generates and returns an ArrayList of 4096 Puzzle
	 * objects. Arraylist is returned in a random order
	 * @return merklePuzzles an unordered arrayList which includes 4096  puzzles (not encrypted)
	 */
	public ArrayList<Puzzle> createPuzzles() {

		ArrayList<Puzzle> merklePuzzles = new ArrayList<Puzzle>();

		// create 4096 unique puzzles
		for (int i = 0; i < 4096; i++) {
			try {
				Puzzle thisPuzzle = new Puzzle(i, CryptoLib.createKey(createRandomKey())); //create a new puzzle with a unique Secret Key
				addToArray(thisPuzzle, merklePuzzles); //add to array in different order so that the array is not in puzzleNumber Order for security
				
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return merklePuzzles;
	}

	
	/**
	 * Add puzzle to array in random order so array is not in puzzle number order 
	 * @param thisPuzzle A puzzle object (not encrypted) containing a puzzle number and a key
	 * @param merklePuzzles An arrayList containing <4097 puzzles randomly ordered
	 */
	private void addToArray(Puzzle thisPuzzle, ArrayList<Puzzle> merklePuzzles) {
		
		Random rand = new Random();

		// Obtain a number between 0 and array size.
		int n = rand.nextInt(merklePuzzles.size()+1);
		merklePuzzles.add(n, thisPuzzle); //puzzle added into arrayLIst at random position
	}

	
	/**
	 * A createRandomKey method that returns a byte array that can be used to form a
	 * DES key. This byte array should be in the above specified format (final 48
	 * bits should be zeros).
	 * @return a byte array that holds the byte of a Secret Key key
	 */
	public byte[] createRandomKey() {
		byte[] desKeyArray = null; //empty array to store secret key

		KeyGenerator keygenerator = null;
		
		try {
			keygenerator = KeyGenerator.getInstance("DES"); //make a unique DES key generator
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		SecretKey desKey = keygenerator.generateKey(); //make a unique DES key
		desKeyArray = desKey.getEncoded(); //change the key into a byte array

		//Of 8 byte array(64 bits), set the final 48 bits should be zeros
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
	 * @param keyArray A byte array that holds the bytes of a secret key
	 * @param thisPuzzle a puzzle that is not encrypted
	 * @return a single encrypted puzzle
	 */
	public byte[] encryptPuzzle(byte[] keyArray, Puzzle thisPuzzle) {

		// use generated key to encrypt puzzle, change a byte array into a SecretKey
		SecretKey desKey = new SecretKeySpec(keyArray, 0, keyArray.length, "DES");

		try {
			//set up encryption using DES algorithm and unique secretKey
			ecipher = Cipher.getInstance("DES");
			ecipher.init(Cipher.ENCRYPT_MODE, desKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		byte[] puzzleBytes = (thisPuzzle.getPuzzleAsBytes()); //get puzzle in bytes
		byte[] encryptedPuzzle = null; //make new array for encrypted puzzle

		try {
			encryptedPuzzle = ecipher.doFinal(puzzleBytes);// use random key to DES encrypt the puzzle into byte array
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return encryptedPuzzle;
	}

	
	/**
	 * An encryptPuzzlesToFile method that takes as a parameter a String
	 * representing a filename (e.g. 'puzzles.bin') and encrypts all 4096 puzzles
	 * and also writes them to a binary file (the byte data as it is, no spaces or
	 * new lines) with given name.
	 * @param filename A string filename
	 */
	public void encryptPuzzlesToFile(String filename) {

		deleteExistingFile(filename); //f file exists with the filename, delete it

		//for each puzzle, encrypt and save to file
		for (int i = 0; i < 4096; i++) {
			
			Puzzle thisPuzzle = getPuzzleList().get(i);
			byte[] puzzleByteArray = thisPuzzle.getPuzzleAsBytes(); //unencrypted puzzle
			byte[] sKeyArray = createRandomKey(); //DES encryption key
			byte[] encryptedPuzzle = encryptPuzzle(sKeyArray, thisPuzzle); // encrypted puzzle

			//saving to binary file
			FileOutputStream fos;
			try {
			
				fos = new FileOutputStream(new File(filename), true); //append to file rather than overwrite
				fos.write(encryptedPuzzle); //add encrypted puzzle
				fos.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * deletes file if it has the same filename
	 * @param filename name of the file
	 */
	private void deleteExistingFile(String filename) {
		try {
			File f = new File(filename); // file to be deleted
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * A findKey method that take as a parameter an int representing a puzzle number
	 * and returns the key for that puzzle as a SecretKey object.
	 * @param puzzleNumber An integer value of the puzzleNumber
	 * @return key The key specific to the puzzleNumber given
	 */
	public SecretKey findKey(int puzzleNumber) {
		SecretKey key = null;
		
		//search through the puzzle list for the correct key to use
		for (Puzzle puzzle : getPuzzleList()) {
			if (puzzle.getPuzzleNumber() == puzzleNumber) {
				key = puzzle.getKey();
			}
		}
		return key;
	}

	
	public static void main(String[] args) {
		// test 15
		PuzzleCreator myPuzzle = new PuzzleCreator();
		
		myPuzzle.setPuzzleList(myPuzzle.createPuzzles());
		myPuzzle.encryptPuzzlesToFile("izzytest2.bin");
		System.out.println(myPuzzle.findKey(34));
		
		Tests.test16();
		Tests.test18();
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
//		PuzzleCreator myPuzzle = new PuzzleCreator();
//		myPuzzle.createPuzzles();
		
//		puzzleList = myPuzzle.createPuzzles();
//		myPuzzle.encryptPuzzlesToFile("izzytest2.bin");
//		System.out.println(myPuzzle.findKey(34));

	}
}