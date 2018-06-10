package guilayer.essentials;

import javax.swing.JTabbedPane;

public abstract class NavigationTabbedPane extends JTabbedPane implements Navigatable {
	
	public NavigationTabbedPane() {
		super();
	}
	
	public abstract void prepare();
	public abstract void reset();
}
