package guilayer.contentpanels;

import javax.swing.JPanel;

import guilayer.orders.*;

public class ManageOrders extends JPanel {
	
	public ManageOrders() {
		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		PayOrder payOrder = new PayOrder();
		add(payOrder);
		
		EditOrder editOrder = new EditOrder();
		add(editOrder);
		
		ListOrders listOrders = new ListOrders(payOrder, editOrder);
		add(listOrders);
	}
}
