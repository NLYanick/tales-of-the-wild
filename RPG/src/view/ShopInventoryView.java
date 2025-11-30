package view;

public class ShopInventoryView extends InventoryView {

	public ShopInventoryView(MainScene scene) {
		super(scene);
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
	}

}
