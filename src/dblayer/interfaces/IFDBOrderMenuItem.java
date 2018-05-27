package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBOrderMenuItem {

    public ArrayList<OrderMenuItem> getOrderMenuItems(int orderId);
    public OrderMenuItem selectOrderMenuItem(int orderId, int menuItemId); 
    public boolean insertOrderMenuItem(OrderMenuItem item);
    public boolean updateOrderMenuItem(OrderMenuItem item);
    public boolean deleteOrderMenuItem(OrderMenuItem item);
}