import java.util.*;
import java.io.*;

public class ValuesEdit{
	
	private final int START_POINTER = 0;
	private final int STRING_MAX_LENGTH = 256;
	private final int ALLOCATED_STRLEN = 2;
	private final int TOTAL_BYTE_SIZE = STRING_MAX_LENGTH + ALLOCATED_STRLEN;
	private final int HEADER_SIZE = 8;
	private RandomAccessFile data;
	private long numRecords;

	public ValuesEdit(String fileName) throws IOException{ 
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

	public void insertEntry(String entry) throws IOException{
		this.data.seek(START_POINTER);
		this.data.writeLong(++numRecords); 
		//<DEBUG> System.out.println(entry);
		byte[] word = entry.getBytes("UTF8");
		this.data.seek(HEADER_SIZE + (numRecords-1)*(TOTAL_BYTE_SIZE));
		this.data.writeShort(entry.length()); //writes the length of the entry into the file
		this.data.write(word);
	}

	public void updateEntry(int point, String entry) throws IOException{
		byte[] word = entry.getBytes("UTF8");
		this.data.seek(HEADER_SIZE+point*(TOTAL_BYTE_SIZE));
		this.data.writeShort(entry.length()); //writes the length of the entry into the file
		this.data.write(word); //writes the byte[] word to the values file
	}

	public String readEntry(int point) throws IOException{
		this.data.seek(HEADER_SIZE+point*(TOTAL_BYTE_SIZE)); 
		int size = this.data.readShort();
		byte[] word = new byte[size];
		this.data.read(word); //reads the byre[] word
		String output = new String(word, "UTF8");
		return output;
	}
}