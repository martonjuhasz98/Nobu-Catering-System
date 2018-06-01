package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBOrderMenuItem {

    public ArrayList<OrderMenuItem> getOrderMenuItems(int orderId);
    public OrderMenuItem selectOrderMenuItem(int orderId, int menuItemId);
    public boolean canCreateOrderMenuItem(OrderMenuItem item);
    public boolean hasOrderMenuItem(OrderMenuItem item);
    public boolean insertOrderMenuItem(OrderMenuItem item, boolean force);
    public boolean updateOrderMenuItem(OrderMenuItem item, boolean force);
    public boolean confirmOrderMenuItem(OrderMenuItem item);
    public boolean deleteOrderMenuItem(OrderMenuItem item);
}