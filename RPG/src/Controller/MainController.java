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
			NPCView NPCView = new NPCView(npc.getURL(), npc.getStartLocation().getX(), npc.getStartLocation().getY());
			scene.addNPCView(NPCView);
			npcsWithViews.put(npc, NPCView);
			npc.setUpThread(npc.getWalkDirection());
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
			npc.moveWithBackground(dir);
			NPCView npcView = npcsWithViews.get(npc);
			npcView.move(npc.getX(), npc.getY());
		}
	}
	
	public void setFullScreen(boolean isFullScreen) {
		appController.setFullScreen(isFullScreen);
	}
	
	public void setNPCViewLocation(NPC npc) {
		npcsWithViews.get(npc).move(npc.getX(), npc.getY());
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
		
		boolean isFullScreen = appController.isFullScreen();
		
		scene.moveBackground(backgroundLocation.getX(), backgroundLocation.getY(), isFullScreen);
		resizeNPCLocation(isFullScreen);
		
	}
	
	@SuppressWarnings("static-access")
	private void resizeNPCLocation(boolean isFullScreen) {
		
		int screenXDiffernce = (int) scene.getWidth()/2 - scene.SCENEWIDTH/2;
		int screenYDiffernce = (int) scene.getHeight()/2 - scene.SCENEHEIGHT/2;
		
		for(NPC npc : npcs) {
			NPCView npcView = npcsWithViews.get(npc);
			if(isFullScreen) {
				npcView.move(npc.getX() + screenXDiffernce, npc.getY() + screenYDiffernce);
			} else {
				npcView.move(npc.getX(), npc.getY());
			}
		}
	}
	
	public void playerInteract() {
		System.out.println(player.getX() + " " + player.getY());
		NPC nearbyNPC = getNearbyNPC();
		if(nearbyNPC != null) {			
			player.talkToNPC(nearbyNPC);
		}
	}
	
	private NPC getNearbyNPC() {
		for(NPC npc : npcs) {
			if(hasNPCInDirection(npc)) {
				return npc;
			}
		}
		return null;
	}

	private boolean hasNPCInDirection(NPC npc) {
		if((npc.getX() <= (player.getX() + movingDirection.getX() * 2) && npc.getX() <= (player.getX() - movingDirection.getX() * 2)) 
				|| (npc.getY() <= (player.getY() + movingDirection.getY() * 2) && npc.getY() <= (player.getY() - movingDirection.getY() * 2))) {
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
