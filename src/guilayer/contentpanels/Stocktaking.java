package guilayer.contentpanels;

import javax.swing.JPanel;

import guilayer.stocktaking.*;

public class Stocktaking extends JPanel {
	
	public Stocktaking() {
		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		CheckInventory checkInv = new CheckInventory();
		add(checkInv);
	}
}
