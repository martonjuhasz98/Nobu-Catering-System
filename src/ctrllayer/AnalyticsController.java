package ctrllayer;

import java.sql.Date;

import dblayer.DBAnalytics;

public class AnalyticsController {


	DBAnalytics dba = new DBAnalytics();
	public String[][] getSalesData(Date from, Date to) {
		return dba.getDailySales(from, to);
	}
	
	public String[][] getWasteData(Date from, Date to) {
		return dba.getWaste(from, to);
	}
}
