package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import dblayer.interfaces.*;
import modlayer.*;

public class ItemController {

	private IFDBItem dbItem;
	private IFDBItemCategory dbCategory;
	private IFDBUnit dbUnit;
	private IFDBStocktaking dbStocktaking;

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
		//New category
		if (category.getId() < 1) {
			int categoryId = dbCategory.insertCategory(category);
			if (categoryId < 1) return false;
			category.setId(categoryId);
		}
		//Item
		Item item = new Item();
		item.setBarcode(barcode);
		item.setName(name);
		item.setQuantity(quantity);
		item.setUnit(unit);
		item.setCategory(category);
		
		return dbItem.insertItem(item).equals(barcode);
	}
	public boolean updateItem(Item item) {
		//New category
		ItemCategory category = item.getCategory();
		if (category.getId() < 1) {
			int categoryId = dbCategory.insertCategory(category);
			if (categoryId < 1) return false;
			category.setId(categoryId);
			item.setCategory(category);
		}
		
		return dbItem.updateItem(item);
	}
	public boolean deleteItem(Item item) {
		return dbItem.deleteItem(item);
	}
	
	//ItemCategories
	public ArrayList<ItemCategory> getCategories() {
		return dbCategory.getCategories();
	}

	//Units
	public ArrayList<Unit> getUnits() {
		return dbUnit.getUnits();
	}
	
	//Stocktaking
	public boolean createStocktaking( ArrayList<Item> items) {
		//Discrepancies
		ArrayList<Discrepancy> discrepancies = new ArrayList<Discrepancy>();
		for (Item item : items) {
			Discrepancy discrepancy = new Discrepancy();
			discrepancy.setItem(item);
			discrepancy.setQuantity(item.getQuantity());
			discrepancies.add(discrepancy);
		}
		//User
		Employee user = SessionSingleton.getInstance().getUser();
		if (user == null) return false;
		//Stocktaking
		Stocktaking stocktaking = new Stocktaking();
		stocktaking.setDiscrepancies(discrepancies);
		stocktaking.setEmployee(user);
		
		return dbStocktaking.insertStocktaking(stocktaking) > 0;
	}
}
