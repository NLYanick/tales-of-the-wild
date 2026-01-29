package view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ShopInventoryView extends InventoryView {
	
	public ShopInventoryView(MainScene scene) {
		super(scene);
		
		canDragAndDrop = false;
	}
	
	protected void setUpStyling() {
		styling.put("background", "Images/Background/Water/Sand.png");
		
		styling.put("buttons-pane", "shop-inventory-buttons-pane");
		styling.put("slots-box", "shop-inventory-slots-box");
		styling.put("info-box", "shop-inventory-info-box");
		styling.put("button", "shop-inventory-button");
		
		styling.put("title", "inventory-title");
		styling.put("info-box-title", "inventory-info-box-title");
		styling.put("info-box-separator-line", "inventory-info-box-separator-line");
		styling.put("info-box-description", "inventory-info-box-description");
	}
	
	protected void setUpTexts() {
		texts.put("title", "Shop");
		
		texts.put("button-1", "Close");
		texts.put("button-2", "Buy");
	}
	
	protected VBox getButtonsPane() {
		int spacing = 30;
		
		close = getButton(texts.get("button-1"));
		close.setOnAction(e -> scene.removeInventoryView(this));
		
		Button buy = getButton(texts.get("button-2"));
		buy.setOnAction(e -> buyItem());
		
		VBox buttonsPane = new VBox(close, buy);
		buttonsPane.setSpacing(spacing);
		buttonsPane.setAlignment(Pos.CENTER);
		
		return buttonsPane;
	}
	
	public void buyItem() {
		ItemView itemView = selectedSlot.getItemView();
		if(itemView == null) return;
		
		System.out.println(itemView);
	}

}
