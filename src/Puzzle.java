import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * represents a Puzzle
 * @author liz
 */

public class Puzzle{
	
	int puzzleNum;
	SecretKey secretKey;
	byte[] puzzle;
	
/**
 * A constructor that takes a puzzle number as an int and a secret key as a SecretKey object and then constructs a Puzzle object.
 * @param puzzleNum - the id of the puzzle
 * @param sKey - the secret key of the puzzle
 */
	public Puzzle(int puzzleNum, SecretKey sKey){
		
		//create the 3 parts of the puzzle
		byte[] plaintextArray = new byte[16];
		byte[] puzzleNumArray = null;
		byte[] sKeyArray = new byte[8];
		
		
		Arrays.fill(plaintextArray, (byte) 0);										//populate byte array for plaintext with 0's
		puzzleNumArray = ByteBuffer.allocate(2).putShort((short)puzzleNum).array(); //populate byte array for puzzlenumber from the input integer
	    sKeyArray=sKey.getEncoded();												//populate byte array for secret key with secret key input

	    //join the three separate byte arrays into a single puzzle array
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
	    try {
	    	outputStream.write( plaintextArray );
			outputStream.write( puzzleNumArray );
			outputStream.write( sKeyArray );
	     }catch (IOException e) {
			e.printStackTrace();
	     }
	     byte puzzle[] = outputStream.toByteArray( );
	     
	     //assign the puzzle variables
	     this.puzzleNum = puzzleNum; 
		 this.secretKey = sKey;
	     this.puzzle = puzzle;
	}
	
	
/**
 * A method getPuzzleNumber that returns the puzzles number as an int.
 */
	public int getPuzzleNumber(){
		return this.puzzleNum;
	}
	
	
/**
 * A method getKey that returns the puzzles key as a SecretKey.
 */
	public SecretKey getKey(){
		return this.secretKey;
	}
	

/**
 *  A method getPuzzleAsBytes that returns a byte array representing the puzzle.
 *  Each puzzle is a cryptogram whose plaintext starts out with 128 zero bits (16-bytes), 
 *  followed by a 16-bit (2-byte) puzzle number in the range 1 to 4096, 
 *  and then a 64-bit (8-byte) key.
 */
	public byte[] getPuzzleAsBytes(){		
		return this.puzzle;
		
	}
	
	
	
	
	@Override
public String toString() {
	return "Puzzle [puzzleNum=" + puzzleNum + ", secretKey=" + secretKey + ", puzzle=" + Arrays.toString(puzzle) + "]";
}


	public static void main(String[] args) {
		
        KeyGenerator keygenerator = null;
		try {
			keygenerator = KeyGenerator.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SecretKey desKey = keygenerator.generateKey();

//        //TEST 1
		Puzzle myPuzzle = new Puzzle(4000, desKey);
		byte[] myPuzzleArray = myPuzzle.getPuzzleAsBytes();
		System.out.println(myPuzzleArray.length);
	
//		//TEST 2
		System.out.println("puzzle numbet array should be");
	      for (byte value : myPuzzleArray) {
		         System.out.println(value);
		      }
	      
	     
//			System.out.println(sKey);
//			System.out.println("Plaintext array should be 0");
//		      for (byte value : puzzleNumArray) {
//		         System.out.print(value);
//		      }
//		   
//		      System.out.println("puzzle numbet array should be");
//		      for (byte value : puzzleNumArray) {
//			         System.out.print(value);
//			      }
//			for (byte value : sKeyArray) {
//		         System.out.print(" v " + value);
//		      }
//			System.out.println(plaintextArray.length);
//			System.out.println(puzzleNumArray.length);
			//System.out.println("key length " + sKeyArray.length);

	}
	
}
