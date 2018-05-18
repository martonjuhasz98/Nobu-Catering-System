package dblayer;

import java.util.ArrayList;

import dblayer.interfaces.IFDBItem;
import modlayer.Item;

public class DBItem implements IFDBItem {

	private ArrayList<Item> items = new ArrayList<Item>();
	
	@Override
	public ArrayList<Item> getItems() {
		// TODO Auto-generated method stub
		return items;
	}

	@Override
	public ArrayList<Item> searchItems(String keyword) {
		// TODO Auto-generated method stub
		ArrayList<Item> results = new ArrayList<>();
		keyword = keyword.toLowerCase();
		
		for (Item item : items) {
			if (item.getBarcode().toLowerCase().indexOf(keyword) > -1 ||
				item.getName().toLowerCase().indexOf(keyword) > -1 ||
				item.getCategory().getName().toLowerCase().indexOf(keyword) > -1) {
				results.add(item);
			}
		}
		
		return results;
	}

	@Override
	public Item selectItem(String barcode) {
		// TODO Auto-generated method stub
		Item result = null;
		
		for (Item item : items) {
			if (item.getBarcode().equals(barcode)) {
				result = item;
			}
		}
		
		return result;
	}

	@Override
	public String insertItem(Item item) {
		// TODO Auto-generated method stub
		return item.getBarcode();
	}

	@Override
	public boolean updateItem(Item item) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean deleteItem(Item item) {
		// TODO Auto-generated method stub
		return true;
	}

	
	
}
