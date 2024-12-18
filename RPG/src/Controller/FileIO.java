package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileIO {

	public final static String BACKGROUNDFILEPATH = "./Resource/textfiles/background.txt";
	
	private FileChooser filechooser;
	
	public FileIO() {
		setUpFileChooser();
	}
	
	private void setUpFileChooser() {
		filechooser = new FileChooser();
		filechooser.initialDirectoryProperty().set(new File("./Resource"));
		filechooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		filechooser.getExtensionFilters().add(new ExtensionFilter("Everything", "*"));
	}
	
	public void readText(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String[] line = br.readLine().split(" ");
			while(line != null){
				for (int i = 0; i < line.length; i++) {
					loadBackground(line, i);
				}
				String readLine = br.readLine();
				if(readLine == null) {
					break;
				}
				line = readLine.split(" ");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void loadBackground(String[] line, int i) {
		switch(line[i]) {
		case "0":
			break;
		}
	}
	
	
}
