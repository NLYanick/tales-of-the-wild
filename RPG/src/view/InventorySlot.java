package view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
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
		int rectSize = 64;
		int rectStrokeWidth = 5;
		
		slot = new BorderPane();
		slot.setPrefSize(rectSize, rectSize);
		slot.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		slot.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(rectStrokeWidth))));
		
		selected.addListener(((observableValue, oldValue, isSelected) -> {
			if(isSelected) {
				slot.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(rectStrokeWidth)))); 
			} else {
				slot.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(rectStrokeWidth))));
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
