package io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Class encapsulates the contents of a file used during the compilation of a C program.
 * 
 * Could be anything, but typically either a .c or a .h file.
 * 
 * @author troy
 */
public class CFile {

	private String filename;
	private StringBuffer rawContents;
	
	public CFile(String filename) throws FileNotFoundException {
		this.filename = filename;
		
		readFile();
	}
	
	private void readFile() throws FileNotFoundException {		
        BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
		} catch (UnsupportedEncodingException e) {
			System.out.println("Incorrect coding. Aborting.");
			System.exit(1);
		} catch (FileNotFoundException e) {
			throw e;
		}

        StringBuffer text = new StringBuffer();
        String tmp;
        
        try {
			while ((tmp = in.readLine()) != null) {
			    text.append(tmp);
			    text.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        
        try {
			in.close();
		} catch (IOException e) {
			//can't really do anything here
		}

        rawContents = text;
	}
	
	public String getFileName() {
		return filename;
	}
	
	public StringBuffer getContents() {
		return rawContents;
	}
}
