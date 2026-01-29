package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.KeyEvent;
import model.Direction;
import model.Location;
import view.InventoryView;
import view.MainScene;
import view.ShopInventoryView;
import view.Buildings.BuildingView;
import view.Buildings.ShopView;

public class InputController {

	private MainController controller;
	private MainScene scene;
	
	private BooleanProperty gameLoaded = new SimpleBooleanProperty(false);
	private BooleanProperty pauseMenuOpen = new SimpleBooleanProperty(false);
	private BooleanProperty inGameMenuOpen = new SimpleBooleanProperty(false);
	private BooleanProperty playerInDialog = new SimpleBooleanProperty(false);
	private BooleanProperty inventoryOpen = new SimpleBooleanProperty(false);
	private BooleanProperty inBuilding = new SimpleBooleanProperty(false);
	
	public InputController(MainController controller) {
		this.controller = controller;
	}
	
	public void handleKeyPressed(KeyEvent e) {
		switch (e.getCode()) {
		case T:
			// TODO Remove
			if (gameLoaded.get())
				controller.teleportPlayer(new Location(5100, 3500));
			break;
		case E:
			if (gameLoaded.get() && allIsClosed()) {
				controller.playerInteract();
			}
			break;
		case ESCAPE:
			if (gameLoaded.get()) {
				scene.handlePauseMenu();
			}
			break;
		case I:
			if (gameLoaded.get() && !pauseMenuOpen.get() && !playerInDialog.get() && !inventoryOpen.get()) {
				scene.handleInGameMenu();
			}
			break;
		case Q:
			if (gameLoaded.get() && !pauseMenuOpen.get() && inventoryOpen.get()) {
				InventoryView inventoryView = scene.getInventoryView();
				inventoryView.dropSelectedItem();
			}
			break;
		case B:
			BuildingView buildingView = scene.getBuildingView();
			if (gameLoaded.get() && !pauseMenuOpen.get() && inventoryOpen.get() && inBuilding.get()) {
				try {
					ShopView shopView = (ShopView) buildingView;
					ShopInventoryView shopInventory = shopView.getInventoryView();
					shopInventory.buyItem();
				} catch(Exception ex) {}
			}
			break;
		case F11:
			scene.handleFullScreen();
			break;
		default:
			if (gameLoaded.get() && allIsClosed()) {
				handleMovementPressed(e);
			}
		}
	}

	public void handleMovementPressed(KeyEvent e) {
		switch (e.getCode()) {
		case UP:
		case W:
			controller.getUpPressed().set(true);
			break;
		case DOWN:
		case S:
			controller.getDownPressed().set(true);
			break;
		case RIGHT:
		case D:
			controller.getRightPressed().set(true);
			break;
		case LEFT:
		case A:
			controller.getLeftPressed().set(true);
			break;
		default:
			System.out.println("Input not valid");
		}
	}

	public void handleMovementReleased(KeyEvent e) {
		if (gameLoaded.get() && allIsClosed()) {
			switch (e.getCode()) {
			case UP:
			case W:
				controller.setMovingDirection(Direction.SOUTH);
				controller.getUpPressed().set(false);
				break;
			case DOWN:
			case S:
				controller.setMovingDirection(Direction.NORTH);
				controller.getDownPressed().set(false);
				break;
			case RIGHT:
			case D:
				controller.setMovingDirection(Direction.WEST);
				controller.getRightPressed().set(false);
				break;
			case LEFT:
			case A:
				controller.setMovingDirection(Direction.EAST);
				controller.getLeftPressed().set(false);
				break;
			default:
			}
		}
	}

	public void setAllKeyPressesFalse() {
		controller.getUpPressed().set(false);
		controller.getDownPressed().set(false);
		controller.getRightPressed().set(false);
		controller.getLeftPressed().set(false);
	}

	public boolean allIsClosed() {
		return !pauseMenuOpen.get() && !inGameMenuOpen.get() && !playerInDialog.get() && !inventoryOpen.get();
	}

	public void setScene(MainScene scene) {
		this.scene = scene;
		
		setBooleans();
	}
	
	private void setBooleans() {
		gameLoaded = scene.gameHasLoaded();
		pauseMenuOpen = scene.pauseMenuIsOpen();
		inGameMenuOpen = scene.inGameMenuIsOpen();
		playerInDialog = scene.playerIsInDialog();
		inventoryOpen = scene.inventoryIsOpen();
		inBuilding = scene.isInBuilding();
	}
	
}
