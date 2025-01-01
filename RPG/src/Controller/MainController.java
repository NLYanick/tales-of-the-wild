package Controller;

import java.util.ArrayList;
import java.util.HashMap;

import Model.BackgroundLocation;
import Model.Direction;
import Model.Location;
import Model.NPC;
import Model.Player;
import View.Background;
import View.MainScene;
import View.NPCView;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class MainController {

	private BackgroundLocation backgroundLocation;
	private Player player;
	
	private ApplicationController appController;
	private MainScene scene;
	private FileIO fileIO;
	
	private ArrayList<NPC> npcs;
	private HashMap<NPC, NPCView> npcsWithViews;
	
	private Direction movingDirection = Direction.SOUTH;
	
	private BooleanProperty upPressed = new SimpleBooleanProperty();
	private BooleanProperty leftPressed = new SimpleBooleanProperty();
    private BooleanProperty downPressed = new SimpleBooleanProperty();
    private BooleanProperty rightPressed = new SimpleBooleanProperty();
    private BooleanBinding keyPressed = upPressed.or(leftPressed).or(downPressed).or(rightPressed);
	
	@SuppressWarnings("static-access")
	public MainController(ApplicationController appController, FileIO fileIO) {
		
		this.fileIO = fileIO;
		scene = new MainScene(this);
		
		player = new Player("Images/Fox/FoxStandingStill.gif", new Location(scene.SCENEWIDTH/2, scene.SCENEHEIGHT/2));
		backgroundLocation = new BackgroundLocation(0, 0);
		
		this.appController = appController;
		
		addKeyPressedListener();
		
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
		backgroundLocation.move(dir);
		scene.moveBackground(backgroundLocation.getX(), backgroundLocation.getY(), appController.isFullScreen());
		moveNPCs(dir);
		
		player.move(Direction.getOpposite(dir));
	}
	
	public void setMovingDirection(Direction dir) {
		movingDirection = dir;
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
	
	public void setNPCViewLocation(NPC npc) {
		
		moveNPCViewWithScreen(npc);
		
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
	private void moveNPCViewWithScreen(NPC npc) {
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
		int multiplier = 12;
		
		if((player.getX() >= npc.getX() - movingDirection.getX() * multiplier) 
				&& (player.getX() <= npc.getX() + movingDirection.getX() * multiplier)) {
			return true;
		}
		if((player.getY() >= npc.getY() - movingDirection.getY() * multiplier) 
				&& (player.getY() <= npc.getY() + movingDirection.getY() * multiplier)) {
			return true;
		}
		
		return false;
	}
	
	// -------------------------- Listeners --------------------------- //
	
	private AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long timestamp) {
			
			if(leftPressed.get()){
				moveBackground(Direction.EAST);
				if(!player.getURL().equals("Images/Fox/FoxLeftRunning.gif") && !(upPressed.get() || downPressed.get())) {
					setPlayerImage(Direction.EAST);
				}
			}
			if(rightPressed.get()){
				moveBackground(Direction.WEST);
				if(!player.getURL().equals("Images/Fox/FoxRightRunning.gif") && !(upPressed.get() || downPressed.get())) {
					setPlayerImage(Direction.WEST);
				}
			}
			if(upPressed.get()) {
				moveBackground(Direction.SOUTH);
				if(!player.getURL().equals("Images/Fox/FoxBackRunning.gif")) {
					setPlayerImage(Direction.SOUTH);
				}

			}
			if(downPressed.get()){
				moveBackground(Direction.NORTH);
				if(!player.getURL().equals("Images/Fox/FoxRunning.gif")) {
					setPlayerImage(Direction.NORTH);
				}
			}
		}
	};
	
	private void addKeyPressedListener() {
		 keyPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
            	timer.start();
			 } else {	                
            	setPlayerStandingStillAnimation(movingDirection);
        		timer.stop();
			 }
		 }));
		 
		 upPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
				 setPlayerImage(Direction.SOUTH);
			 } 
		 }));
		 downPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
				 setPlayerImage(Direction.NORTH);
			 } 
		 }));
		 rightPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
				 setPlayerImage(Direction.WEST);
			 } 
		 }));
		 leftPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
				 setPlayerImage(Direction.EAST);
			 } 
		 }));
	}
	
	public BooleanProperty getUpPressed() {
		return upPressed;
	}
	
	public BooleanProperty getLeftPressed() {
		return leftPressed;
	}
	
	public BooleanProperty getDownPressed() {
		return downPressed;
	}
	
	public BooleanProperty getRightPressed() {
		return rightPressed;
	}
	
	// --------------------
	
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
	
}
