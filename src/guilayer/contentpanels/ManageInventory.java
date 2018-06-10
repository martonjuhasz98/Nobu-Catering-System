package guilayer.contentpanels;

import guilayer.essentials.NavigationPanel;
import guilayer.inventory.*;

public class ManageInventory extends NavigationPanel {
	
	private EditItem itemEditor;
	private ListInventory listInv;
	
	public ManageInventory() {
		super();
		
		initalize();
	}

	private void initalize() {
		itemEditor = new EditItem();
		add(itemEditor);
		
		listInv = new ListInventory(itemEditor);
		add(listInv);
	}
	@Override
	public void prepare() {
		itemEditor.prepare();
		listInv.prepare();
	}
	@Override
	public void reset() {
		itemEditor.reset();
		listInv.reset();
	}
}
