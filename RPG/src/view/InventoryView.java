package view;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Item;
import model.Location;

public class InventoryView extends StackPane {
	
	protected final static int GRIDWIDTH = 10;
	protected final static int GRIDHEIGHT = 6;
	
	protected MainScene scene;
	protected GridPane inventorySlots;
	protected BorderPane root, infoBox;
	
	protected Button close, closeModal;
	
	protected StringProperty infoBoxName, infoBoxDescription, modalMessage;
	protected BooleanBinding slotIsFocused;
	
	protected boolean canDragAndDrop = true;
	
	protected ArrayList<ItemView> itemViews;
	
	protected HashMap<String, String> styling;
	protected HashMap<String, String> texts;
	
	protected ItemView draggedItemView;
	protected InventorySlot focusedSlot, selectedSlot, activatedSlot; // Difference focused and selected: Selected == focused + when no slots are focused
		
	public InventoryView(MainScene scene) {
		this.scene = scene;
		
		setUpLayout();
	}
	
	private void setUpLayout() {
		styling = new HashMap<String, String>();
		texts = new HashMap<String, String>();
		setUpStyling();
		setUpTexts();
		
		setBackground(new Background(new BackgroundImage(new Image(styling.get("background")), null, null, null, null)));
		
		root = new BorderPane();
		
		BorderPane leftPane = getLeftPane();
		root.setLeft(leftPane);
				
		setUpInventorySlots();
		setUpInfoBox();
		
		HBox boxes = getInventoryBoxes();
		root.setCenter(boxes);
		
		BorderPane modal = getModalContainer();
		getChildren().addAll(root, modal);
		
		setOnKeyPressed(e -> handleKeyPressed(e));
	}

	protected void setUpStyling() {
		styling.put("background", "Images/Background/Grass/Grass.png");
		
		styling.put("buttons-pane", "inventory-buttons-pane");
		styling.put("title", "inventory-title");
		styling.put("slots-box", "inventory-slots-box");
		styling.put("button", "menu-button");
		
		styling.put("info-box", "inventory-info-box");
		styling.put("info-box-title", "inventory-info-box-title");
		styling.put("info-box-separator-line", "inventory-info-box-separator-line");
		
		styling.put("info-box-description", "inventory-info-box-description");
		styling.put("modal", "inventory-modal");
		styling.put("modal-text", "inventory-modal-text");
		styling.put("modal-close", "inventory-modal-close");
	}
	
	protected void setUpTexts() {
		texts.put("title", "Inventory");
		texts.put("button-1", "Close");
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
		int rectWidth = 300;
		int rectHeight = 104;
		
		BorderPane leftPane = new BorderPane();
		leftPane.getStyleClass().add(styling.get("buttons-pane"));
		
		VBox titleBox = getTitleBox();
		leftPane.setTop(titleBox);
		
		VBox buttonsPane = getButtonsPane();
		leftPane.setCenter(buttonsPane);
		
		Rectangle invisRect = new Rectangle(rectWidth, rectHeight);
		invisRect.setFill(Color.TRANSPARENT);
		leftPane.setBottom(invisRect);
		
		return leftPane;
	}
	
	protected VBox getButtonsPane() {
		int spacing = 30;
		
		close = getButton(texts.get("button-1"));
		close.setOnAction(e -> scene.removeInventoryView(this));
		
		VBox buttonsPane = new VBox(close);
		buttonsPane.setSpacing(spacing);
		buttonsPane.setAlignment(Pos.CENTER);
		
		return buttonsPane;
	}
	
	private VBox getTitleBox() {
		int spacing = 60;
		
		Text title = new Text(texts.get("title"));
		title.getStyleClass().add(styling.get("title"));
		
		VBox titleBox = new VBox(title);
		titleBox.setPadding(new Insets(spacing, 0, 0, 0));
		titleBox.setAlignment(Pos.TOP_CENTER);
		
		return titleBox;
	}
	
	private void setUpInventorySlots() {
		
		inventorySlots = new GridPane();
		
		int gapSize = 10;

		makeSlots();
		
		inventorySlots.setHgap(gapSize);
		inventorySlots.setVgap(gapSize);
		inventorySlots.getStyleClass().add(styling.get("slots-box"));
		
		inventorySlots.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
		inventorySlots.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
		inventorySlots.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);		
	}
	
	protected void makeSlots() {
		for(int x = 0; x < GRIDWIDTH; x++) {
			for(int y = 0; y < GRIDHEIGHT; y++) {
				InventorySlot slot = new InventorySlot(new Location(x, y));
				
				handleEvents(slot);
				inventorySlots.add(slot, x, y);
			}
		}
	}
	
	protected void handleEvents(InventorySlot slot) {
		slot.setOnKeyPressed(e -> handleKeyPressedSlots(e, slot));
		slot.focusedProperty().addListener(((observableValue, oldValue, isFocused) -> {
			focusInventorySlot(slot, isFocused);
		}));
		
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
	
	private void handleKeyPressedSlots(KeyEvent e, InventorySlot slot) {
		if(e.getCode() == KeyCode.ENTER) {
			if(canDragAndDrop) moveItem(e, slot);
		}
	}
	
	private void moveItem(KeyEvent e, InventorySlot slot) {
		deactivateSlots();
		if(activatedSlot != null) {
			InventorySlot target = (InventorySlot) e.getTarget();
			if(target.getItemView() == null) {
				target.setItemView(activatedSlot.getItemView());
				activatedSlot.setItemView(null);
			}
			activatedSlot = null;
		} else {				
			activatedSlot = slot;
			activatedSlot.switchActive();
		}
	}
	
	protected void focusInventorySlot(InventorySlot slot, boolean focused) {
		if(focused) focusedSlot = slot;
		else focusedSlot = null;
		
		ItemView itemView = slot.getItemView();
		Item item = scene.getItemFromView(itemView);
		
		infoBoxName.set(itemView != null ? item.getName() : "");
		infoBoxDescription.set(itemView != null ? item.getDescription() : "");
		
		selectSlot(slot);
	}
	
	protected void selectSlot(InventorySlot slot) {
		if(selectedSlot != null)
			selectedSlot.handleSelectedClass(false);
		
		selectedSlot = slot;
		selectedSlot.handleSelectedClass(true);
	}
	
	private void setUpInfoBox() {
		infoBox = new BorderPane();
		infoBox.getStyleClass().add(styling.get("info-box"));
		
		setUpBindings();
		
		VBox texts = getInfoTexts();
		
		infoBox.setCenter(texts);
	}
	
	protected void setUpBindings() {
		infoBoxName = new SimpleStringProperty();
		infoBoxDescription = new SimpleStringProperty();
		modalMessage = new SimpleStringProperty();
		
		slotIsFocused = new SimpleBooleanProperty(false).not();
		slotIsFocused = infoBoxName.isNotEmpty().and(infoBoxDescription.isNotEmpty());
	}
	
	protected VBox getInfoTexts() {
		int spacing = 10;
		int innerSpace = 434;
		
		Text name = getText("info-box-title", innerSpace, infoBoxName);

		Line separator = getSeparatorLine("info-box-separator-line", innerSpace, true);
		
		Text description = getText("info-box-description", innerSpace, infoBoxDescription);
		
		VBox texts = new VBox(name, separator, description);
		texts.setAlignment(Pos.TOP_LEFT);
		texts.setSpacing(spacing);
		
		return texts;
	}
	
	protected Text getText(String style, double innerSpace, StringProperty binding) {
		Text text = new Text();
		text.getStyleClass().add(styling.get(style));
		text.textProperty().bind(binding);
		text.setWrappingWidth(innerSpace);
		
		return text;
	}
	
	protected Text getText(String style, StringProperty binding) {
		Text text = new Text();
		text.getStyleClass().add(styling.get(style));
		text.textProperty().bind(binding);
		
		return text;
	}
	
	protected Line getSeparatorLine(String style, double innerSpace, boolean horizontal) {
		Line separator = (horizontal ? new Line(0, 0, innerSpace, 0) : new Line(0, 0, 0, innerSpace));
		separator.getStyleClass().add(styling.get(style));
		separator.visibleProperty().bind(slotIsFocused);
		
		return separator;
	}
	
	private HBox getInventoryBoxes() {
		int spacing = 80;
		
		HBox boxes = new HBox(inventorySlots, infoBox);
		boxes.setSpacing(spacing);
		boxes.setAlignment(Pos.CENTER);
		
		return boxes;
	}
	
	private BorderPane getModalContainer() {
		BorderPane modalContainer = new BorderPane();
		
		modalContainer.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.6), null, null)));
		modalContainer.toFront();
		modalContainer.visibleProperty().bind(modalMessage.isNotEmpty());
		modalContainer.setPickOnBounds(true);
		
		modalContainer.visibleProperty().addListener((obs, wasVisible, isVisible) -> {
		    if (isVisible) {
		        Platform.runLater(() -> closeModal.requestFocus());
		        root.setDisable(true);
		    } else {
		    	root.setDisable(false);		    	
		    }
		});
		
		StackPane modal = getModal();
		
		modalContainer.setCenter(modal);
		
		return modalContainer;
	}
	
	private StackPane getModal() {
		int margin = 10;
		
		Text message = new Text();
		message.textProperty().bind(modalMessage);
		message.getStyleClass().add(styling.get("modal-text"));
		
		closeModal = new Button("✖");
		closeModal.setOnAction(e -> modalMessage.set(""));
		closeModal.setOnKeyPressed(e -> handleButtonKeyPressed(e, closeModal));
		closeModal.getStyleClass().add(styling.get("modal-close"));
		
		StackPane.setAlignment(closeModal, Pos.TOP_RIGHT);
		StackPane.setMargin(closeModal, new Insets(margin));
		
		StackPane modal = new StackPane(message, closeModal);
		modal.getStyleClass().add(styling.get("modal"));
		
		return modal;
	}
	
	protected Button getButton(String text) {
		
		Button button = new Button(text);
		
		button.getStyleClass().add(styling.get("button"));
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
		if(canDragAndDrop) makeItemDraggable(itemView);
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
		if(focusedSlot == null) return;
		
		dropItem(focusedSlot.getItemView());
	}
	
	private void deactivateSlots() {
		for(Node node : inventorySlots.getChildren()) {
			InventorySlot slot = (InventorySlot) node;
			if(slot.isActive()) {
				slot.switchActive();
			}
		}
	}
	
	public void disableActiveSlot() {
		deactivateSlots();
		activatedSlot = null;
		
		if(selectedSlot != null)
			selectedSlot.handleSelectedClass(false);
		selectedSlot = null;
	}
	
	private void dropItem(ItemView itemView) {
		if(itemView == null) return;

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
	
	public void setError(String error) {
		modalMessage.set(error);
	}
	
}
