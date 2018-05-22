package dblayer.interfaces;

import java.sql.Date;

public interface IFDBAnalytics {

	public String[][] getSalesBbreakdown(Date from, Date to);

	public String[][] getWaste(Date from, Date to);

	public String[][] getSales(Date from, Date to);

	public String[][] getWeeklyAverage();

}
