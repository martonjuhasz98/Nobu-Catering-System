package ctrllayer;

import java.sql.Date;

import dblayer.DBAnalytics;
import dblayer.interfaces.IFDBAnalytics;

public class AnalyticsController {


	IFDBAnalytics dba = new DBAnalytics();
	public String[][] getSalesData(Date from, Date to) {
		return dba.getDailySales(from, to);
	}
	
	public String[][] getWasteData(Date from, Date to) {
		return dba.getWaste(from, to);
	}
	
	public String[][] getDailyWaste(Date from, Date to) {
		return dba.getWaste(from, to);
	}
}
