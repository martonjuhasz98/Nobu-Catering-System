package guilayer.contentpanels;

import javax.swing.JPanel;

import guilayer.orders.*;

public class CreateOrders extends JPanel {
	
	public CreateOrders() {
		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		CreateOrder createOrder = new CreateOrder();
		add(createOrder);
	}
}
