package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBMenuItemCategory {

    public ArrayList<MenuItemCategory> getCategories();
    public MenuItemCategory selectCategory(int id);
    public int insertCategory(MenuItemCategory category);
}