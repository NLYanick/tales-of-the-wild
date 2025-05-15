package model;

import java.util.ArrayList;

public class Inventory {

	private final static int SIZE = 60;
	
	private ArrayList<Item> items;
	private boolean isFull;
	
	public Inventory() {
		items = new ArrayList<Item>();
	}
	
	public void addItem(Item item) {
		if(items.size() < SIZE)
			items.add(item);
		else 
			isFull = true;
	}
	
	public void removeItem(Item item) {
		if(items.size() > 0)
			items.remove(item);
		if(items.size() < SIZE)
			isFull = false;
	}
	
	public Item getItemWithId(int id) {
		for(Item item : items) {
			if(item.getId() == id) {
				return item;
			}
		}
		return null;
	}
	
	public boolean isFull() {
		return isFull;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}

}
