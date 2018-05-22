package guilayer.contentpanels;

import javax.swing.JTabbedPane;

import guilayer.analytics.SalesGraph;
import guilayer.analytics.SalesBreakdownTab;
import guilayer.analytics.WasteTab;
import guilayer.analytics.WeeklyUsageTab;

public class AnalyticsTabbedPane extends JTabbedPane {

	public AnalyticsTabbedPane() {
		add(new WasteTab(),"Waste breakdown");
		add(new SalesGraph(), "Sales graph");
		add(new SalesBreakdownTab(),"Sales Item Breakdown");
		add(new WeeklyUsageTab(), "Weekly Usage Breakdown");
	}

}
