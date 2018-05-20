package guilayer.analytics;

import javax.swing.JTabbedPane;

public class AnalyticsTabbedPane extends JTabbedPane {

	public AnalyticsTabbedPane() {
		add(new SalesTab(),"Sales");
		add(new WasteTab(),"Waste");
	}

}
