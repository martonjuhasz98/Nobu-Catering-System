package guilayer.contentpanels;

import javax.swing.JPanel;

import guilayer.interfaces.PerformListener;
import guilayer.orders.*;

public class CreateOrders extends JPanel {
	
	public CreateOrders() {
		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		EditOrder createOrder = new EditOrder();
		add(createOrder);
		
		createOrder.addPerformListener(new PerformListener() {
			@Override
			public void performed() {
				createOrder.create();
			}
			@Override
			public void cancelled() {
				createOrder.create();
			}
		});
		createOrder.create();
	}
}
