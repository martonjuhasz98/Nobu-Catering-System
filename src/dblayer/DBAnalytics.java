package dblayer;

import java.sql.Date; 

import dblayer.interfaces.IFDBAnalytics;

public class DBAnalytics implements IFDBAnalytics {
	

	@Override
	public String[][] getDailySales(Date from, Date to) {
		// TODO: select
		// SELECT 
		//		timestamp as [date],
		//		SUM(case when amount >= 0 then amount else 0 end) as revenue,
		//		SUM(case when amount < 0 then amount else 0 end) as costs,
		//		SUM(amount) as profit
		// FROM [Transaction] 
		// WHERE timestamp > fromDate AND timestamp < toDate
		// GROUP BY CAST(timestamp AS DATE) 
		// 
		return new String[][] {{"14-15-2018","420","9","qik mafs"}, {"27-19-2018","360","0","qik mafs"}};
	}

	@Override
	public String[][] getWaste(Date from, Date to) {
		// TODO Auto-generated method stub
		//
		// (SELECT 
		//		ingredient.item_barcode, 
		//		SUM(ingredient.quantity*Order_menu_item.quantity) as usage
		// FROM Order_menu_item 
		//		INNER JOIN Order ON Order_menu_item.order_id = Order.id
		//		INNER JOIN ingredient ON Order_menu_item.item_barcode = ingredient.item_barcode
		// WHERE Order_menu_item.is_finished = 1 AND Order.timestamp > fromDate AND Order.timestamp < toDate
		// GROUP BY ingredient.item_barcode) 
		// AS item_usage
		// 
		// (SELECT
		// 		item_barcode 
		//		SUM(quantity) as waste
		// FROM discrepancy
		// 		INNER JOIN stock_Taking ON discrepancy.session_id = Order.id
		// WHERE stock_Taking.timestamp > fromDate AND stock_Taking.timestamp < toDate
		// GROUP BY discrepancy.item_barcode)
		// AS item_waste
		//
		// SELECT
		//    	item_barcode AS barcode
		// 		name
		//		unit
		//		item_usage.usage AS usage
		//		item_waste.waste AS waste
		//		item_waste.waste/(item_usage.usage+item_waste.waste)*100 AS waste%
		// FROM item
		// 		LEFT JOIN item_usage ON item.barcode = item_usage.item_barcode
		// 		LEFT JOIN item_waste ON item.barcode = item_waste.item_barcode
		
		
		
		//if previous doesnt work
		// (SELECT 
		//		menu_item_id, 
		//		SUM(quantity) as quantity
		// FROM Order_menu_item 
		//		INNER JOIN Order ON Order_menu_item.order_id = Order.order_id
		// WHERE is_finished = 1 AND timestamp > fromDate AND timestamp < toDate
		// GROUP BY menu_item_id) 
		// AS menu_item_usage
		//
		// (SELECT 
		//		ingredient.item_barcode as barcode,
		//		SUM(ingredient.quantity*menu_item_usage.quantity) AS usage
		// FROM ingredient INNER JOIN menu_item_usage ON ingredient.menu_item_id = menu_item_count.menu_item_id
		// GROUP BY ingredient.item_barcode)
		// AS item_usage
		
		return new String[][] {{"151515","tomahto","kg","150","5","qik mafs"},{"666","potahto","l","9","0","0"}};
	}
	
	
	
}
