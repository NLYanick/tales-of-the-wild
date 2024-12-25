package Model;

import java.util.HashMap;

public class BackgroundImages {

	private static final String PATH = "Images/Background/";
	private HashMap<String, String> images;
	
	public BackgroundImages() {
		images = new HashMap<String, String>();
		setUpImages();
	}
	
	public String getImageUrl(String key) {
		return images.get(key);
	}

	private void setUpImages() {
		
		images.put("0", PATH.concat("Grass.png"));
		images.put("1", PATH.concat("Water.png"));
		images.put("2", PATH.concat("WaterGrassTopBottom.png"));
		images.put("3", PATH.concat("WaterGrassTopBottom.png SOUTH"));
		images.put("4", PATH.concat("WaterGrassSides.png"));
		images.put("5", PATH.concat("WaterGrassSides.png SOUTH"));
		images.put("6", PATH.concat("WaterGrassCorner.png"));
		images.put("7", PATH.concat("WaterGrassCorner.png SOUTH"));
		images.put("8", PATH.concat("WaterGrassCornerReversed.png"));
		images.put("9", PATH.concat("WaterGrassCornerReversed.png SOUTH"));
		images.put("10", PATH.concat("WaterGrassSidesAndTopBottom.png"));
		images.put("11", PATH.concat("WaterGrassSidesAndTopBottom.png SOUTH"));
		images.put("12", PATH.concat("WaterGrassSidesAndTopBottomReversed.png"));
		images.put("13", PATH.concat("WaterGrassSidesAndTopBottomReversed.png SOUTH"));
		images.put("14", PATH.concat("StoneBridge.png"));
		images.put("15", PATH.concat("StoneBridgeRailing.png"));
		images.put("16", PATH.concat("StoneBridgeRailing.png SOUTH"));
		images.put("17", PATH.concat("StoneBridgeRailingSide.png"));
		images.put("18", PATH.concat("StoneBridgeRailingSide.png SOUTH"));
		images.put("19", PATH.concat("Grass.png"));
		images.put("20", PATH.concat("Grass.png"));
		images.put("21", PATH.concat("Grass.png"));
		images.put("22", PATH.concat("Grass.png"));
		images.put("23", PATH.concat("Grass.png"));
		images.put("24", PATH.concat("Grass.png"));
		images.put("25", PATH.concat("Grass.png"));
		
	}
	
}
