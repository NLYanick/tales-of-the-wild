package Controller;

import Model.BackgroundLocation;
import Model.Direction;
import Model.Player;
import View.MainScene;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class MainController {

	private BackgroundLocation backgroundLocation;
	private Player player;
	
	private MainScene scene;
	
	private Direction movingDirection = Direction.SOUTH;
	
	private BooleanProperty upPressed = new SimpleBooleanProperty();
	private BooleanProperty leftPressed = new SimpleBooleanProperty();
    private BooleanProperty downPressed = new SimpleBooleanProperty();
    private BooleanProperty rightPressed = new SimpleBooleanProperty();
    private BooleanBinding keyPressed = upPressed.or(leftPressed).or(downPressed).or(rightPressed);
	
	public MainController() {
		
		player = new Player("Images/FoxStandingStill.gif");
		backgroundLocation = new BackgroundLocation(0, 0);
		
		scene = new MainScene(this);
		addKeyPressedListener();
		
	}
	
	public void moveBackground(Direction dir) {
		backgroundLocation.move(dir);
		scene.moveBackground(backgroundLocation.getX(), backgroundLocation.getY());
		
		player.move(dir);
	}
	
	public void setMovingDirection(Direction dir) {
		movingDirection = dir;
	}
	
	public void setPlayerStandingStillAnimation(Direction dir) {
		switch(dir) {
		case NORTH:
			player.setImageURL("Images/FoxStandingStill.gif");
			break;
		case EAST:
			player.setImageURL("Images/FoxLeftStandingStill.gif");
			break;
		case SOUTH:
			player.setImageURL("Images/FoxBackStandingStill.gif");
			break;
		case WEST:
			player.setImageURL("Images/FoxRightStandingStill.gif");
			break;
		default:
			break;
		}
		scene.changePlayerImage();
	}
	
	public void setPlayerImage(Direction dir) {
		switch(dir) {
		case NORTH:
			player.setImageURL("Images/FoxRunning.gif");
			break;
		case EAST:
			player.setImageURL("Images/FoxLeftRunning.gif");
			break;
		case SOUTH:
			player.setImageURL("Images/FoxBackRunning.gif");
			break;
		case WEST:
			player.setImageURL("Images/FoxRightRunning.gif");
			break;
		default:
			break;
		}
		scene.changePlayerImage();
	}
	
	// -------------------------- Listeners --------------------------- //
	
	private AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long timestamp) {
			
			if(leftPressed.get()){
				moveBackground(Direction.EAST);
				if(!player.getURL().equals("Images/FoxLeftRunning.gif") && !(upPressed.get() || downPressed.get())) {
					setPlayerImage(Direction.EAST);
				}
			}
			if(rightPressed.get()){
				moveBackground(Direction.WEST);
				if(!player.getURL().equals("Images/FoxRightRunning.gif") && !(upPressed.get() || downPressed.get())) {
					setPlayerImage(Direction.WEST);
				}
			}
			if(upPressed.get()) {
				moveBackground(Direction.SOUTH);
				if(!player.getURL().equals("Images/FoxBackRunning.gif")) {
					setPlayerImage(Direction.SOUTH);
				}

			}
			if(downPressed.get()){
				moveBackground(Direction.NORTH);
				if(!player.getURL().equals("Images/FoxRunning.gif")) {
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
	
}
