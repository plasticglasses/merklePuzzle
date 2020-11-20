import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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
	public Puzzle(int puzzleNum, SecretKey sKey){
		this.puzzleNum = puzzleNum; 
		this.sKey = sKey;
		
		
//		System.out.println(sKey);
		byte[] plaintextArray = new byte[16];
		byte[] puzzleNumArray = null;
		byte[] sKeyArray = new byte[8];
		
		Arrays.fill(plaintextArray, (byte) 0);
		
		puzzleNumArray = ByteBuffer.allocate(2).putShort((short)puzzleNum).array(); 
	    
		
//		System.out.println("Plaintext array should be 0");
//	
//	      for (byte value : puzzleNumArray) {
//	         System.out.print(value);
//	      }
//	   
//	      System.out.println("puzzle numbet array should be");
//	      for (byte value : puzzleNumArray) {
//		         System.out.print(value);
//		      }
	      
	     
	     
		sKeyArray=sKey.getEncoded();
//		for (byte value : sKeyArray) {
//	         System.out.print(" v " + value);
//	      }
//		System.out.println(plaintextArray.length);
//		System.out.println(puzzleNumArray.length);
		//System.out.println("key length " + sKeyArray.length);
	      

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
//	
	public static void main(String[] args) {
		
		
//
        KeyGenerator keygenerator = null;
		try {
			keygenerator = KeyGenerator.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SecretKey desKey = keygenerator.generateKey();
//		
//        
//        //TEST 1
		Puzzle myPuzzle = new Puzzle(4000, desKey);
		byte[] myPuzzleArray = myPuzzle.getPuzzleAsBytes();
		System.out.println(myPuzzleArray.length);
//		
//		//TEST 2
		System.out.println("puzzle numbet array should be");
	      for (byte value : myPuzzleArray) {
		         System.out.println(value);
		      }
//		
	}
	
}
