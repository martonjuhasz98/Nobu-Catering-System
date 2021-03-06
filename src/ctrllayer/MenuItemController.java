package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import dblayer.interfaces.*;
import modlayer.*;

public class MenuItemController {

	private IFDBMenuItem dbMenuItem;
	private IFDBMenuItemCategory dbCategory;

	public MenuItemController() {
		dbMenuItem = new DBMenuItem();
		dbCategory = new DBMenuItemCategory();
	}

	//MenuItems
	public ArrayList<MenuItem> getMenuItems() {
		return dbMenuItem.getMenuItems(false);
	}
	public ArrayList<MenuItem> searchMenuItems(String keyword) {
		return dbMenuItem.searchMenuItems(keyword, false);
	}
	public ArrayList<MenuItem> getOrderedMenuItems() {
		return dbMenuItem.getMenuItems(true);
	}
	public ArrayList<MenuItem> searchOrderedMenuItems(String keyword) {
		return dbMenuItem.searchMenuItems(keyword, true);
	}
	public MenuItem getMenuItem(int id) {
		return dbMenuItem.selectMenuItem(id);
	}
	public boolean createMenuItem(int id, String name, double price, MenuItemCategory category, ArrayList<Ingredient> ingredients) {
		MenuItem item = new MenuItem();
		item.setId(id);
		item.setName(name);
		item.setPrice(price);
		item.setCategory(category);
		item.setIngredients(ingredients);
		
		boolean success = dbMenuItem.insertMenuItem(item) > 0;
		
		return success;
	}
	public boolean updateMenuItem(MenuItem item) {
		return dbMenuItem.updateMenuItem(item);
	}
	public boolean deleteMenuItem(MenuItem item) {
		boolean success = dbMenuItem.deleteMenuItem(item);
		
		return success;
	}
	
	//MenuItemCategories
	public ArrayList<MenuItemCategory> getCategories() {
		return dbCategory.getCategories();
	}
	public MenuItemCategory getCategory(int id) {
		return dbCategory.selectCategory(id);
	}
	public boolean createMenuItemCategory(String name) {
		MenuItemCategory category = new MenuItemCategory();
		category.setName(name);
		
		int id = dbCategory.insertCategory(category);
		boolean success = id > 0;
		if (success) {
			category.setId(id);
		}
		
		return success;
	}
}
