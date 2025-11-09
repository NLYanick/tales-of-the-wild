package model;

import java.util.HashMap;

public class BackgroundImages {

	@SuppressWarnings("unused")
	private static final String PATH = "Images/Background/";
	private static final String GRASSPATH = "Images/Background/Grass/";
	private static final String WATERPATH = "Images/Background/Water/";
	private static final String BUILDINGPATH = "Images/Background/Building/";
	
	public static final int MAXNUMBER = 58;
	
	private HashMap<Integer, Tile> tiles;
	
	public BackgroundImages() {
		tiles = new HashMap<Integer, Tile>();
		setUpTiles();
	}
	
	public Tile getTile(int key) {
		return tiles.get(key);
	}

	private void setUpTiles() {
		
		tiles.put(0, new Tile(WATERPATH.concat("Water.png"), false));
		tiles.put(1, new Tile(WATERPATH.concat("WaterGrassTopBottom.png"), false));
		tiles.put(2, new Tile(WATERPATH.concat("WaterGrassTopBottom.png SOUTH"), false));
		tiles.put(3, new Tile(WATERPATH.concat("WaterGrassSides.png"), false));
		tiles.put(4, new Tile(WATERPATH.concat("WaterGrassSides.png SOUTH"), false));
		tiles.put(5, new Tile(WATERPATH.concat("WaterGrassCorner.png"), false));
		tiles.put(6, new Tile(WATERPATH.concat("WaterGrassCorner.png SOUTH"), false));
		tiles.put(7, new Tile(WATERPATH.concat("WaterGrassCornerReversed.png"), false));
		tiles.put(8, new Tile(WATERPATH.concat("WaterGrassCornerReversed.png SOUTH"), false));
		tiles.put(9, new Tile(WATERPATH.concat("WaterGrassSidesAndTopBottom.png"), false));
		tiles.put(10, new Tile(WATERPATH.concat("WaterGrassSidesAndTopBottom.png SOUTH"), false));
		tiles.put(11, new Tile(WATERPATH.concat("WaterGrassSidesAndTopBottomReversed.png"), false));
		tiles.put(12, new Tile(WATERPATH.concat("WaterGrassSidesAndTopBottomReversed.png SOUTH"), false));
		tiles.put(13, new Tile(WATERPATH.concat("StoneBridge.png"), true));
		tiles.put(14, new Tile(WATERPATH.concat("StoneBridgeRailing.png"), false));
		tiles.put(15, new Tile(WATERPATH.concat("StoneBridgeRailing.png SOUTH"), false));
		tiles.put(16, new Tile(WATERPATH.concat("StoneBridgeRailingSide.png"), false));
		tiles.put(17, new Tile(WATERPATH.concat("StoneBridgeRailingSide.png SOUTH"), false));
		tiles.put(18, new Tile(WATERPATH.concat("WoodenBridge.png"), true));
		tiles.put(19, new Tile(WATERPATH.concat("WoodenBridge.png EAST"), true));
		tiles.put(20, new Tile(WATERPATH.concat("WoodenBridgeRailing.png"), false));
		tiles.put(21, new Tile(WATERPATH.concat("WoodenBridgeRailing.png EAST"), false));
		tiles.put(22, new Tile(WATERPATH.concat("WoodenBridgeRailing.png SOUTH"), false));
		tiles.put(23, new Tile(WATERPATH.concat("WoodenBridgeRailing.png WEST"), false));
		tiles.put(24, new Tile(WATERPATH.concat("WoodenBridgeRailingWater.png"), false));
		tiles.put(25, new Tile(WATERPATH.concat("WoodenBridgeRailingWater.png SOUTH"), false));
		tiles.put(26, new Tile(WATERPATH.concat("WoodenBridgeRailingWaterSide.png"), false));
		tiles.put(27, new Tile(WATERPATH.concat("WoodenBridgeRailingWaterSide.png SOUTH"), false));
		
		tiles.put(28, new Tile(GRASSPATH.concat("Grass.png"), true));
		tiles.put(29, new Tile(GRASSPATH.concat("PathUp.png"), true));
		tiles.put(30, new Tile(GRASSPATH.concat("PathSide.png"), true));
		tiles.put(31, new Tile(GRASSPATH.concat("PathCross.png"), true));
		tiles.put(32, new Tile(GRASSPATH.concat("PathTUp.png"), true));
		tiles.put(33, new Tile(GRASSPATH.concat("PathTLeft.png"), true));
		tiles.put(34, new Tile(GRASSPATH.concat("PathTDown.png"), true));
		tiles.put(35, new Tile(GRASSPATH.concat("PathTRight.png"), true));
		tiles.put(36, new Tile(GRASSPATH.concat("PathUpEndUp.png"), true));
		tiles.put(37, new Tile(GRASSPATH.concat("PathUpEndDown.png"), true));
		tiles.put(38, new Tile(GRASSPATH.concat("PathSideEndLeft.png"), true));
		tiles.put(39, new Tile(GRASSPATH.concat("PathSideEndRight.png"), true));
		tiles.put(40, new Tile(GRASSPATH.concat("OakTree.png"), false));
		tiles.put(41, new Tile(GRASSPATH.concat("PurpleFlower.png"), true));
		tiles.put(42, new Tile(GRASSPATH.concat("RedFlowerBatch.png"), true));
		tiles.put(43, new Tile(GRASSPATH.concat("ConcretePath.png"), true));
		tiles.put(44, new Tile(GRASSPATH.concat("ConcretePath.png EAST"), true));
		tiles.put(45, new Tile(GRASSPATH.concat("ConcretePathEnd.png"), true));
		tiles.put(46, new Tile(GRASSPATH.concat("ConcretePathEnd.png EAST"), true));
		tiles.put(47, new Tile(GRASSPATH.concat("ConcretePathEnd.png SOUTH"), true));
		tiles.put(48, new Tile(GRASSPATH.concat("ConcretePathEnd.png WEST"), true));
		tiles.put(49, new Tile(GRASSPATH.concat("ConcretePathCrossing.png"), true));
		tiles.put(50, new Tile(GRASSPATH.concat("ConcretePathT.png"), true));
		tiles.put(51, new Tile(GRASSPATH.concat("ConcretePathT.png EAST"), true));
		tiles.put(52, new Tile(GRASSPATH.concat("ConcretePathT.png SOUTH"), true));
		tiles.put(53, new Tile(GRASSPATH.concat("ConcretePathT.png WEST"), true));
		
		tiles.put(54, new Tile(BUILDINGPATH.concat("BlueTent.png"), false));
		tiles.put(55, new Tile(BUILDINGPATH.concat("BrickHouse.png"), false));
		tiles.put(56, new Tile(BUILDINGPATH.concat("Bricks.png"), false));
		tiles.put(57, new Tile(BUILDINGPATH.concat("ShopFloor.png"), false));
		tiles.put(58, new Tile(BUILDINGPATH.concat("ShopFloor2.png"), false));
		tiles.put(59, new Tile(BUILDINGPATH.concat("ShopFloor3.png"), false));
		tiles.put(60, new Tile(BUILDINGPATH.concat("WhiteShop.png"), false));
		
	}
	
	public HashMap<Integer, Tile> getAllTiles() {
		return tiles;
	}
	
}
