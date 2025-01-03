package Controller;

import java.util.ArrayList;
import java.util.HashMap;

import Model.BackgroundLocation;
import Model.Direction;
import Model.Image;
import Model.Location;
import Model.NPC;
import Model.Player;
import View.Background;
import View.MainScene;
import View.NPCView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;

public class MainController {

	private BackgroundLocation backgroundLocation;
	private Player player;
	
	private ApplicationController appController;
	private MovementController movementController;
	
	private MainScene scene;
	private FileIO fileIO;
	
	private ArrayList<NPC> npcs;
	private HashMap<NPC, NPCView> npcsWithViews;
	
	private Direction movingDirection = Direction.SOUTH;
	
	@SuppressWarnings("static-access")
	public MainController(ApplicationController appController, FileIO fileIO) {
		
		this.fileIO = fileIO;
		scene = new MainScene(this);
		
		player = new Player("Images/Fox/FoxStandingStill.gif", new Location(scene.SCENEWIDTH/2, scene.SCENEHEIGHT/2));
		backgroundLocation = new BackgroundLocation(0, 0);
		
		this.appController = appController;
		movementController = new MovementController(this, player);
		
	}
	
	public void setUpNPCs() {
		npcs = new ArrayList<NPC>();
		npcsWithViews = new HashMap<NPC, NPCView>();
		
		NPC tempNPC = new NPC("Images/NPCs/TempCharacter.png", new Location(500, 450), Direction.WEST, this, "TempCharacter");
		npcs.add(tempNPC);
		NPC tempNPCTwo = new NPC("Images/NPCs/TempCharacter.png", new Location(1450, 1700), Direction.WEST, this, "TempCharacter");
		npcs.add(tempNPCTwo);
		
		for(NPC npc : npcs) {
			NPCView npcView = new NPCView(npc.getURL(), npc.getStartLocation().getX(), npc.getStartLocation().getY());
			npcView.fixImage();
			npc.setViewLocation(new Location((int) npcView.getLayoutX(), (int) npcView.getLayoutY()));
			
			scene.addNPCView(npcView);
			npcsWithViews.put(npc, npcView);
			npc.setUpThread();
		}
		
	}
	
	public void moveBackground(Direction dir) {
		if(playerCanWalk()) {
			backgroundLocation.move(dir);
			scene.moveBackground(backgroundLocation.getX(), backgroundLocation.getY(), appController.isFullScreen());
			moveNPCs(dir);
			
			player.move(Direction.getOpposite(dir));
		}
	}
	
	private boolean playerCanWalk() {
		ArrayList<Image> backgroundImages = fileIO.getImagesInFile();
		
		for(Image img : backgroundImages) {
			if(nextStepIsOnImage(img) && img.canWalkOn()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean nextStepIsOnImage(Image img) {
		int extraSpace = 127;
		Direction opposite = Direction.getOpposite(movingDirection);
		int nextStepX = player.getX() + opposite.getX();
		int nextStepY = player.getY() + opposite.getY();
		if(img.getY() + extraSpace == 127) {
			System.out.println(player.getY());
			System.out.println(nextStepY);
			System.out.println(img.getY() + extraSpace);
			System.out.println();
		}
		return (nextStepX >= img.getX()
				&& nextStepY >= img.getY())
				&& (nextStepX <= img.getX() + extraSpace
				&& nextStepY <= img.getY() + extraSpace);
	}
	
	public void setPlayerStandingStillAnimation(Direction dir) {
		player.setStandingStillAnimation(dir);
		scene.changePlayerImage();
	}
	
	public void setPlayerImage(Direction dir) {
		player.setRunningImage(dir);
		scene.changePlayerImage();
	}
	
	public void stopNPCThreads() {
		if(npcs != null && !(npcs.size() <= 0)) {			
			for(NPC npc : npcs) {
				npc.setThreadRunning(false);
			}
		}
	}
	
	private void moveNPCs(Direction dir) {
		for(NPC npc : npcs) {
			npc.moveViewLocationWithBackground(dir);
			moveNPCViewWithScreen(npc);
		}
	}
	
	public void setFullScreen(boolean isFullScreen) {
		appController.setFullScreen(isFullScreen);
	}
	
	public void switchNPCImage(NPC npc, String url) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				scene.changeNPCImage(npcsWithViews.get(npc), url);
			}
		});
	}
	
	public void resizeBackgroundAndNPCLocation() {
		scene.moveBackground(backgroundLocation.getX(), backgroundLocation.getY(), appController.isFullScreen());
		if(npcs != null)
			resizeNPCLocation();
	}
	
	private void resizeNPCLocation() {
		for(NPC npc : npcs) {
			moveNPCViewWithScreen(npc);
		}
	}
	
	@SuppressWarnings("static-access")
	public void moveNPCViewWithScreen(NPC npc) {
		int screenXDiffernce = (int) scene.getWidth()/2 - scene.SCENEWIDTH/2;
		int screenYDiffernce = (int) scene.getHeight()/2 - scene.SCENEHEIGHT/2;
		
		NPCView npcView = npcsWithViews.get(npc);
		if(appController.isFullScreen()) {
			npcView.move(npc.getViewLocation().getX() + screenXDiffernce, npc.getViewLocation().getY() + screenYDiffernce);
		} else {
			npcView.move(npc.getViewLocation().getX(), npc.getViewLocation().getY());
		}
	}
	
	public void playerInteract() {
		NPC nearbyNPC = getNearbyNPC();
		if(nearbyNPC != null) {			
			player.talkToNPC(nearbyNPC);
		}
	}
	
	private NPC getNearbyNPC() {
		for(NPC npc : npcs) {
			if(hasNPCNearby(npc)) {
				return npc;
			}
		}
		return null;
	}

	private boolean hasNPCNearby(NPC npc) {
		
		Direction direction = getGoodNPCCheckDirection(movingDirection);
		Direction nextDirection = Direction.getNext(direction);

		if(Direction.isHorizontal(movingDirection)) {
			return hasNPCNearbyHorizontal(npc, direction, nextDirection);
		} else if(Direction.isVertical(movingDirection)) {
			return hasNPCNearbyVertical(npc, direction, nextDirection);
		}
		
		return false;
	}
	
	private Direction getGoodNPCCheckDirection(Direction dir) {
		if((dir.equals(Direction.NORTH) || dir.equals(Direction.WEST))) {
			dir = Direction.getOpposite(dir);
		}
		return dir;
	}
	
	private boolean hasNPCNearbyHorizontal(NPC npc, Direction direction, Direction nextDirection) {
		
		int multiplier = 8;
		boolean hasNearby = false;
		
		if(movingDirection == Direction.EAST) {
			if((player.getX() >= npc.getX()) && (player.getX() <= npc.getX() + direction.getX() * multiplier)
				&& playerYIsNearNPCY(npc, nextDirection, multiplier)) {
				hasNearby = true;
			}
		} else if(movingDirection == Direction.WEST) {
			if((player.getX() >= npc.getX() - direction.getX() * multiplier) && (player.getX() <= npc.getX())
				&& playerYIsNearNPCY(npc, nextDirection, multiplier)) {
				hasNearby = true;
			}
		}
		return hasNearby;
	}
	
	private boolean hasNPCNearbyVertical(NPC npc, Direction direction, Direction nextDirection) {
		
		int multiplier = 8;
		boolean hasNearby = false;
		
		if(movingDirection == Direction.NORTH) {
			if(((player.getY() >= npc.getY() - direction.getY() * multiplier) && (player.getY() <= npc.getY()))
					&& playerXIsNearNPCX(npc, nextDirection, multiplier)) {
				hasNearby = true;
			}
		} else if(movingDirection == Direction.SOUTH) {
			if((player.getY() >= npc.getY()) && (player.getY() <= npc.getY() + direction.getY() * multiplier)
				&& playerXIsNearNPCX(npc, nextDirection, multiplier)) {
				hasNearby = true;
			}
		}
		return hasNearby;
	}
	
	private boolean playerXIsNearNPCX(NPC npc, Direction nextDirection, int multiplier) {
		return (player.getX() >= npc.getX() + nextDirection.getX() * multiplier) 
		&& (player.getX() <= npc.getX() - nextDirection.getX() * multiplier);
	}
	
	private boolean playerYIsNearNPCY(NPC npc, Direction nextDirection, int multiplier) {
		return (player.getY() >= npc.getY() - nextDirection.getY() * multiplier) 
				&& (player.getY() <= npc.getY() + nextDirection.getY() * multiplier);
	}
	
	// -------------------- Getters & Setters --------------------
	
	public BooleanProperty getUpPressed() {
		return movementController.getUpPressed();
	}
	
	public BooleanProperty getLeftPressed() {
		return movementController.getLeftPressed();
	}
	
	public BooleanProperty getDownPressed() {
		return movementController.getDownPressed();
	}
	
	public BooleanProperty getRightPressed() {
		return movementController.getRightPressed();
	}
	
	public MainScene getMainScene() {
		return scene;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getPlayerURL() {
		return player.getURL();
	}
	
	public Background getBackground() {
		return fileIO.getBackground();
	}
	
	public FileIO getFileIO() {
		return fileIO;
	}
	
	public void setMovingDirection(Direction dir) {
		movingDirection = dir;
	}
	
	public Direction getMovingDirection() {
		return movingDirection;
	}
	
}
