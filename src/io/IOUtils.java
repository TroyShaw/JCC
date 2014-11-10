package io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class IOUtils {

	public static String readFile(String path) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			
			StringBuffer text = new StringBuffer();
			String tmp;
			while ((tmp = in.readLine()) != null) {
				text.append(tmp);
				text.append("\n");
			}
			
			
			in.close();
			
			return text.toString();
			
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO: fix this up. For now can't be bothered propagating a checked exception
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static StringBuffer readFileToStringBuffer(String path) {
		return new StringBuffer(readFile(path));
	}
	
	public static void saveFile(String contents, String path) {
		
	}

}
