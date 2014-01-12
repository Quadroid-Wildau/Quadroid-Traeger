package de.th_wildau.quadroid.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Properties extends java.util.Properties{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Properties instance = null;
	
	public static Properties getInstance() {
		if(instance == null) {
			instance = new Properties();
		}
		
		return instance;
	}
	
	private Properties() {
		super();
		
		try {
			this.load(new FileInputStream("config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
