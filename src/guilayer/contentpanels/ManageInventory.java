package guilayer.contentpanels;

import java.awt.CardLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;

import guilayer.inventorypanels.*;

public class ManageInventory extends JPanel {
	
	public ManageInventory() {

		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		ItemEditor itemEditor = new ItemEditor();
		add(itemEditor);
		
		ListInventory listInv = new ListInventory(itemEditor);
		add(listInv);
	}
}
