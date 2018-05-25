package guilayer.contentpanels;

import javax.swing.JTabbedPane;

import guilayer.analytics.*;

public class Analytics extends JTabbedPane {

	public Analytics() {
		add(new WasteTab(),"Waste breakdown");
		add(new SalesGraph(), "Sales graph");
		add(new SalesBreakdownTab(),"Sales Item Breakdown");
		add(new WeeklyUsageTab(), "Weekly Usage Breakdown");
	}

}
