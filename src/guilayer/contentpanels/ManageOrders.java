package guilayer.contentpanels;

import guilayer.essentials.NavigationPanel;
import guilayer.orders.*;

public class ManageOrders extends NavigationPanel {
	
	private EditOrder editOrder;
	private ListOrders listOrders;
	private SelectTable selectTable;

	public ManageOrders() {
		super();
		
		initalize();
	}

	private void initalize() {
		
		selectTable = new SelectTable(20);
		add(selectTable);
		
		editOrder = new EditOrder(selectTable);
		add(editOrder);
		
		listOrders = new ListOrders(editOrder);
		add(listOrders);
	}
	@Override
	public void prepare() {
		selectTable.prepare();
		editOrder.prepare();
		listOrders.prepare();
	}
	@Override
	public void reset() {
		selectTable.reset();
		editOrder.reset();
		listOrders.reset();
	}
}
