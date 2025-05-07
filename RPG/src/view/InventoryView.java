package view;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Location;

public class InventoryView extends BorderPane {
	
	private final static int GRIDWIDTH = 10;
	private final static int GRIDHEIGHT = 6;
	
	private MainScene scene;
	private GridPane inventorySlots;
	
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
		inventorySlots = new GridPane();
		setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, null, null)));
		
		BorderPane leftPane = getLeftPane();
		setLeft(leftPane);
		
		BorderPane rightPane = getRightPane();
		setRight(rightPane);
		
		setUpInventorySlots();
		setCenter(inventorySlots);
	}
	
	private BorderPane getLeftPane() {
		int width = 300;
		
		BorderPane leftPane = new BorderPane();
		leftPane.setMinWidth(width);
		leftPane.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, null, null)));
		
		close = getButton("Close");
		close.setOnAction(e -> scene.removeInventoryView());
		leftPane.setCenter(close);
		
		return leftPane;
	}
	
	private BorderPane getRightPane() {
		int width = 300;
		
		BorderPane rightPane = new BorderPane();
		rightPane.setMinWidth(width);
		rightPane.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, null, null)));
		
		dropAll = getButton("Drop Items");
		dropAll.setOnAction(e -> dropAllSelectedItems());
		rightPane.setCenter(dropAll);
		
		return rightPane;
	}
	
	private void setUpInventorySlots() {
		
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
		inventorySlots.getStyleClass().add("inventory-plate");
		
		inventorySlots.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
		inventorySlots.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
		inventorySlots.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		
		BorderPane.setAlignment(inventorySlots, Pos.CENTER);
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
