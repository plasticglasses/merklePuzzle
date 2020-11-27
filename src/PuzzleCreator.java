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
	 * objects.
	 */
	public ArrayList<Puzzle> createPuzzles() {

		ArrayList<Puzzle> merklePuzzles = new ArrayList<Puzzle>();

		// create 4096 unique puzzles
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

		try {
			File f = new File("filename"); // file to be delete
			f.delete(); // returns Boolean value
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 4096; i++) {
			Puzzle thisPuzzle = puzzleList.get(i);
			byte[] puzzleByteArray = thisPuzzle.getPuzzleAsBytes();
			byte[] sKeyArray = createRandomKey();
			byte[] encryptedPuzzle = encryptPuzzle(sKeyArray, thisPuzzle);

			// System.out.println(Arrays.toString(puzzleByteArray));
			// System.out.println(Arrays.toString(encryptedPuzzle));

			FileOutputStream fos;
			try {
				fos = new FileOutputStream(new File(filename), true);

				/*
				 * To write byte array to a file, use void write(byte[] bArray) method of Java
				 * FileOutputStream class.
				 *
				 * This method writes given byte array to a file.
				 */

				fos.write(encryptedPuzzle);

				/*
				 * Close FileOutputStream using, void close() method of Java FileOutputStream
				 * class.
				 *
				 */

				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		////		try {
		//		    File myFile = new File(filename);
		//			FileOutputStream fos = null;
		//			
		//			try {
		//				fos = new FileOutputStream(myFile, true);
		//			
		//
		//				          /* This logic will check whether the file
		//					   * exists or not. If the file is not found
		//					   * at the specified location it would create
		//					   * a new file*/
		//			if (!myFile.exists()) {
		//					myFile.createNewFile();
		//			}
		//			
		//				//for each puzzle encrypt and save
		////			for (Puzzle puzzle : puzzleList) {
		//				byte[] sKeyArray = createRandomKey();
		//				byte[] encryptedPuzzle = encryptPuzzle(sKeyArray, puzzle);
		//				
		//				  FileOutputStream fos1 = new FileOutputStream("myFile" + puzzle.getPuzzleNumber());
		//				  
		//				  /*
		//				   * To write byte array to a file, use
		//				   * void write(byte[] bArray) method of Java FileOutputStream class.
		//				   *
		//				   * This method writes given byte array to a file.
		//				   */
		//				for (byte b :encryptedPuzzle){
		//				   fos1.write(b);}
		//
		//				  /*
		//				   * Close FileOutputStream using,
		//				   * void close() method of Java FileOutputStream class.
		//				   *
		//				   */
		//
		//				   fos1.close();
		//				
		//				for (byte b:encryptedPuzzle){
		////					System.out.println(b);
		//					fos.write(b);
		//				}
		//			}
		//					 } catch (FileNotFoundException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//
		// catch (IOException e) {
		//						// TODO Auto-generated catch block
		//						e.printStackTrace();
		//					}

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
		puzzleList = myPuzzle.createPuzzles();
		myPuzzle.encryptPuzzlesToFile("izzytest2.bin");

		System.out.println(myPuzzle.findKey(34));

		////		
		//		try {
		//		    // create a reader
		//		    FileInputStream fis = new FileInputStream(new File("izzytest.bin"));
		//
		//		    // read one byte at a time
		//		    int ch;
		//		    while ((ch = fis.read()) != -1) {
		//		        System.out.println((int) ch);
		//		    }
		//
		//		    // close the reader
		//		    fis.close();
		//
		//		} catch (IOException ex) {
		//		    ex.printStackTrace();
		//		}
		//		
		//		Tests.test15();

		//		try {
		//		    // create a reader
		//		    FileInputStream fis = new FileInputStream(new File("merkle.bin"));
		//
		//		    // read one byte at a time
		//		    int ch;
		//		    while ((ch = fis.read()) != -1) {
		//		        System.out.print((char) ch);
		//		    }
		//
		//		    // close the reader
		//		    fis.close();
		//
		//		} catch (IOException ex) {
		//		    ex.printStackTrace();
		//		}
	}

}

//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.KeyGenerator;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//
//public class PuzzleCreator {
//	private static Cipher ecipher;
//	
//	static ArrayList<Puzzle> puzzleList;
//	
//	public PuzzleCreator(){
//		
//	}
//
//
//	/**
//	 * A createPuzzles method that generates and returns an ArrayList of 4096 Puzzle objects.
//	 */
//	public ArrayList<Puzzle> createPuzzles(){
//		
//		ArrayList<Puzzle> merklePuzzles = new ArrayList<Puzzle>();
//		
//		//create 4096 unique puzzles
//		for (int i = 0; i < 4096; i++) {
//			try {
//				Puzzle thisPuzzle = new Puzzle(i, CryptoLib.createKey(createRandomKey()));
//				merklePuzzles.add(thisPuzzle);
//			} catch (InvalidKeyException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvalidKeySpecException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			 	
//		}
//		
//		return merklePuzzles;
//	}
//	
//	/**
//	 * A createRandomKey method that returns a byte array that can be used to form a DES
//	 * key. This byte array should be in the above specified format (final 48 bits should be zeros).
//	 */
//	public byte[] createRandomKey() {
//		byte[] desKeyArray = null;
//		
//		KeyGenerator keygenerator = null;
//		try {
//			keygenerator = KeyGenerator.getInstance("DES");
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//      SecretKey desKey = keygenerator.generateKey();
//      desKeyArray=desKey.getEncoded();	
//		
//      for(int i=0;i<desKeyArray.length;i++)
//      {
//        if (i > 1){
//      	  desKeyArray[i] = 0;
//        }
//      }
//      
//		return desKeyArray;
//	}
//	
//	/**
//	 * An encryptPuzzle method that takes a byte array representing a key and a puzzle object
//	 * and encrypts the puzzles byte representation into a byte array representing the encrypted
//	 * puzzle. The method should return this byte array. Note that if completed correctly the
//	 * resulting byte array will be 32 bytes in length.
//	 */
//	public byte[] encryptPuzzle(byte[] keyArray, Puzzle thisPuzzle) {
//		
//		//use generated key to encrypt puzzle
//		SecretKey desKey = new SecretKeySpec(keyArray, 0, keyArray.length, "DES");
//		
//		try {
//			//encrypt using DES algorithm
//			ecipher = Cipher.getInstance("DES");
//			ecipher.init(Cipher.ENCRYPT_MODE, desKey);
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchPaddingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		byte[] puzzleBytes = (thisPuzzle.getPuzzleAsBytes());
//		byte[] encryptedPuzzle = null;
//		
//		try {
//			//use random key to DES encrypt the puzzle 
//			encryptedPuzzle = ecipher.doFinal(puzzleBytes);
//		} catch (IllegalBlockSizeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BadPaddingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//		return encryptedPuzzle;
//	}
//	
//	/**
//	 * An encryptPuzzlesToFile method that takes as a parameter a String representing a filename (e.g. 'puzzles.bin') and encrypts all 4096 puzzles and also writes them to a binary
//	 * file (the byte data as it is, no spaces or new lines) with given name.
//	 */
//	public void encryptPuzzlesToFile(String filename) {
//		
//		
////		try {
//			
//		    File myFile = new File(filename);
//		
//			FileOutputStream fos = null;
//
//			
//			try {
//				fos = new FileOutputStream(myFile, true);
//			
//
//				          /* This logic will check whether the file
//					   * exists or not. If the file is not found
//					   * at the specified location it would create
//					   * a new file*/
//			if (!myFile.exists()) {
//					myFile.createNewFile();
//			}
//			
//				//for each puzzle encrypt and save
//			for (Puzzle puzzle : puzzleList) {
//				byte[] sKeyArray = createRandomKey();
//				byte[] encryptedPuzzle = encryptPuzzle(sKeyArray, puzzle);
//				
////				  FileOutputStream fos1 = new FileOutputStream("myFile" + puzzle.getPuzzleNumber());
//				  
//
//				  /*
//				   * To write byte array to a file, use
//				   * void write(byte[] bArray) method of Java FileOutputStream class.
//				   *
//				   * This method writes given byte array to a file.
//				   */
////				for (byte b :encryptedPuzzle){
////				   fos1.write(b);}
//
//				  /*
//				   * Close FileOutputStream using,
//				   * void close() method of Java FileOutputStream class.
//				   *
//				   */
//
////				   fos1.close();
//				
//				for (byte b:encryptedPuzzle){
////					System.out.println(b);
//					fos.write(b);
//				}
//			}
//					 } catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		        
//	}
//	
//	
//	/**
//	 * A findKey method that take as a parameter an int representing a puzzle number and
//	 * returns the key for that puzzle as a SecretKey object.
//	 */
//	public SecretKey findKey(int puzzleNumber) {
//		Puzzle thisPuzzle = puzzleList.get(puzzleNumber);
//		return thisPuzzle.getKey();
//	}
//	
//	
//	public static void main(String[] args) {
//		//TEST 5
////		PuzzleCreator myPuzzle = new PuzzleCreator();
////		System.out.println(myPuzzle.createPuzzles().size());
//		 
//		//TEST 9
////		PuzzleCreator myPuzzle = new PuzzleCreator();
////		System.out.println(myPuzzle.createRandomKey().length);
//		
//		//TEST 10
////		PuzzleCreator myPuzzle = new PuzzleCreator();
////		byte[] sKeyArray = myPuzzle.createRandomKey();
////		for (byte value : sKeyArray) {
////	         System.out.print(" v " + value);
////	      }
//		
////		//TEST 12
////		PuzzleCreator myPuzzle = new PuzzleCreator();
////		
////		ArrayList<Puzzle> myPuzzles = myPuzzle.createPuzzles();
////		for (Puzzle puzzle : myPuzzles) {
////			byte[] sKeyArray = myPuzzle.createRandomKey();
////			System.out.println(myPuzzle.encryptPuzzle(sKeyArray, puzzle).length);
////		}
//		
//		
//		//TEST 18
//		PuzzleCreator myPuzzle = new PuzzleCreator();
//		puzzleList = myPuzzle.createPuzzles();
//		System.out.println(myPuzzle.findKey(10));
//		myPuzzle.encryptPuzzlesToFile("puzzles.bin");
//		
////		PuzzleCreator myPuzzle = new PuzzleCreator();
//////		puzzleList = myPuzzle.createPuzzles();
//////		for (Puzzle puzzle: puzzleList) {
//////			//System.out.println(puzzle);
//////		}
//////		myPuzzle.encryptPuzzlesToFile("puzzles.bin");
//////		//System.out.println(myPuzzle.findKey(10));
//////		
//////		//read file 
//////		
//////		Path path = Paths.get("puzzles.bin");
//////
//////		// Load as binary:
//////
//////		try {
//////			byte[] bytes = Files.readAllBytes(path);
//////			for(int i=0; i< 32 ; i++) {
//////		         System.out.print(bytes[i] +" ");
//////		         
//////		      }
//////			System.out.println("woops");
//////			
//////
//////		} catch (IOException e) {
//////		
//////			e.printStackTrace();
//////		}
//////		
//		Tests.test1();
//		Tests.test2();
//		Tests.test3();
//		Tests.test5();
//		Tests.test6();
//		Tests.test9();
//		Tests.test10();
//		Tests.test12();
//		Tests.test13();
//		Tests.test15();
//		Tests.test16();
//		Tests.test18();
////		
//	}
//	}
//
//	
////}