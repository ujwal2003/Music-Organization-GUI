package muiscStorage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * A class to create, read and edit XML files
 * @author Ujwal
 */
public class XMLHandler {
	private File fileName;
	private FileOutputStream fos;
	private FileInputStream fis;
	private XMLEncoder encoder;
	private XMLDecoder decoder;
	private Object obj;
	
	/**
	 * constructor that initializes the file and objects
	 * @param f File Object
	 * @param o Object to put in XML File
	 */
	public XMLHandler(File f, Object o){
		fileName = f;
		obj = o;
	}
	
	/**
	 * Creates the XML file with the name and object specified in the constructor
	 * @throws IOException
	 */
	public void createXMLFile() throws IOException {
		fos = new FileOutputStream(fileName);
		encoder = new XMLEncoder(fos);
		encoder.close();
		fos.close();
	}
	
	/**
	 * Gets the object string in the XML file
	 * @return XML object string
	 */
	public String getXMLObjectString() {
		String className = obj.getClass().toString().replaceAll("class ", "");
		return "<object class=\""+ className + "\">";
	}
	
	/**
	 * Writes one or multiple objects to the XML file
	 * @param o Object to be written to XML file
	 * @throws IOException
	 */
	public void writeXMLFile(Object ...o) throws IOException {
		fos = new FileOutputStream(fileName);
		encoder = new XMLEncoder(fos);
		Object[] t = o;
		for(int i=0; i<t.length; i++) {
			encoder.writeObject(t[i]);
		}
		encoder.close();
		fos.close();
	}
	
	/**
	 * Writes all objects from an ArrayList to the XML file
	 * @param <T> 
	 * @param o ArrayList of any type that contains objects
	 * @throws IOException
	 */
	public <T> void writeXMLFile(ArrayList<T> o) throws IOException {
		fos = new FileOutputStream(fileName);
		encoder = new XMLEncoder(fos);
		for(int i=0; i<o.size(); i++) {
			encoder.writeObject(o.get(i));
		}
		encoder.close();
		fos.close();
	}
	
	/**
	 * Gets all objects from XML file and stores them in an ArrayList
	 * @param <T>
	 * @return ArrayList of specified type
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> readXMLFile() throws IOException {
		Scanner sc = new Scanner(fileName);
		ArrayList<T> ar = new ArrayList<T>();
		
		fis = new FileInputStream(fileName);
		decoder = new XMLDecoder(fis);
		
		while(sc.hasNext()) {
			if(sc.nextLine().contains(this.getXMLObjectString())) {
				ar.add((T)decoder.readObject());
			}
		}
		sc.close();
		
		decoder.close();
		fis.close();
		
		return ar;
	}
}