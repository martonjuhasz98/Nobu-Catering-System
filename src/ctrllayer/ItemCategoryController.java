package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import modlayer.*;

public class ItemCategoryController {

	private DBItemCategory dbCategory;

	public ItemCategoryController() {
		dbCategory = new DBItemCategory();
	}

	public ArrayList<ItemCategory> getItemCategorys() {
		return dbCategory.getCategories();
	}
	public ItemCategory getCategory(int id) {
		return dbCategory.selectCategory(id);
	}
	public boolean createItemCategory(int id, String name) {
		ItemCategory category = new ItemCategory();
		category.setId(id);
		category.setName(name);
		
		boolean success = dbCategory.insertCategory(category) > 0;
		
		return success;
	}
	public boolean deleteItemCategory(ItemCategory category) {
		boolean success = dbCategory.deleteCategory(category);
		
		return success;
	}
}
