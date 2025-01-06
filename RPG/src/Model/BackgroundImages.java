package Model;

import java.util.HashMap;

public class BackgroundImages {

	private static final String PATH = "Images/Background/";
	private static final String GRASSPATH = "Images/Background/Grass/";
	private static final String WATERPATH = "Images/Background/Water/";
	
	public static final int MAXNUMBER = 41;
	
	private HashMap<Integer, Image> images;
	
	public BackgroundImages() {
		images = new HashMap<Integer, Image>();
		setUpImages();
	}
	
	public Image getImage(int key) {
		return images.get(key);
	}

	private void setUpImages() {
		
		images.put(0, new Image(WATERPATH.concat("Water.png"), false));
		images.put(1, new Image(WATERPATH.concat("WaterGrassTopBottom.png"), false));
		images.put(2, new Image(WATERPATH.concat("WaterGrassTopBottom.png SOUTH"), false));
		images.put(3, new Image(WATERPATH.concat("WaterGrassSides.png"), false));
		images.put(4, new Image(WATERPATH.concat("WaterGrassSides.png SOUTH"), false));
		images.put(5, new Image(WATERPATH.concat("WaterGrassCorner.png"), false));
		images.put(6, new Image(WATERPATH.concat("WaterGrassCorner.png SOUTH"), false));
		images.put(7, new Image(WATERPATH.concat("WaterGrassCornerReversed.png"), false));
		images.put(8, new Image(WATERPATH.concat("WaterGrassCornerReversed.png SOUTH"), false));
		images.put(9, new Image(WATERPATH.concat("WaterGrassSidesAndTopBottom.png"), false));
		images.put(10, new Image(WATERPATH.concat("WaterGrassSidesAndTopBottom.png SOUTH"), false));
		images.put(11, new Image(WATERPATH.concat("WaterGrassSidesAndTopBottomReversed.png"), false));
		images.put(12, new Image(WATERPATH.concat("WaterGrassSidesAndTopBottomReversed.png SOUTH"), false));
		images.put(13, new Image(WATERPATH.concat("StoneBridge.png"), true));
		images.put(14, new Image(WATERPATH.concat("StoneBridgeRailing.png"), false));
		images.put(15, new Image(WATERPATH.concat("StoneBridgeRailing.png SOUTH"), false));
		images.put(16, new Image(WATERPATH.concat("StoneBridgeRailingSide.png"), false));
		images.put(17, new Image(WATERPATH.concat("StoneBridgeRailingSide.png SOUTH"), false));
		images.put(18, new Image(WATERPATH.concat("WoodenBridge.png"), true));
		images.put(19, new Image(WATERPATH.concat("WoodenBridge.png EAST"), true));
		images.put(20, new Image(WATERPATH.concat("WoodenBridgeRailing.png"), false));
		images.put(21, new Image(WATERPATH.concat("WoodenBridgeRailing.png EAST"), false));
		images.put(22, new Image(WATERPATH.concat("WoodenBridgeRailing.png SOUTH"), false));
		images.put(23, new Image(WATERPATH.concat("WoodenBridgeRailing.png WEST"), false));
		images.put(24, new Image(WATERPATH.concat("WoodenBridgeRailingWater.png"), false));
		images.put(25, new Image(WATERPATH.concat("WoodenBridgeRailingWater.png SOUTH"), false));
		images.put(26, new Image(WATERPATH.concat("WoodenBridgeRailingWaterSide.png"), false));
		images.put(27, new Image(WATERPATH.concat("WoodenBridgeRailingWaterSide.png SOUTH"), false));
		
		images.put(28, new Image(GRASSPATH.concat("Grass.png"), true));
		images.put(29, new Image(GRASSPATH.concat("PathUp.png"), true));
		images.put(30, new Image(GRASSPATH.concat("PathSide.png"), true));
		images.put(31, new Image(GRASSPATH.concat("PathCross.png"), true));
		images.put(32, new Image(GRASSPATH.concat("PathTUp.png"), true));
		images.put(33, new Image(GRASSPATH.concat("PathTLeft.png"), true));
		images.put(34, new Image(GRASSPATH.concat("PathTDown.png"), true));
		images.put(35, new Image(GRASSPATH.concat("PathTRight.png"), true));
		images.put(36, new Image(GRASSPATH.concat("PathUpEndUp.png"), true));
		images.put(37, new Image(GRASSPATH.concat("PathUpEndDown.png"), true));
		images.put(38, new Image(GRASSPATH.concat("PathSideEndLeft.png"), true));
		images.put(39, new Image(GRASSPATH.concat("PathSideEndRight.png"), true));
		images.put(40, new Image(GRASSPATH.concat("OakTree.png"), false));
		
		images.put(41, new Image(PATH.concat("BlueTent.png"), false));
		
	}
	
	public HashMap<Integer, Image> getAllImages() {
		return images;
	}
	
}
