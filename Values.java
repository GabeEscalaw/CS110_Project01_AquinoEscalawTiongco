import java.util.*;
import java.io.*;

/**
* <h1> Values </h1>
*
* Java class that contains all the value strings of the database
* @author Mico Aquino, Elise Gabriel Escalaw, John Eugene Tiongco
* @1.0
* @since November 28, 2017
*/

public class Values{
	
	private final int START_POINTER = 0;
	private final int STRING_MAX_LENGTH = 256;
	private final int ALLOCATED_STRLEN = 2;
	private final int TOTAL_BYTE_SIZE = STRING_MAX_LENGTH + ALLOCATED_STRLEN;
	private final int HEADER_SIZE = 8;
	private RandomAccessFile data;
	private long numRecords;
	
	/**
	* Constructor for Values which initializes the actual file 
	* Checks the initial file's byte and writes the number of existing records
	* 
	* @param fileName	name of the file
	* @throws IOException
	* @see IOException
	*/
	
	public Values(String fileName) throws IOException{ 
		File file = new File(fileName); //makes the actual file for the values
		if(!file.exists()){  //checks the initial byte
			this.numRecords = 0;
			this.data = new RandomAccessFile("Data.values","rwd");	
			this.data.seek(START_POINTER);
			this.data.writeLong(this.numRecords); //writes the number of existing records
		} 
		else{
			this.data = new RandomAccessFile(file, "rwd");
			this.data.seek(START_POINTER);
			this.numRecords = this.data.readLong(); //reads the initial bytes 
		}
	}
	
	/**
	* insertEntry seeks the initial pointer and increments the number of records 
	* it also writes the length of the entry into the file by accessing getBytes
	* 
	* @param entry	byte that will be read
	*/	

	public void insertEntry(String entry) throws IOException{
		this.data.seek(START_POINTER);
		this.data.writeLong(++numRecords); 
		//<DEBUG> System.out.println(entry);
		byte[] word = entry.getBytes("UTF8");
		this.data.seek(HEADER_SIZE + (numRecords-1)*(TOTAL_BYTE_SIZE));
		this.data.writeShort(entry.length()); //writes the length of the entry into the file
		this.data.write(word);
	}

	/**
	* updateEntry writes the updated length and word for the output file
	* 
	* @param point	pointer for locating the the characters that were encoded into 8 bytes (UTF8)
	* @param entry	byte that will be read
	*/
	
	public void updateEntry(int point, String entry) throws IOException{
		byte[] word = entry.getBytes("UTF8");
		this.data.seek(HEADER_SIZE+point*(TOTAL_BYTE_SIZE));
		this.data.writeShort(entry.length()); //writes the length of the entry into the file
		this.data.write(word); //writes the byte[] word to the values file
	}

	/**
	* readEntry reads the word that is being pointed to 
	* 
	* @param point	pointer for locating the the characters that were encoded into 8 bytes (UTF8)
	*/
	
	public String readEntry(int point) throws IOException{
		this.data.seek(HEADER_SIZE+point*(TOTAL_BYTE_SIZE)); 
		int size = this.data.readShort();
		byte[] word = new byte[size];
		this.data.read(word); //reads the byre[] word
		String output = new String(word, "UTF8");
		return output;
	}
}
