package guilayer.contentpanels;

import javax.swing.JTabbedPane;

import guilayer.analytics.SalesTab;
import guilayer.analytics.WasteTab;

public class AnalyticsTabbedPane extends JTabbedPane {

	public AnalyticsTabbedPane() {
		add(new SalesTab(),"Sales");
		add(new WasteTab(),"Waste");
	}

}
