package guilayer.contentpanels;

import guilayer.essentials.NavigationPanel;
import guilayer.essentials.PerformListener;
import guilayer.orders.*;

public class CreateOrders extends NavigationPanel {
	
	private EditOrder createOrder;
	private SelectTable selectTable;
	
	public CreateOrders() {
		super();
		
		initalize();
	}

	private void initalize() {
		
		selectTable = new SelectTable(20);
		add(selectTable);
		
		createOrder = new EditOrder(selectTable);
		add(createOrder);
		
		createOrder.addPerformListener(new PerformListener() {
			@Override
			public void performed() {
				createOrder.openToCreate();
			}
			@Override
			public void cancelled() {
				createOrder.openToCreate();
			}
		});
		createOrder.openToCreate();
	}
	@Override
	public void prepare() {
		selectTable.prepare();
		createOrder.prepare();
		
		createOrder.openToCreate();
	}
	@Override
	public void reset() {
		selectTable.reset();
		createOrder.reset();
	}
}
