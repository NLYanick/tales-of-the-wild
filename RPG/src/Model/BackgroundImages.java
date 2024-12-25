package Model;

import java.util.HashMap;

public class BackgroundImages {

	private static final String PATH = "Images/Background/";
	private static final String GRASSPATH = "Images/Background/Grass/";
	private static final String WATERPATH = "Images/Background/Water/";
	
	private HashMap<String, String> images;
	
	public BackgroundImages() {
		images = new HashMap<String, String>();
		setUpImages();
	}
	
	public String getImageUrl(String key) {
		return images.get(key);
	}

	private void setUpImages() {
		
		images.put("0", WATERPATH.concat("Water.png"));
		images.put("1", WATERPATH.concat("WaterGrassTopBottom.png"));
		images.put("2", WATERPATH.concat("WaterGrassTopBottom.png SOUTH"));
		images.put("3", WATERPATH.concat("WaterGrassSides.png"));
		images.put("4", WATERPATH.concat("WaterGrassSides.png SOUTH"));
		images.put("5", WATERPATH.concat("WaterGrassCorner.png"));
		images.put("6", WATERPATH.concat("WaterGrassCorner.png SOUTH"));
		images.put("7", WATERPATH.concat("WaterGrassCornerReversed.png"));
		images.put("8", WATERPATH.concat("WaterGrassCornerReversed.png SOUTH"));
		images.put("9", WATERPATH.concat("WaterGrassSidesAndTopBottom.png"));
		images.put("10", WATERPATH.concat("WaterGrassSidesAndTopBottom.png SOUTH"));
		images.put("11", WATERPATH.concat("WaterGrassSidesAndTopBottomReversed.png"));
		images.put("12", WATERPATH.concat("WaterGrassSidesAndTopBottomReversed.png SOUTH"));
		images.put("13", WATERPATH.concat("StoneBridge.png"));
		images.put("14", WATERPATH.concat("StoneBridgeRailing.png"));
		images.put("15", WATERPATH.concat("StoneBridgeRailing.png SOUTH"));
		images.put("16", WATERPATH.concat("StoneBridgeRailingSide.png"));
		images.put("17", WATERPATH.concat("StoneBridgeRailingSide.png SOUTH"));
		images.put("18", WATERPATH.concat("WoodenBridge.png"));
		images.put("19", WATERPATH.concat("WoodenBridge.png EAST"));
		images.put("20", WATERPATH.concat("WoodenBridgeRailing.png"));
		images.put("21", WATERPATH.concat("WoodenBridgeRailing.png EAST"));
		images.put("22", WATERPATH.concat("WoodenBridgeRailing.png SOUTH"));
		images.put("23", WATERPATH.concat("WoodenBridgeRailing.png WEST"));
		images.put("24", WATERPATH.concat("WoodenBridgeRailingWater.png"));
		images.put("25", WATERPATH.concat("WoodenBridgeRailingWater.png SOUTH"));
		images.put("26", WATERPATH.concat("WoodenBridgeRailingWaterSide.png"));
		images.put("27", WATERPATH.concat("WoodenBridgeRailingWaterSide.png SOUTH"));
		
		images.put("28", GRASSPATH.concat("Grass.png"));
		images.put("29", GRASSPATH.concat("PathUp.png"));
		images.put("30", GRASSPATH.concat("PathSide.png"));
		images.put("31", GRASSPATH.concat("PathCross.png"));
		images.put("32", GRASSPATH.concat("PathTUp.png"));
		images.put("33", GRASSPATH.concat("PathTLeft.png"));
		images.put("34", GRASSPATH.concat("PathTDown.png"));
		images.put("35", GRASSPATH.concat("PathTRight.png"));
		images.put("36", GRASSPATH.concat("PathUpEndUp.png"));
		images.put("37", GRASSPATH.concat("PathUpEndDown.png"));
		images.put("38", GRASSPATH.concat("PathSideEndLeft.png"));
		images.put("39", GRASSPATH.concat("PathSideEndRight.png"));
		
		images.put("40", PATH.concat("BlueTent.png"));
		
	}
	
}
