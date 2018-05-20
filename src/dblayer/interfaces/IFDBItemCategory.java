package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBItemCategory {

    public ArrayList<ItemCategory> getCategories();
    public ItemCategory selectCategory(int id);
    public int insertCategory(ItemCategory category);
    public boolean deleteCategory(ItemCategory category);
}