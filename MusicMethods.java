package muiscStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;



/*
 * Plays requested music and gets music info
 */

import org.python.util.PythonInterpreter;

public class MusicMethods {
	
	private String path;
	
	public MusicMethods() {
		
	}
	
	public MusicMethods(String p) {
		path = p;
	}
	
	//plays music file
	public boolean playMusic(String fileName) {
		try(PythonInterpreter py = new PythonInterpreter()){
			 Properties props = new Properties();
		        props.put("python.home", "C:\\Users\\Ujwal\\AppData\\Local\\Microsoft\\WindowsApps\\PythonSoftwareFoundation.Python.3.7_qbz5n2kfra8p0\\python.exe");
		        props.put("python.console.encoding", "UTF-8");
		        props.put("python.security.respectJavaAccessibility", "false");
		        props.put("python.import.site", "false");
		        Properties preprops = System.getProperties();
		        PythonInterpreter.initialize(preprops, props, new String[0]);
			
			py.exec("import webbrowser");
			py.exec("webbrowser.open(" + "'"+path+fileName+"'" + ")");
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//returns a String of the file path where music files are stored
	public static String getMusicPath() throws FileNotFoundException {
		Scanner sc = new Scanner(new File("settings.TXT"));
		String mainPath = null;
		mainPath = sc.nextLine();
		sc.close();
		return mainPath;
	}
	
	//creates xml file
	private static void generateXMLFile() throws IOException {
		XMLHandler x = new XMLHandler(new File(MusicMethods.getMusicPath()+"musicInfo.xml"), new MusicInfo());
		x.createXMLFile();
		x.writeXMLFile(new MusicInfo("SongName", "AuthorName", "none"));
	}
	
	//checks if xml file should be created
	public static boolean checkForRegen() throws IOException {
		Scanner sc = new Scanner(new File("settings.txt"));
		sc.nextLine();
		String check = sc.nextLine();
		if(check.equals("regen=true")) {
			sc.close();
			generateXMLFile();
			return true;
		}
		sc.close();
		//System.out.println(check);
		return false;
	}
	
	//returns data of song by searching for song name
	public static String[] searchForSongDataFromName(ArrayList<MusicInfo> arr, String name) {
		String[] data = new String[3];
		for(int i=0; i<arr.size(); i++) {
			if(arr.get(i).getSongName().contains(name)) {
				data[0] = arr.get(i).getSongName();
				data[1] = arr.get(i).getAuthor();
				data[2] = arr.get(i).getDescr();
				
				return data;
			}
		}
		
		data[0] = null;
		data[1] = null;
		data[2] = null;
		
		return data;
	}
	
	//returns index of a given music object from the name of the song
	public static int returnIndexFromName(ArrayList<MusicInfo> arr, String name) {
		for(int i=0; i<arr.size(); i++) {
			if(arr.get(i).getSongName().contains(name)) {
				return i;
			}
		}
		
		return -1;
	}
	
}