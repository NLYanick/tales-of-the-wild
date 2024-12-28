package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Model.BackgroundImages;
import Model.Direction;
import View.Background;

public class FileIO {

	public final static String BACKGROUNDFILEPATH = "./Resource/textfiles/background.txt";
	
	private Background background;
	private BackgroundImages backgroundImages;
	
	private int layer = 0;
	
	public FileIO() {;
		background = new Background();
		backgroundImages = new BackgroundImages();
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
		if(!line[i].equals("-1")) {
			int xLocation = i;
//			int xLocation = i - line.length/2;
			String[] imageUrl = backgroundImages.getImageUrl(line[i]).split(" ");
			if(imageUrl.length > 1) {
				Direction direction = Direction.valueOf(imageUrl[1]);
				background.placeBackground(xLocation, layer, imageUrl[0], direction);
			} else {
				background.placeBackground(xLocation, layer, imageUrl[0]);
			}
		}
	}

	public Background getBackground() {
		return background;
	}
	
	
}
