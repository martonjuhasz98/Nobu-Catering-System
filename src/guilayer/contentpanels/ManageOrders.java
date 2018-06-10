package guilayer.contentpanels;

import guilayer.essentials.NavigationPanel;
import guilayer.orders.*;

public class ManageOrders extends NavigationPanel {
	
	private PayOrder payOrder;
	private EditOrder editOrder;
	private ListOrders listOrders;

	public ManageOrders() {
		super();
		
		initalize();
	}

	private void initalize() {
		
		payOrder = new PayOrder();
		add(payOrder);
		
		editOrder = new EditOrder();
		add(editOrder);
		
		listOrders = new ListOrders(payOrder, editOrder);
		add(listOrders);
	}
	@Override
	public void prepare() {
		//payOrder.prepare();
		//editOrder.prepare();
		//listOrders.prepare();
	}
	@Override
	public void reset() {
		//payOrder.reset();
		//editOrder.reset();
		//listOrders.reset();
	}
}
