package guilayer.essentials;

import javax.swing.JPanel;

public abstract class NavigationPanel extends JPanel implements Navigatable {
	
	public NavigationPanel() {
		super();
		
		setLayout(null);
	}
	
	public abstract void prepare();
	public abstract void reset();
}
