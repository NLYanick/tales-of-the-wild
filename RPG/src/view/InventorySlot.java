package view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.BorderPane;
import model.Location;

public class InventorySlot extends BorderPane {

	private BorderPane slot;
	private ItemView itemView;
	
	private Location location; 
	private BooleanProperty selected;
	
	public InventorySlot(Location location) {
		this.location = location;
		selected = new SimpleBooleanProperty();
		
		setUpLayout();
	}
	
	private void setUpLayout() {		
		slot = new BorderPane();
		slot.getStyleClass().add("inventory-slot");
		
		selected.addListener(((observableValue, oldValue, isSelected) -> {
			if(isSelected) {
				slot.getStyleClass().add("selected-inventory-slot");
				slot.getStyleClass().remove("inventory-slot");
			} else {
				slot.getStyleClass().add("inventory-slot");
				slot.getStyleClass().remove("selected-inventory-slot");
			}
		}));
		
		setCenter(slot);
	}

	public ItemView getItemView() {
		return itemView;
	}

	public void setItemView(ItemView itemView) {
		this.itemView = itemView;
		slot.setCenter(itemView);
	}
	
	public void removeItemView() {
		this.itemView = null;
		slot.setCenter(null);
	}
	
	public int getX() {
		return location.getX();
	}
	
	public int getY() {
		return location.getY();
	}

	public boolean isSelected() {
		return selected.get();
	}

	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}
	
}
