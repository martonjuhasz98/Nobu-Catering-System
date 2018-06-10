package guilayer.contentpanels;

import guilayer.analytics.*;
import guilayer.essentials.NavigationTabbedPane;

public class Analytics extends NavigationTabbedPane {

	private WasteTab wasteTab;
	private SalesGraph salesGraph;
	private SalesBreakdownTab salesBreakdownTab;
	private WeeklyUsageTab weeklyUsageTab;
	
	public Analytics() {
		super();
		
		initialize();
	}
	
	private void initialize() {
		
		wasteTab = new WasteTab();
		add(wasteTab, "Waste breakdown");
		
		salesGraph = new SalesGraph();
		add(salesGraph, "Sales graph");
		
		salesBreakdownTab = new SalesBreakdownTab();
		add(salesBreakdownTab,"Sales Item Breakdown");
		
		weeklyUsageTab = new WeeklyUsageTab();
		add(weeklyUsageTab, "Weekly Usage Breakdown");
	}

	@Override
	public void prepare() {
		//wasteTab.prepare();
		//salesGraph.prepare();
		//salesBreakdownTab.prepare();
		//weeklyUsageTab.prepare();
	}
	@Override
	public void reset() {
		//wasteTab.reset();
		//salesGraph.reset();
		//salesBreakdownTab.reset();
		//weeklyUsageTab.reset();
	}
}
