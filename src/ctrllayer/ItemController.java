package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import modlayer.*;

public class ItemController {

	private DBItem dbItem;

	public ItemController() {
		dbItem = new DBItem();
	}

	public ArrayList<Item> getItems() {
		return dbItem.getItems();
	}
	public ArrayList<Item> searchItems(String keyword) {
		return dbItem.searchItems(keyword);
	}
	public Item getItem(String barcode) {
		return dbItem.selectItem(barcode);
	}
	public boolean createItem(String barcode, String name, double quantity, Unit unit, ItemCategory category) {
		Item item = new Item();
		item.setBarcode(barcode);
		item.setName(name);
		item.setQuantity(quantity);
		item.setUnit(unit);
		item.setCategory(category);
		
		boolean success = dbItem.insertItem(item) != null;
		
		return success;
	}
	public boolean updateItem(Item item) {
		return dbItem.updateItem(item);
	}
	public boolean deleteItem(Item item) {
		boolean success = dbItem.deleteItem(item);
		
		return success;
	}
	
	//TODO: get categories
	public String[] getCategories(){
		return new String[]{"meat","vegetable","non-perishable","dairy"};
	}
}
