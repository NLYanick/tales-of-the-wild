package controller;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import model.Direction;
import model.Player;

public class MovementController {

	private MainController controller;
	
	private BooleanProperty upPressed = new SimpleBooleanProperty();
	private BooleanProperty leftPressed = new SimpleBooleanProperty();
    private BooleanProperty downPressed = new SimpleBooleanProperty();
    private BooleanProperty rightPressed = new SimpleBooleanProperty();
    private BooleanBinding keyPressed = upPressed.or(leftPressed).or(downPressed).or(rightPressed);
	
	private Player player;
	
	public MovementController(MainController controller) {
		this.controller = controller;
		
		addKeyPressedListener();
	}
	
	private AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long timestamp) {
			
			if(leftPressed.get()){
				controller.setMovingDirection(Direction.EAST);
				controller.moveBackgroundAndPlayer(Direction.EAST);
				if(!player.getURL().equals("Images/Fox/FoxLeftRunning.gif") && !(upPressed.get() || downPressed.get())) {
					controller.setPlayerImage(Direction.EAST);
				}
			}
			if(rightPressed.get()){
				controller.setMovingDirection(Direction.WEST);
				controller.moveBackgroundAndPlayer(Direction.WEST);
				if(!player.getURL().equals("Images/Fox/FoxRightRunning.gif") && !(upPressed.get() || downPressed.get())) {
					controller.setPlayerImage(Direction.WEST);
				}
			}
			if(upPressed.get()) {
				controller.setMovingDirection(Direction.SOUTH);
				controller.moveBackgroundAndPlayer(Direction.SOUTH);
				if(!player.getURL().equals("Images/Fox/FoxBackRunning.gif")) {
					controller.setPlayerImage(Direction.SOUTH);
				}
			}
			if(downPressed.get()){
				controller.setMovingDirection(Direction.NORTH);
				controller.moveBackgroundAndPlayer(Direction.NORTH);
				if(!player.getURL().equals("Images/Fox/FoxRunning.gif")) {
					controller.setPlayerImage(Direction.NORTH);
				}
			}
		}
	};
	
	private void addKeyPressedListener() {
		 keyPressed.addListener(((observableValue, oldValue, isPressed) -> {
			 if(isPressed){
				 timer.start();
			 } else {	                
				 controller.setPlayerStandingStillAnimation(controller.getMovingDirection());
				 timer.stop();
			 }
		 }));
		 
		 upPressed.addListener(((observableValue, oldValue, isPressed) -> {
			 if(isPressed){
				 controller.setPlayerImage(Direction.SOUTH);
			 } 
		 }));
		 downPressed.addListener(((observableValue, oldValue, isPressed) -> {
			 if(isPressed){
				 controller.setPlayerImage(Direction.NORTH);
			 } 
		 }));
		 rightPressed.addListener(((observableValue, oldValue, isPressed) -> {
			 if(isPressed){
				 controller.setPlayerImage(Direction.WEST);
			 } 
		 }));
		 leftPressed.addListener(((observableValue, oldValue, isPressed) -> {
			 if(isPressed){
				 controller.setPlayerImage(Direction.EAST);
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
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
