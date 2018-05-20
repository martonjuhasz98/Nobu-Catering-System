package dblayer;

import java.util.ArrayList;
import java.util.Arrays;

import dblayer.interfaces.IFDBItem;
import dblayer.interfaces.IFDBItemCategory;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

public class DBItemCategory implements IFDBItemCategory {

	private ArrayList<ItemCategory> categories = new ArrayList<ItemCategory>(Arrays.asList(
			new ItemCategory(1, "Vegetables"),
			new ItemCategory(2, "Meats"),
			new ItemCategory(3, "Fishes"),
			new ItemCategory(4, "Fruites"),
			new ItemCategory(5, "Dairy"),
			new ItemCategory(6, "Nonperishable")
	));
	
	@Override
	public ArrayList<ItemCategory> getCategories() {
		// TODO Auto-generated method stub
		return categories;
	}

	@Override
	public ItemCategory selectCategory(int id) {
		// TODO Auto-generated method stub
		ItemCategory result = null;
		
		for (ItemCategory itemCategory : categories) {
			if (itemCategory.getId() == id) {
				result = itemCategory;
			}
		}
		
		return result;
	}

	@Override
	public int insertCategory(ItemCategory category) {
		// TODO Auto-generated method stub
		return category.getId();
	}

	@Override
	public boolean deleteCategory(ItemCategory category) {
		// TODO Auto-generated method stub
		return true;
	}
}
