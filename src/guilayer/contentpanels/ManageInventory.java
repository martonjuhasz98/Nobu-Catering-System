package guilayer.contentpanels;

import java.awt.CardLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;

import guilayer.inventory.*;

public class ManageInventory extends JPanel {
	
	public ManageInventory() {

		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		EditItem itemEditor = new EditItem();
		add(itemEditor);
		
		ListInventory listInv = new ListInventory(itemEditor);
		add(listInv);
	}
}
