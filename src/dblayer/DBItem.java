package dblayer;

import java.util.ArrayList;
import java.util.Arrays;

import dblayer.interfaces.IFDBItem;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

public class DBItem implements IFDBItem {

	private ArrayList<Unit> units = new ArrayList<Unit>(Arrays.asList(
			new Unit("g", "gramm"),
			new Unit("dkg", "dekagramm"),
			new Unit("kg", "kilogramm"),
			new Unit("ml", "mililiter"),
			new Unit("cl", "centiliter"),
			new Unit("dl", "deciliter"),
			new Unit("l", "liter"),
			new Unit("pcs", "pieces")
	));
	private ArrayList<ItemCategory> categories = new ArrayList<ItemCategory>(Arrays.asList(
			new ItemCategory(1, "Vegetables"),
			new ItemCategory(2, "Meats"),
			new ItemCategory(3, "Fishes"),
			new ItemCategory(4, "Fruites"),
			new ItemCategory(5, "Dairy")
	));
	private ArrayList<Item> items = new ArrayList<Item>(Arrays.asList(
			new Item("1", "Carrots", 2.5, units.get(1), categories.get(0)),
			new Item("2", "Chicken breast", 4.5, units.get(0), categories.get(1)),
			new Item("3", "Salmon", 7.5, units.get(0), categories.get(2)),
			new Item("4", "Apple", 13.2, units.get(2), categories.get(3)),
			new Item("5", "Milk", 3.0, units.get(6), categories.get(4))
	));
	
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
		items.add(item);
		return item.getBarcode();
	}

	@Override
	public boolean updateItem(Item item) {
		// TODO Auto-generated method stub
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getBarcode().equals(item.getBarcode())) {
				items.set(i, item);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deleteItem(Item item) {
		// TODO Auto-generated method stub
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getBarcode().equals(item.getBarcode())) {
				items.remove(i);
				return true;
			}
		}
		return false;
	}

	
	
}
