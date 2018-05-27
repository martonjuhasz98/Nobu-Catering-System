package guilayer.contentpanels;

import javax.swing.JPanel;

import guilayer.orders.*;

public class ManageOrders extends JPanel {
	
	public ManageOrders() {
		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		CreateOrder createOrder = new CreateOrder();
		add(createOrder);
	}
}
