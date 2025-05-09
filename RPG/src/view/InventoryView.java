package view;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Location;

public class InventoryView extends BorderPane {
	
	private final static int GRIDWIDTH = 10;
	private final static int GRIDHEIGHT = 6;
	
	private MainScene scene;
	private GridPane inventorySlots;
	private BorderPane infoBox;
	
	private Button close;
	private Button dropAll;
	
	private ArrayList<ItemView> itemViews;
	private int newX;
	private int newY;
		
	public InventoryView(MainScene scene) {
		this.scene = scene;
		
		setUpLayout();
	}
	
	private void setUpLayout() {
		setBackground(new Background(new BackgroundImage(new Image("Images/Background/Grass/Grass.png"), null, null, null, null)));
		
		BorderPane leftPane = getLeftPane();
		setLeft(leftPane);
				
		setUpInventorySlots();
		setUpInfoBox();
		HBox boxes = getInventoryBoxes();
		setCenter(boxes);
	}
	
	private BorderPane getLeftPane() {
		int width = 300;
		int spacing = 30;
		
		BorderPane leftPane = new BorderPane();
		leftPane.setMinWidth(width);
		leftPane.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, null, null)));
		
		close = getButton("Close");
		close.setOnAction(e -> scene.removeInventoryView());
		
		dropAll = getButton("Drop Items");
		dropAll.setOnAction(e -> dropAllSelectedItems());
		
		VBox buttonsPane = new VBox(close, dropAll);
		buttonsPane.setSpacing(spacing);
		buttonsPane.setAlignment(Pos.CENTER);
		
		leftPane.setCenter(buttonsPane);
		
		return leftPane;
	}
	
	private void setUpInventorySlots() {
		
		inventorySlots = new GridPane();
		
		int gapSize = 10;
		
		for(int x = 0; x < GRIDWIDTH; x++) {
			for(int y = 0; y < GRIDHEIGHT; y++) {
				InventorySlot slot = new InventorySlot(new Location(x, y));
				slot.setOnMouseClicked(e -> selectItem(e, slot));
				inventorySlots.add(slot, x, y);
			}
		}
		
		inventorySlots.setHgap(gapSize);
		inventorySlots.setVgap(gapSize);
		inventorySlots.getStyleClass().add("inventory-slots-box");
		
		inventorySlots.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
		inventorySlots.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
		inventorySlots.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);		
	}
	
	private void setUpInfoBox() {
		infoBox = new BorderPane();
		
		// TODO
		
		infoBox.getStyleClass().add("inventory-info-box");
	}
	
	private HBox getInventoryBoxes() {
		int spacing = 80;
		
		HBox boxes = new HBox(inventorySlots, infoBox);
		boxes.setSpacing(spacing);
		boxes.setAlignment(Pos.CENTER);
		
		return boxes;
	}
	
	private Button getButton(String text) {
		
		Button button = new Button(text);
		
		button.getStyleClass().add("menu-button");
		button.setOnKeyPressed(e -> handleButtonKeyPressed(e, button));
				
		return button;
	}
	
	private void handleButtonKeyPressed(KeyEvent e, Button button) {
		if(e.getCode().equals(KeyCode.ENTER) && button.isFocused()) {
			button.fire();
		}
	}
	
	private void remakeInventorySlots() {		
		for(Node node : inventorySlots.getChildren()) {
			InventorySlot slot = (InventorySlot) node;
			slot.removeItemView();
		}
		
		newX = 0; 
		newY = 0;
		addItemViewsToInventory();
	}
	
	public void requestFocusForButton() {
		close.requestFocus();
	}
	
	public void addItemView(ItemView itemView) {
		itemViews.add(itemView);
		addItemViewToInventory(itemView);
	}
	
	public void addItemViewToInventory(ItemView itemView) {
		if(newY >= GRIDHEIGHT && newX >= GRIDWIDTH) {
			return;
		}
		
		if(newX >= GRIDWIDTH) {
			newX = 0;
			newY++;
		}
		
		InventorySlot slot = getInventorySlotByLocation();
		slot.setItemView(itemView);
		
		newX++;
		
	}
	
	private void selectItem(MouseEvent e, InventorySlot slot) {
		slot.setSelected(!slot.isSelected());
	}
	
	private void dropAllSelectedItems() {
		for(Node node : inventorySlots.getChildren()) {
			InventorySlot slot = (InventorySlot) node;
			if(slot.isSelected() && slot.getItemView() != null) {
				dropItem(slot.getItemView());
			}
			slot.setSelected(false);
		}
		remakeInventorySlots();
	}
	
	private void dropItem(ItemView itemView) {
		scene.dropItem(itemView);
		itemViews.remove(itemView);
	}
	
	private InventorySlot getInventorySlotByLocation() {
		for(Node node : inventorySlots.getChildren()) {
			InventorySlot slot = (InventorySlot) node;
			if(slot.getX() == newX && slot.getY() == newY) {
				return slot;
			}
		}
		return null;
	}
	
	private InventorySlot getInventorySlotWithItemView(ItemView itemView) {
		for(Node node : inventorySlots.getChildren()) {
			InventorySlot slot = (InventorySlot) node;
			if(slot.getItemView() == itemView) {
				return slot;
			}
		}
		return null;
	}
	
	public void removeItemView(ItemView itemView) {
		getInventorySlotWithItemView(itemView).removeItemView();
		
		newX--;
		if(newX < 0) {
			newX = GRIDWIDTH - 1;
			newY = newY <= 0 ? 0 : (newY - 1);
		}
	}
	
	public void setItemViews(ArrayList<ItemView> itemViews) {
		this.itemViews = itemViews;
		
		addItemViewsToInventory();
	}
	
	private void addItemViewsToInventory() {
		if(itemViews == null || itemViews.size() == 0) {
			return;
		}
		for(int i = 0; i < itemViews.size(); i++) { // Not foreach because ConcurrentModificationException
			addItemViewToInventory(itemViews.get(i));
		}
	}
	
}
