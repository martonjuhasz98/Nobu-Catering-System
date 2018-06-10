package guilayer.contentpanels;

import guilayer.essentials.NavigationPanel;
import guilayer.essentials.PerformListener;
import guilayer.orders.*;

public class CreateOrders extends NavigationPanel {
	
	private EditOrder createOrder;
	
	public CreateOrders() {
		super();
		
		initalize();
	}

	private void initalize() {
		
		createOrder = new EditOrder();
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
	@Override
	public void prepare() {
		//createOrder.prepare();
	}
	@Override
	public void reset() {
		//createOrder.reset();
	}
}
