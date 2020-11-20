package merklePuzzle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Puzzle{
	int puzzleNum;
	SecretKey sKey;
	byte[] puzzle;
	
/*
 * A constructor that takes a puzzle number as an int and a secret key as a SecretKey object and then constructs a Puzzle object.
 */

	public Puzzle(Integer puzzleNum, SecretKey sKey){
		this.puzzleNum = puzzleNum; 
		this.sKey = sKey;
		
		
		System.out.println(sKey);
		byte[] plaintextArray = new byte[16];
		byte[] puzzleNumArray = new byte[2];
		byte[] sKeyArray = new byte[8];
		
		Arrays.fill(plaintextArray, (byte) 0);
		puzzleNumArray = intToBytes(puzzleNum);
		
		System.out.println("Plaintext array should be 0");
	
	      for (byte value : plaintextArray) {
	         System.out.print(value);
	      }
	   
	      System.out.println("puzzle numbet array should be");
	      for (byte value : puzzleNumArray) {
		         System.out.print(value);
		      }
	      
	      SecretKey key = null;
		try {
			key = KeyGenerator.getInstance("DES").generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
		sKeyArray=key.getEncoded();
	      

	      ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
	      try {
			outputStream.write( plaintextArray );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      try {
			outputStream.write( puzzleNumArray );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      try {
				outputStream.write( sKeyArray );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      

	      byte puzzle[] = outputStream.toByteArray( );
	      
		this.puzzle = puzzle;
	}
	
	
	private static byte[] intToBytes(int n) 
    {
            byte[] bytes = new byte[4];
            for(int i = 0; i < 4; i++)
            {
                    bytes[i] = (byte) (n | 0);
                    n >>= 8;
            }
            return bytes;
    }
	
	
/*
 * A method getPuzzleNumber that returns the puzzles number as an int.
 */
	public int getPuzzeNumber(){
		return this.puzzleNum;
	}
	
	
/*
 * A method getKey that returns the puzzles key as a SecretKey.
 */
	public SecretKey getKey(){
		return this.sKey;
	}
	

/*
 *  A method getPuzzleAsBytes that returns a byte array representing the puzzle.
 *  Each puzzle is a cryptogram whose plaintext starts out with 128 zero bits (16-bytes), 
 *  followed by a 16-bit (2-byte) puzzle number in the range 1 to 4096, 
 *  and then a 64-bit (8-byte) key.
 */
	public byte[] getPuzzleAsBytes(){		
		return this.puzzle;
		
	}
	
	public static void main(String[] args) {
		byte[] message = "Hello World".getBytes();

        KeyGenerator keygenerator = null;
		try {
			keygenerator = KeyGenerator.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SecretKey desKey = keygenerator.generateKey();
		
		Puzzle myPuzzle = new Puzzle(12, desKey);
	}
	
}
