package guilayer.contentpanels;

import java.awt.CardLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;

import guilayer.inventorypanels.CreateInventory;
import guilayer.inventorypanels.EditInventory;
import guilayer.inventorypanels.ListInventory;

public class ManageInventory extends JPanel {
	
	public ManageInventory() {

		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		CreateInventory createInv = new CreateInventory();
		add(createInv);
		
		EditInventory editInv = new EditInventory();
		add(editInv);
		
		ListInventory listInv = new ListInventory(createInv, editInv);
		add(listInv);
	}
}
