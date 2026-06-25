package view;

import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import model.Location;

public class InventorySlot extends BorderPane {

	private BorderPane slot;
	private ItemView itemView;
	
	private Location location; 
	
	public InventorySlot(Location location) {
		this.location = location;
		
		setFocusTraversable(true);
		setUpLayout();
	}
	
	private void setUpLayout() {		
		slot = new BorderPane();
		slot.getStyleClass().add("inventory-slot");
		
		setOnMouseClicked(e -> requestFocus());
		focusedProperty().addListener(((observableValue, oldValue, isFocused) -> {
			handleSelectedClass(isFocused);
		}));
		
		setCenter(slot);
		
		slot.setOnDragOver(e -> {
			if(e.getDragboard().hasString()) {				
				e.acceptTransferModes(TransferMode.ANY);
			}
		});
	}
	
	private void handleSelectedClass(boolean isSelected) {
		if(isSelected) {
			slot.getStyleClass().add("selected-inventory-slot");
			slot.getStyleClass().remove("inventory-slot");
		} else {
			slot.getStyleClass().add("inventory-slot");
			slot.getStyleClass().remove("selected-inventory-slot");
		}
	}
			
	public void setItemView(ItemView itemView) {
		this.itemView = itemView;
		slot.setCenter(itemView);
	}
	
	public void removeItemView() {
		if(itemView != null) {
			itemView.setOnDragDetected(null);
		}
		this.itemView = null;
		slot.setCenter(null);
	}
	
	public void switchActive() {
		if(slot.getStyleClass().contains("activated-inventory-slot")) { 
			slot.getStyleClass().remove("activated-inventory-slot"); 
		} else { 
			slot.getStyleClass().add("activated-inventory-slot"); 
		};
	}
	
	public boolean isActive() {
		return slot.getStyleClass().contains("activated-inventory-slot");
	}
	
	public ItemView getItemView() {
		return itemView;
	}
	
	public int getX() {
		return location.getX();
	}
	
	public int getY() {
		return location.getY();
	}
	
}
