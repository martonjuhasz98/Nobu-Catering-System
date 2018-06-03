package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import ctrllayer.AnalyticsController;

class TestAnalytics {

	AnalyticsController ac = new AnalyticsController();
	SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY-MM-dd");
	java.sql.Date sqlFromDate;
	java.sql.Date sqlToDate;
	 
	@BeforeClass 
	void Build(){
		try {
			String fromDate="1996-12-31";
			String toDate="2004-01-02";
			
			java.util.Date startDate = sdf1.parse(fromDate);
			java.util.Date endDate = sdf1.parse(toDate);
			
			sqlFromDate = new java.sql.Date(startDate.getTime());
			sqlToDate = new java.sql.Date(endDate.getTime());
			
		}catch(java.text.ParseException e1) {
			System.out.print(e1.getMessage());
		}
		
	}
	
	
	@Test
	void AN01() {
		//somehow @BeforeClass isnt working
		Build();
			
		String[][] data = new String[0][0];
		data = ac.getWaste(sqlFromDate, sqlToDate);
			
		assertTrue(data[0][5].contains("8"));
	}
	
	@Test
	void AN02() {
		//somehow @BeforeClass isnt working
		Build();
		
		String[][] data = new String[0][0];
		data = ac.getSales(sqlFromDate, sqlToDate);
		assertTrue(data[0][2].contains("-119"));
	}
	
	@Test
	void AN03() {
		//somehow @BeforeClass isnt working
		Build();
		
		String[][] data = new String[0][0];
		data = ac.getSalesBbreakdown(sqlFromDate, sqlToDate);
		assertTrue(data[0][4].contains("1"));
	}

}
