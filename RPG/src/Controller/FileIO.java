package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import View.Background;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileIO {

	public final static String BACKGROUNDFILEPATH = "./Resource/textfiles/background.txt";
	
	private FileChooser filechooser;
	private Background background;
	
	private int layer = 0;
	
	public FileIO() {
		setUpFileChooser();
		background = new Background();
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
				layer++;
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
		int xLocation = i - line.length/2;
		switch(line[i]) {
		case "0":
			background.placeBackground(xLocation, layer, "Images/Background/Grass.png");
			break;
		case "1":
			background.placeBackground(xLocation, layer, "Images/Background/Water.png");
			break;
		case "2":
			break;
		case "3":
			break;
		case "4":
			break;
		}
	}

	public Background getBackground() {
		return background;
	}
	
	
}
