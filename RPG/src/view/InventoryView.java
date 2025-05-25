package view;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Item;
import model.Location;

public class InventoryView extends BorderPane {
	
	private final static int GRIDWIDTH = 10;
	private final static int GRIDHEIGHT = 6;
	
	private MainScene scene;
	private GridPane inventorySlots;
	private BorderPane infoBox;
	
	private Button close;
	
	private StringProperty infoBoxText;
	
	private ArrayList<ItemView> itemViews;
	
	private ItemView draggedItemView;
		
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
		
		setOnKeyPressed(e -> handleKeyPressed(e));
	}
	
	private void handleKeyPressed(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ALT)) {
			switchAllSlotsTraversable();
		}
	}
	
	private void switchAllSlotsTraversable() {
		boolean isTraversable = inventorySlots.getChildren().get(0).isFocusTraversable();
		for(Node node : inventorySlots.getChildren()) {
			node.setFocusTraversable(!isTraversable);
		}
	}
	
	private BorderPane getLeftPane() {
		int spacing = 30;
		int rectWidth = 300;
		int rectHeight = 104;
		
		BorderPane leftPane = new BorderPane();
		leftPane.getStyleClass().add("inventory-buttons-pane");
		
		VBox titleBox = getTitleBox();
		leftPane.setTop(titleBox);
		
		close = getButton("Close");
		close.setOnAction(e -> scene.removeInventoryView());
		
		VBox buttonsPane = new VBox(close);
		buttonsPane.setSpacing(spacing);
		buttonsPane.setAlignment(Pos.CENTER);
		
		leftPane.setCenter(buttonsPane);
		
		Rectangle invisRect = new Rectangle(rectWidth, rectHeight);
		invisRect.setFill(Color.TRANSPARENT);
		leftPane.setBottom(invisRect);
		
		return leftPane;
	}
	
	private VBox getTitleBox() {
		int spacing = 60;
		
		Text title = new Text("Inventory");
		title.getStyleClass().add("inventory-title");
		
		VBox titleBox = new VBox(title);
		titleBox.setPadding(new Insets(spacing, 0, 0, 0));
		titleBox.setAlignment(Pos.TOP_CENTER);
		
		return titleBox;
	}
	
	private void setUpInventorySlots() {
		
		inventorySlots = new GridPane();
		
		int gapSize = 10;
		
		for(int x = 0; x < GRIDWIDTH; x++) {
			for(int y = 0; y < GRIDHEIGHT; y++) {
				InventorySlot slot = new InventorySlot(new Location(x, y));
				slot.setOnMouseClicked(e -> slot.requestFocus());
				slot.focusedProperty().addListener(((observableValue, oldValue, isFocused) -> {
					selectInventorySlot(slot);
				}));
				handleDropEvent(slot);
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
	
	private void handleDropEvent(InventorySlot slot) {
		slot.setOnDragDropped(e -> {
			InventorySlot otherSlot = getInventorySlotWithItemView(draggedItemView);
			BorderPane slotPane = (BorderPane) slot.getCenter();
			
			if(slotPane.getCenter() == null) {	
				otherSlot.removeItemView();
				slot.setItemView(draggedItemView);
				makeItemDraggable(draggedItemView);
				draggedItemView = null;
			}
		});
	}
	
	private void selectInventorySlot(InventorySlot slot) {
		ItemView itemView = slot.getItemView();
		Item item = scene.getItemFromView(itemView);
		infoBoxText.set(itemView != null ? item.getName() : "");
	}
	
	private void setUpInfoBox() {
		infoBox = new BorderPane();
		
		infoBoxText = new SimpleStringProperty();
		
		Text text = new Text();
		text.getStyleClass().add("inventory-info-box-title");
		text.textProperty().bind(infoBoxText);
		
		infoBox.setTop(text);
		BorderPane.setAlignment(text, Pos.TOP_LEFT);
		
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
	
	public void requestFocusForButton() {
		close.requestFocus();
	}
	
	public void addItemView(ItemView itemView) {
		itemViews.add(itemView);
		addItemViewToInventory(itemView);
	}
	
	public void addItemViewToInventory(ItemView itemView) {
		InventorySlot slot = getNextFreeInventorySlot();
		if(slot == null) {
			return;
		}
		slot.setItemView(itemView);
		makeItemDraggable(itemView);
	}
	
	private void makeItemDraggable(ItemView itemView) {
		itemView.setOnDragDetected(e -> {
			draggedItemView = itemView;
			
			Dragboard db = itemView.startDragAndDrop(TransferMode.ANY);
			
			ClipboardContent cbc = new ClipboardContent();
			cbc.putString("");
			
			db.setContent(cbc);
			
			e.consume();			
		});
	}
	
	public void dropSelectedItem() {
		for(Node node : inventorySlots.getChildren()) {
			InventorySlot slot = (InventorySlot) node;
			if(slot.isFocused() && slot.getItemView() != null) {
				dropItem(slot.getItemView());
			}
		}
	}
	
	private void dropItem(ItemView itemView) {
		scene.dropItem(itemView);
		itemViews.remove(itemView);
	}
	
	private InventorySlot getNextFreeInventorySlot() {
		for(int y = 0; y < GRIDHEIGHT; y++) {
			for(int x = 0; x < GRIDWIDTH; x++) {
				InventorySlot slot = getInventorySlotByLocation(x, y);
				if(slot.getItemView() == null) {
					return slot;
				}
			}
		}
		return null;
	}
	
	private InventorySlot getInventorySlotByLocation(int x, int y) {
		for(Node node : inventorySlots.getChildren()) {
			InventorySlot slot = (InventorySlot) node;
			if(slot.getX() == x && slot.getY() == y) {
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
