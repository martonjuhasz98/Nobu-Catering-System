package guilayer.contentpanels;

import guilayer.essentials.NavigationPanel;
import guilayer.menuitems.*;

public class ManageMenuItems extends NavigationPanel {

	private EditMenuItem editMenuItem;
	private ListMenuItems listMenuItems;
	
	public ManageMenuItems() {
		super();
		
		initalize();
	}

	private void initalize() {

		editMenuItem = new EditMenuItem();
		add(editMenuItem);

		listMenuItems = new ListMenuItems(editMenuItem);
		add(listMenuItems);
	}
	@Override
	public void prepare() {
		editMenuItem.prepare();
		listMenuItems.prepare();	
	}
	@Override
	public void reset() {
		editMenuItem.reset();
		listMenuItems.reset();
	}
}
