package guilayer.contentpanels;

import guilayer.essentials.NavigationPanel;
import guilayer.stocktaking.*;

public class Stocktaking extends NavigationPanel {
	
	private CheckInventory checkInv;
	
	public Stocktaking() {
		super();
		
		initalize();
	}

	private void initalize() {
		checkInv = new CheckInventory();
		add(checkInv);
	}

	@Override
	public void prepare() {
		checkInv.prepare();
	}
	@Override
	public void reset() {
		checkInv.reset();
	}
}
