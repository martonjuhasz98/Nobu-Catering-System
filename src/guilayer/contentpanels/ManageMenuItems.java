package guilayer.contentpanels;

import javax.swing.JPanel;
import guilayer.menuitems.*;

public class ManageMenuItems extends JPanel {

	public ManageMenuItems() {
		initalize();
	}

	private void initalize() {

		setLayout(null);

		EditMenuItem editMenuItem = new EditMenuItem();
		add(editMenuItem);

		ListMenuItems listMenuItems = new ListMenuItems(editMenuItem);
		add(listMenuItems);
	}
}
