package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import modlayer.*;

public class ItemController {

	private DBItem dbItem;
	private DBItemCategory dbCategory;
	private DBUnit dbUnit;
	private DBStocktaking dbStocktaking;

	public ItemController() {
		dbItem = new DBItem();
		dbCategory = new DBItemCategory();
		dbUnit = new DBUnit();
		dbStocktaking = new DBStocktaking();
	}

	//Items
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
	
	//ItemCategories
	public ArrayList<ItemCategory> getCategories() {
		return dbCategory.getCategories();
	}
	public ItemCategory getCategory(int id) {
		return dbCategory.selectCategory(id);
	}
	public boolean createItemCategory(String name) {
		ItemCategory category = new ItemCategory();
		category.setName(name);
		
		int id = dbCategory.insertCategory(category);
		boolean success = id > 0;
		if (success) {
			category.setId(id);
		}
		
		return success;
	}

	//Units
	public ArrayList<Unit> getUnits() {
		return dbUnit.getUnits();
	}
	public Unit getUnit(String abbr) {
		return dbUnit.selectUnit(abbr);
	}
	
	//Stocktaking
	public boolean createStocktaking(Employee employee, ArrayList<Item> items) {
		ArrayList<Discrepancy> discrepancies = new ArrayList<Discrepancy>();
		for (Item item : items) {
			Discrepancy discrepancy = new Discrepancy();
			discrepancy.setItem(item);
			discrepancy.setQuantity(item.getQuantity());
			discrepancies.add(discrepancy);
		}
		
		Stocktaking stocktaking = new Stocktaking();
		stocktaking.setEmployee(employee);
		stocktaking.setDiscrepancies(discrepancies);
		
		int id = dbStocktaking.insertStocktaking(stocktaking);
		boolean success = id > 0;
		if (success) {
			stocktaking.setId(id);
		}
		
		return success;
	}
}
