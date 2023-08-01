package muiscStorage;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("serial")
public class MusicStorageMain extends JPanel {
	
	//components
    private static JButton playButton;
    private static JButton addButton;
    private static JButton removeButton;
    private static JButton editButton;
    private static JButton pathButton;
    private static JLabel drpdwnLab;
    private static JTextField authLab;
    private static JTextArea descrLab;
    private static JLabel pathLab;
    private static JComboBox<String> songOptions;
    private static JScrollPane scrollPane;
    
 	public MusicStorageMain() {
		//construct components
		playButton = new JButton("Play Audio");
		addButton = new JButton("Add Audio");
		editButton = new JButton("Edit Song Info");
		removeButton = new JButton("Remove Audio");
		pathButton = new JButton("Path Settings");
		drpdwnLab = new JLabel("Selected Audio File: ");
		authLab = new JTextField("Author: [author]");
		descrLab = new JTextArea("Description Info: [descr]");
		pathLab = new JLabel("Path: [path]");
		songOptions = new JComboBox<String>();
		scrollPane = new JScrollPane(descrLab);
		
		//size and layout
        setPreferredSize (new Dimension (414, 421));
        setLayout (null);

        //add components
        add (playButton);
        add (addButton);
        add (removeButton);
        add (songOptions);
        add (drpdwnLab);
        add (editButton);
        add (authLab);
        //add (descrLab);
        add(scrollPane);
        add (pathButton);
        add (pathLab);
        
        //set component bounds (only needed by Absolute Positioning)
        playButton.setBounds (25, 55, 365, 45);
        addButton.setBounds (145, 125, 120, 50);
        removeButton.setBounds (270, 125, 120, 50);
        songOptions.setBounds (125, 10, 265, 30);
        drpdwnLab.setBounds (5, 10, 120, 30);
        editButton.setBounds (25, 125, 115, 50);
        authLab.setBounds (135, 185, 180, 50);
        //descrLab.setBounds (35, 235, 190, 35);
        scrollPane.setBounds (35, 235, 190, 35);
        pathButton.setBounds (60, 325, 300, 30);
        pathLab.setBounds (35, 375, 350, 25);
	}
	
	public static void main(String[] args) throws IOException {
		//create GUI
		JFrame win = new JFrame("Music Storage Player");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.getContentPane().add(new MusicStorageMain());
		win.setResizable(false);
		
		//Read XML file and put contents in arraylist
		MusicMethods.checkForRegen();
		XMLHandler x = new XMLHandler(new File(MusicMethods.getMusicPath()+"musicInfo.xml"), new MusicInfo());
		ArrayList<MusicInfo> fileContent = x.readXMLFile();
		sort(fileContent);
		
		//make textfields look like labels
		authLab.setEditable(false);
		authLab.setBackground(null);
		authLab.setBorder(null);
		authLab.setFont(authLab.getFont().deriveFont(Font.BOLD));
		descrLab.setEditable(false);
		descrLab.setBorder(null);
		scrollPane.setSize(350, 80);
		descrLab.setLineWrap(true);
		descrLab.setWrapStyleWord(true);
		descrLab.setFont(authLab.getFont().deriveFont(Font.BOLD));
		
		//put dong names in dropdown
		for(int i=0; i<fileContent.size(); i++) {
			songOptions.addItem(fileContent.get(i).getSongName());
		}
		//disable play, edit and remove buttons if first option is selected
		if(String.valueOf(songOptions.getSelectedItem()).contains("[choose audio file]")) {
			playButton.setEnabled(false);
			editButton.setEnabled(false);
			removeButton.setEnabled(false);
		} else {
			playButton.setEnabled(true);
			editButton.setEnabled(true);
			removeButton.setEnabled(true);
		}
		
		//edit audio info code
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = MusicMethods.returnIndexFromName(fileContent, String.valueOf(songOptions.getSelectedItem()));
				String dat[] = MusicMethods.searchForSongDataFromName(fileContent, String.valueOf(songOptions.getSelectedItem()));
				
				String songName = null;
				String songAuthor = null;
				String descrInfo = null;
				
				JTextField name = new JTextField(dat[0]);
				JTextField author = new JTextField(dat[1]);
				JTextField crInfo = new JTextField(dat[2]);
				
				Object[] fields = {
						"Audio File Name:", name,
						"Author:", author,
						"Description/Copyright Info:", crInfo
				};
				
				int n = JOptionPane.showConfirmDialog(null, fields, "Input Info", JOptionPane.OK_CANCEL_OPTION);
				
				if(n == JOptionPane.OK_OPTION) {
					songName = name.getText();
					songAuthor = author.getText();
					descrInfo = crInfo.getText();
					if(index != -1) {
						fileContent.get(index).setSongName(songName);
						fileContent.get(index).setAuthor(songAuthor);
						fileContent.get(index).setDescr(descrInfo);
						
						try {
							x.writeXMLFile(fileContent);
							sort(fileContent);
						} catch (IOException e1) {
							//e1.printStackTrace();
							StringWriter sw = new StringWriter();
				            e1.printStackTrace(new PrintWriter(sw));
				            String eString = sw.toString();
							JOptionPane.showMessageDialog(win,
								    "Cannot access Directory!\n"+eString,
								    "File Chooser Exception",
								    JOptionPane.ERROR_MESSAGE);
						}
						
						songOptions.removeAllItems();
						for(int i=0; i<fileContent.size(); i++) {
							songOptions.addItem(fileContent.get(i).getSongName());
						}
					} else {
						JOptionPane.showMessageDialog(win,
							    "Failed to find data for file",
							    "Data Acess Failure",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		//add audio code
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String songName = null;
				String songAuthor = null;
				String descrInfo = null;
				
				JTextField name = new JTextField("[name]");
				JTextField author = new JTextField("[author]");
				JTextField crInfo = new JTextField("[descr]");
				
				Object[] fields = {
						"Audio File Name:", name,
						"Author:", author,
						"Description/Copyright Info:", crInfo
				};
				
				int n = JOptionPane.showConfirmDialog(null, fields, "Input Info", JOptionPane.OK_CANCEL_OPTION);
				
				if(n == JOptionPane.OK_OPTION) {
					songName = name.getText();
					songAuthor = author.getText();
					descrInfo = crInfo.getText();
					
					fileContent.add(new MusicInfo(songName, songAuthor, descrInfo));
					try {
						x.writeXMLFile(fileContent);
						sort(fileContent);
					} catch (IOException e1) {
						//e1.printStackTrace();
						StringWriter sw = new StringWriter();
			            e1.printStackTrace(new PrintWriter(sw));
			            String eString = sw.toString();
						JOptionPane.showMessageDialog(win,
							    "Cannot access Directory!\n"+eString,
							    "File Chooser Exception",
							    JOptionPane.ERROR_MESSAGE);
					}
					songOptions.removeAllItems();
					for(int i=0; i<fileContent.size(); i++) {
						songOptions.addItem(fileContent.get(i).getSongName());
					}
				}
			}
		});
		
		//remove audio code
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = MusicMethods.returnIndexFromName(fileContent, String.valueOf(songOptions.getSelectedItem()));
				if(index != -1) {
					fileContent.remove(index);
					try {
						x.writeXMLFile(fileContent);
						sort(fileContent);
					} catch (IOException e1) {
						//e1.printStackTrace();
						StringWriter sw = new StringWriter();
			            e1.printStackTrace(new PrintWriter(sw));
			            String eString = sw.toString();
						JOptionPane.showMessageDialog(win,
							    "Cannot access Directory!\n"+eString,
							    "File Chooser Exception",
							    JOptionPane.ERROR_MESSAGE);
					}
					songOptions.removeAllItems();
					for(int i=0; i<fileContent.size(); i++) {
						songOptions.addItem(fileContent.get(i).getSongName());
					}
				} else {
					JOptionPane.showMessageDialog(win,
						    "Failed to find data for file",
						    "Data Acess Failure",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//play audio code
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String p = pathLab.getText();
				p = p.replaceAll("Path: ", "");
				MusicMethods m = new MusicMethods(p);
				String fName = String.valueOf(songOptions.getSelectedItem());
				boolean played = m.playMusic(fName);
				
				if(played == false) {
					JOptionPane.showMessageDialog(win,
						    "Cannot Play File!",
						    "File Not Exception",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//path button code
		pathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Scanner sc = new Scanner(new File("settings.TXT"));
					String path = sc.nextLine();
					sc.close();
					
					JTextField pText = new JTextField(path);
					Object[] fields = {
						"Path: ", pText	
					};
					
					int n = JOptionPane.showConfirmDialog(null, fields, "Path Settings", JOptionPane.OK_CANCEL_OPTION);
					if(n == JOptionPane.OK_OPTION) {
						String newPath = pText.getText();
						FileWriter fWrite = new FileWriter(new File("settings.TXT"));
						BufferedWriter bWrite = new BufferedWriter(fWrite);
						bWrite.write(newPath+"\nregen=false");
						bWrite.close();
						fWrite.close();
					}
					
				} catch (IOException e1) {
					//e1.printStackTrace();
					StringWriter sw = new StringWriter();
		            e1.printStackTrace(new PrintWriter(sw));
		            String eString = sw.toString();
					JOptionPane.showMessageDialog(win,
						    "Cannot Fine or Acess Directory!\n"+eString,
						    "Input/Output Exception",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//add song info to author and descr labels; initially on first item in dropdown, and change them according to the selected item
		String initDat[] = MusicMethods.searchForSongDataFromName(fileContent, String.valueOf(songOptions.getSelectedItem()));
		authLab.setText("Author:  "+initDat[1]);
		descrLab.setText("Description Info:  "+initDat[2]);
		songOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dat[] = MusicMethods.searchForSongDataFromName(fileContent, String.valueOf(songOptions.getSelectedItem()));
				authLab.setText("Author:  "+dat[1]);
				descrLab.setText("Description Info:  "+dat[2]);
				
				if(String.valueOf(songOptions.getSelectedItem()).contains("[choose audio file]")) {
					playButton.setEnabled(false);
					editButton.setEnabled(false);
					removeButton.setEnabled(false);
				} else {
					playButton.setEnabled(true);
					editButton.setEnabled(true);
					removeButton.setEnabled(true);
				}
				
			}
		});

		pathLab.setText("Path: " + MusicMethods.getMusicPath());
		
		//make GUI visible
		win.pack();
		win.setVisible(true);
	}
	
    public static void sort(ArrayList<MusicInfo> x) {
        MusicInfo temp;
        for (int i = 0; i < x.size() - 1; i++) {
            for (int j = i + 1; j < x.size(); j++) {
                if (x.get(i).getSongName().compareToIgnoreCase(x.get(j).getSongName()) > 0) {
                    temp = x.get(i);
                    x.set(i, x.get(j));
                    x.set(j, temp); 
                }
            }
        }
    }

}