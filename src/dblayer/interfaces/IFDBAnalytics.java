package dblayer.interfaces;

import java.sql.Date;

public interface IFDBAnalytics {

	public String[][] getDailySales(Date from, Date to);
	
	public String[][] getWaste(Date from, Date to);
}
