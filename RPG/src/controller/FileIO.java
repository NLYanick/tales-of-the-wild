package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import model.BackgroundImages;
import model.Direction;
import model.Image;
import model.Location;
import view.Background;

public class FileIO {

	public final static String BACKGROUNDFILEPATH = "./Resource/textfiles/background.txt";
	
	private Background background;
	private BackgroundImages backgroundImages;
	
	private int imagesUp;
	private int layer;
	private int imagesToLeft;
	
	private ArrayList<Image> imagesInFile;
	
	public FileIO() {
		background = new Background();
		backgroundImages = new BackgroundImages();
		imagesInFile = new ArrayList<Image>();
	}
	
	public void readText(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			int longestLine = 0;
			String[] line = br.readLine().split(" ");
			setImagesUp(line);
			line = br.readLine().split(" ");
			while(line != null){
				for (int i = 0; i < line.length; i++) {
					loadBackground(line, i);
				}
				if(longestLine < line.length) {
					longestLine = line.length;
				}
				layer++;
				String readLine = br.readLine();
				if(readLine == null) {
					break;
				}
				line = readLine.split(" ");
			}
			br.close();
			background.setSize(longestLine, layer + imagesUp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void setImagesUp(String[] line) {
		for(int i = 0; i < line.length; i++) {
			if(line[i].equals("imagesUp:")) {
				i++;
				imagesUp = Integer.parseInt(line[i]);
				layer = -imagesUp;
			} else if(line[i].equals("imagesToLeft:")) {
				i++;
				imagesToLeft = Integer.parseInt(line[i]);
			}
		}
	}

	private void loadBackground(String[] line, int i) {
		if(!line[i].equals("-1")) {
			int xLocation = i - imagesToLeft;
			Image bgImage = makeNewImage(backgroundImages.getImage(Integer.parseInt(line[i])));
			String[] imageUrl = bgImage.getUrl().split(" ");
			
			if(imageUrl.length > 1) {
				background.placeBackground(xLocation, layer, imageUrl[0], Direction.valueOf(imageUrl[1]));
			} else {
				background.placeBackground(xLocation, layer, imageUrl[0]);
			}
			
			setImageLocation(bgImage, xLocation);
			imagesInFile.add(bgImage);
		}
	}
	
	private Image makeNewImage(Image image) {
		Image newImage = new Image(image.getUrl(), image.canWalkOn());
		newImage.setLocation(image.getLocation());
		return newImage;
	}
	
	private void setImageLocation(Image image, int xLocation) {
		int standardImageSize = 128;
		int y = standardImageSize * layer;
		int x = standardImageSize * xLocation;

		image.setLocation(new Location(x, y));
	}
	
	public String getImageUrlByIndex(int index) {
		return backgroundImages.getImage(index).getUrl();
	}
	
	public HashMap<Integer, Image> getAllBackgroundImages() {
		return backgroundImages.getAllImages();
	}
	
	public ArrayList<Image> getImagesInFile() {
		return imagesInFile;
	}

	public Background getBackground() {
		return background;
	}
	
	
}
