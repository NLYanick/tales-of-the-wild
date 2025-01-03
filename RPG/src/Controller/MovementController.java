package Controller;

import Model.Direction;
import Model.Player;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class MovementController {

	private MainController controller;
	
	private BooleanProperty upPressed = new SimpleBooleanProperty();
	private BooleanProperty leftPressed = new SimpleBooleanProperty();
    private BooleanProperty downPressed = new SimpleBooleanProperty();
    private BooleanProperty rightPressed = new SimpleBooleanProperty();
    private BooleanBinding keyPressed = upPressed.or(leftPressed).or(downPressed).or(rightPressed);
	
	private Player player;
	
	public MovementController(MainController controller, Player player) {
		this.controller = controller;
		this.player = player;
		
		addKeyPressedListener();
	}
	
	private AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long timestamp) {
			
			if(leftPressed.get()){
				controller.moveBackground(Direction.EAST);
				controller.setMovingDirection(Direction.EAST);
				if(!player.getURL().equals("Images/Fox/FoxLeftRunning.gif") && !(upPressed.get() || downPressed.get())) {
					controller.setPlayerImage(Direction.EAST);
				}
			}
			if(rightPressed.get()){
				controller.moveBackground(Direction.WEST);
				controller.setMovingDirection(Direction.WEST);
				if(!player.getURL().equals("Images/Fox/FoxRightRunning.gif") && !(upPressed.get() || downPressed.get())) {
					controller.setPlayerImage(Direction.WEST);
				}
			}
			if(upPressed.get()) {
				controller.moveBackground(Direction.SOUTH);
				controller.setMovingDirection(Direction.SOUTH);
				if(!player.getURL().equals("Images/Fox/FoxBackRunning.gif")) {
					controller.setPlayerImage(Direction.SOUTH);
				}
			}
			if(downPressed.get()){
				controller.moveBackground(Direction.NORTH);
				controller.setMovingDirection(Direction.NORTH);
				if(!player.getURL().equals("Images/Fox/FoxRunning.gif")) {
					controller.setPlayerImage(Direction.NORTH);
				}
			}
		}
	};
	
	private void addKeyPressedListener() {
		 keyPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
				 timer.start();
			 } else {	                
				 controller.setPlayerStandingStillAnimation(controller.getMovingDirection());
				 timer.stop();
			 }
		 }));
		 
		 upPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
				 controller.setPlayerImage(Direction.SOUTH);
			 } 
		 }));
		 downPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
				 controller.setPlayerImage(Direction.NORTH);
			 } 
		 }));
		 rightPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
				 controller.setPlayerImage(Direction.WEST);
			 } 
		 }));
		 leftPressed.addListener(((observableValue, isPressed, t1) -> {
			 if(!isPressed){
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
	
}
