package ctrllayer;

import java.sql.Date;

import dblayer.DBAnalytics;
import dblayer.interfaces.IFDBAnalytics;

public class AnalyticsController {


	IFDBAnalytics dba = new DBAnalytics();
	public String[][] getSalesBbreakdown(Date from, Date to) {
		return dba.getSalesBbreakdown(from, to);
	}
	
	public String[][] getWaste(Date from, Date to) {
		return dba.getWaste(from, to);
	}
	
	public String[][] getSales(Date from, Date to) {
		return dba.getSales(from, to);
	}
	public String[][] getWeeklyAverage() {
		return dba.getWeeklyAverage();
	}


}
