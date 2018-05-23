package dblayer;

import java.sql.Date; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBAnalytics;
import modlayer.Order;

public class DBAnalytics implements IFDBAnalytics {
	private Connection con;

	public DBAnalytics() {
		con = DBConnection.getConnection();
	}

	@Override
	public String[][] getSalesBbreakdown(Date from, Date to) {
		// TODO: select
//		 SELECT 
//				Menu_item.id as ID, 
//				Menu_item.name
//				Menu_item_category.name
//				SUM(Order_menu_item.quantity) as sold
//		 FROM Order_menu_item 
//				INNER JOIN Order ON Order_menu_item.order_id = Order.id
//				INNER JOIN Menu_item ON Order_menu_item.menu_item_id = Menu_item.id
//				INNER JOIN Menu_item_category ON Menu_item.category_id = Menu_item_category.id
//		 WHERE Order_menu_item.is_finished = 1 AND Order.timestamp > fromDate AND Order.timestamp < toDate
//		 GROUP BY ingredient.item_barcode
		//
		
		
		ArrayList<String[]> data = new ArrayList<String[]>();

		String query =
				  "		 SELECT \n" + 
				  "				Menu_item.id as ID, \n" + 
				  "				MIN(Menu_item.name) AS name,\n" + 
				  "				MIN(Menu_item_category.name) AS category,\n" + 
				  "				MIN(Menu_item.price) AS price,\n" + 
				  "				SUM(Order_menu_item.quantity) as sold\n" + 
				  "		 FROM Order_menu_item \n" + 
				  "				INNER JOIN [Order] ON Order_menu_item.order_id = [Order].id\n" + 
				  "				INNER JOIN Menu_item ON Order_menu_item.menu_item_id = Menu_item.id\n" + 
				  "				INNER JOIN Menu_item_category ON Menu_item.category_id = Menu_item_category.id\n" + 
				  "		 WHERE Order_menu_item.is_finished = 1 AND CAST([Order].timestamp AS DATE) >= ? AND CAST([Order].timestamp AS DATE) <= ? \n" + 
				  "		 GROUP BY Menu_item.id";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setDate(1, from);
			ps.setDate(2, to);
			
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				data.add(new String[] {
						results.getString("id"),
						results.getString("name"),
						results.getString("category"),
						results.getString("price"),
						results.getString("sold")
				});
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("SELECT error");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		String[][] cast = new String[data.size()][data.get(0).length];
		for(int i=0;i<data.size();i++)
			cast[i] = data.get(i);
		return cast;
//		return new String[][] {{"14-15-2018","420","9","8"}, {"27-19-2018","360","0","8"}};
	}

	@Override
	public String[][] getWaste(Date from, Date to) {
		// TODO Auto-generated method stub
		//
		// SELECT
		// item.barcode AS barcode,
		// item.name AS name,
		// item.unit AS unit,
		// item_usage.usage AS usage,
		// (item_waste.waste+item_usage.recipe_waste) AS waste,
		// ((item_usage.recipe_waste+item_waste.waste)/(item_usage.usage+item_waste.waste+item_usage.recipe_waste)*100)
		// AS [waste%]
		// FROM item
		// LEFT JOIN
		// (SELECT
		// ingredient.item_barcode,
		// SUM(ingredient.quantity*Menu_item_usage.usage) as usage,
		// SUM(ingredient.quantity*ingredient.waste/100) AS recipe_waste
		// FROM ingredient
		// INNER JOIN (SELECT
		// Order_menu_item.menu_item_id AS menu_item_id,
		// SUM(Order_menu_item.quantity) as usage
		// FROM Order_menu_item
		// INNER JOIN [Order] ON Order_menu_item.order_id = [Order].id
		// WHERE Order_menu_item.is_finished = 1 AND [Order].timestamp > GETDATE() AND
		// [Order].timestamp < GETDATE()
		// GROUP BY Order_menu_item.menu_item_id)
		// AS Menu_item_usage ON ingredient.menu_item_id = Menu_item_usage.menu_item_id
		// GROUP BY ingredient.item_barcode)
		// AS item_usage
		// ON item.barcode = item_usage.item_barcode
		// LEFT JOIN
		// (SELECT
		// item_barcode,
		// SUM(quantity) as waste
		// FROM discrepancy
		// INNER JOIN stocktaking ON discrepancy.stocktaking_id = stocktaking.id
		// WHERE stocktaking.timestamp > GETDATE() AND stocktaking.timestamp < GETDATE()
		// GROUP BY discrepancy.item_barcode)
		// AS item_waste ON item.barcode = item_waste.item_barcode

		ArrayList<String[]> data = new ArrayList<String[]>();

		String query = "		 SELECT\n" 
				+ "		    	item.barcode AS barcode,\n"
				+ "		 		item.name AS name,\n" 
				+ "				item.unit AS unit,\n"
				+ "				item_usage.usage AS usage,\n"
				+ "				(item_waste.waste+item_usage.recipe_waste) AS waste,\n"
				+ "				((item_usage.recipe_waste+item_waste.waste)/(item_usage.usage+item_waste.waste+item_usage.recipe_waste)*100) AS [waste%]\n"
				+ "		 FROM item\n" 
				+ "		 		LEFT JOIN 		 \n" 
				+ "				(SELECT \n"
				+ "				ingredient.item_barcode, \n"
				+ "				SUM(ingredient.quantity*Menu_item_usage.usage) as usage,\n"
				+ "		      SUM(ingredient.quantity*ingredient.waste/100) AS recipe_waste\n"
				+ "		 FROM ingredient \n" 
				+ "				INNER JOIN 		 (SELECT \n"
				+ "				Order_menu_item.menu_item_id AS menu_item_id, \n"
				+ "				SUM(Order_menu_item.quantity) as usage\n" 
				+ "		 FROM Order_menu_item \n"
				+ "				INNER JOIN [Order] ON Order_menu_item.order_id = [Order].id\n"
				+ "		 WHERE Order_menu_item.is_finished = 1 AND [Order].timestamp > GETDATE() AND [Order].timestamp < GETDATE()\n"
				+ "		 GROUP BY Order_menu_item.menu_item_id) \n"
				+ "		 AS Menu_item_usage ON ingredient.menu_item_id = Menu_item_usage.menu_item_id\n"
				+ "		 GROUP BY ingredient.item_barcode) \n" 
				+ "		 AS item_usage\n"
				+ "		  ON item.barcode = item_usage.item_barcode\n" 
				+ "		 		LEFT JOIN 		 \n"
				+ "		 (SELECT\n" 
				+ "		 		item_barcode,\n" 
				+ "				SUM(quantity) as waste\n"
				+ "		 FROM discrepancy\n"
				+ "		 		INNER JOIN stocktaking ON discrepancy.stocktaking_id = stocktaking.id\n"
				+ "		 WHERE stocktaking.timestamp > GETDATE() AND stocktaking.timestamp < GETDATE()\n"
				+ "		 GROUP BY discrepancy.item_barcode)\n"
				+ "		 AS item_waste ON item.barcode = item_waste.item_barcode";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setDate(1, from);
			ps.setDate(2, to);
			ps.setDate(3, from);
			ps.setDate(4, to);

			ResultSet results = ps.executeQuery();
			while (results.next()) {
				data.add(new String[] { results.getString("barcode"), results.getString("name"),
						results.getString("unit"), results.getString("usage"), results.getString("waste"),
						results.getString("waste%") });
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println("SELECT error");
			System.out.println(e.getMessage());
			System.out.println(query);
		}

		String[][] cast = new String[data.size()][data.get(0).length];
		for(int i=0;i<data.size();i++)
			cast[i] = data.get(i);
		return cast;
		// return new String[][] {{"151515","tomahto","kg","150","5","qik
		// mafs"},{"666","potahto","l","9","0","0"}};
	}

	public String[][] getSales(Date from, Date to) {
		// TODO: select
		// SELECT
		// MIN(CAST([Transaction].timestamp AS DATE)) as date,
		// SUM(case when amount >= 0 then amount else 0 end) as revenue,
		// SUM(case when amount < 0 then amount else 0 end) as costs,
		// SUM(amount) as profit
		// FROM [Transaction]
		// WHERE timestamp > GETDATE() AND timestamp < GETDATE()
		// GROUP BY CAST([Transaction].timestamp AS DATE)
		//

		ArrayList<String[]> data = new ArrayList<String[]>();

		String query = " 		 SELECT \n" + "				MIN(CAST([Transaction].timestamp AS DATE)) as date,\n"
				+ "				SUM(case when amount >= 0 then amount else 0 end) as revenue,\n"
				+ "				SUM(case when amount < 0 then amount else 0 end) as costs,\n"
				+ "				SUM(amount) as profit\n" + "		 FROM [Transaction] \n"
				+ "		 WHERE CAST(imestamp AS DATE) >= ? AND CAST(timestamp AS DATE) <= ? \n"
				+ "		 GROUP BY CAST([Transaction].timestamp AS DATE) ";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setDate(1, from);
			ps.setDate(2, to);

			ResultSet results = ps.executeQuery();
			while (results.next()) {
				data.add(new String[] { results.getString("date"), results.getString("revenue"),
						results.getString("costs"), results.getString("profit") });
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println("SELECT error");
			System.out.println(e.getMessage());
			System.out.println(query);
		}

		String[][] cast = new String[data.size()][data.get(0).length];
		for(int i=0;i<data.size();i++)
			cast[i] = data.get(i);
		return cast;
		// return new String[][] {{"14-15-2018","420","9","qik mafs"},
		// {"27-19-2018","360","0","qik mafs"}};
	}

	@Override
	public String[][] getWeeklyAverage() {
		// TODO: select
		// (SELECT
		// Order_menu_item.menu_item_id AS menu_item_id,
		// CAST(Order.timestamp AS DATE) AS date,
		// SUM(Order_menu_item.quantity) AS quantity
		// FROM Order_menu_item
		// INNER JOIN Order ON Order_menu_item.order_id = Order.id
		// WHERE Order_menu_item.is_finished = 1
		// GROUP BY Order_menu_item.menu_item_id, CAST(Order.timestamp AS DATE))
		// AS Menu_item_usage
		//
		// (SELECT
		// Ingredient.item_barcode AS barcode,
		// Menu_item_usage.date AS date,
		// SUM(Ingredient.quantity+Menu_item_usage.quantity) AS quantity
		// FROM Ingredient
		// INNER JOIN Menu_item_usage ON Ingredient.menu_item_id =
		// Menu_item_usage.menu_item_id
		// GROUP BY Ingredient.item_barcode, Menu_item_usage.date)
		// AS Ingredient_usage
		//
		// (SELECT
		// Discrepancy.item_barcode AS barcode,
		// CAST(Stock_taking.timestamp AS DATE) AS date,
		// SUM(Discrepancy.quantity) AS quantity
		// FROM Discrepancy
		// INNER JOIN Stock_taking ON Discrepancy.stock_tacking_id = Stock_taking.id
		// GROUP BY Discrepancy.item_barcode, CAST(Stock_taking.timestamp AS DATE))
		// AS Discrepancy_total
		//
		// (SELECT
		// Ingredient_usage.barcode AS barcode,
		// CASE WHEN Ingredient_usage.date IS NULL THEN Discrepancy_total.date ELSE
		// Ingredient_usage.date END AS date,
		// Ingredient_usage.quantity+Discrepancy_total.quantity AS quantity
		// FROM Ingredient_usage
		// FULL JOIN Discrepancy_total ON Ingredient_usage.barcode =
		// Discrepancy_total.barcode AND Ingredient_usage.date = Discrepancy_total.date
		// GROUP BY Ingredient_usage.item_barcode, Ingredient_usage.date)
		// AS Item_usage
		//
		// SELECT
		// item.barcode AS barcode,
		// item.name AS name,
		// item.unit AS unit,
		// AVG(Item_usage.quantity) AS Average
		// AVG(case when DATEPART(dw,Item_usage.date) = 2 then Item_usage.quantity else
		// NULL end) AS Monday
		// AVG(case when DATEPART(dw,Item_usage.date) = 3 then Item_usage.quantity else
		// NULL end) AS Tuesday
		// AVG(case when DATEPART(dw,Item_usage.date) = 4 then Item_usage.quantity else
		// NULL end) AS Wednesday
		// AVG(case when DATEPART(dw,Item_usage.date) = 5 then Item_usage.quantity else
		// NULL end) AS Thursday
		// AVG(case when DATEPART(dw,Item_usage.date) = 6 then Item_usage.quantity else
		// NULL end) AS Friday
		// AVG(case when DATEPART(dw,Item_usage.date) = 7 then Item_usage.quantity else
		// NULL end) AS Saturday
		// AVG(case when DATEPART(dw,Item_usage.date) = 1 then Item_usage.quantity else
		// NULL end) AS Sunday
		// FROM Item
		// LEFT JOIN Item_usage ON Item_usage.barcode = Item_usage.barcode
		// GROUP BY Item.barcode

		// SELECT
		// item.barcode AS barcode,
		// MIN(item.name) AS name,
		// MIN(item.unit) AS unit,
		// SUM(Item_usage.quantity)/MIN(weekday.total) AS Average,
		// SUM(case when DATEPART(dw,Item_usage.date) = 2 then Item_usage.quantity else
		// NULL end)/MIN(weekday.Monday) AS Monday,
		// SUM(case when DATEPART(dw,Item_usage.date) = 3 then Item_usage.quantity else
		// NULL end)/MIN(weekday.Tuesday) AS Tuesday,
		// SUM(case when DATEPART(dw,Item_usage.date) = 4 then Item_usage.quantity else
		// NULL end)/MIN(weekday.Wednesday) AS Wednesday,
		// SUM(case when DATEPART(dw,Item_usage.date) = 5 then Item_usage.quantity else
		// NULL end)/MIN(weekday.Thursday) AS Thursday,
		// SUM(case when DATEPART(dw,Item_usage.date) = 6 then Item_usage.quantity else
		// NULL end)/MIN(weekday.Friday) AS Friday,
		// SUM(case when DATEPART(dw,Item_usage.date) = 7 then Item_usage.quantity else
		// NULL end)/MIN(weekday.Saturday) AS Saturday,
		// SUM(case when DATEPART(dw,Item_usage.date) = 1 then Item_usage.quantity else
		// NULL end)/MIN(weekday.Sunday) AS Sunday
		// FROM
		// (SELECT
		// COUNT(d.date) AS total,
		// COUNT(case when DATEPART(dw,d.date) = 2 then 1 else NULL end) AS monday,
		// COUNT(case when DATEPART(dw,d.date) = 3 then 1 else NULL end) AS tuesday,
		// COUNT(case when DATEPART(dw,d.date) = 4 then 1 else NULL end) AS wednesday,
		// COUNT(case when DATEPART(dw,d.date) = 5 then 1 else NULL end) AS thursday,
		// COUNT(case when DATEPART(dw,d.date) = 6 then 1 else NULL end) AS friday,
		// COUNT(case when DATEPART(dw,d.date) = 7 then 1 else NULL end) AS saturday,
		// COUNT(case when DATEPART(dw,d.date) = 1 then 1 else NULL end) AS sunday
		// FROM
		// (SELECT
		// DISTINCT (CASE WHEN CAST([Order].timestamp AS DATE) IS NULL THEN
		// CAST(Stocktaking.timestamp AS DATE) ELSE CAST([Order].timestamp AS DATE) END)
		// AS date
		// FROM [Order]
		// FULL JOIN Stocktaking ON CAST([Order].timestamp AS DATE) =
		// CAST(Stocktaking.timestamp AS DATE)) as d) weekday, item
		//
		// LEFT JOIN
		// (SELECT
		// Ingredient_usage.barcode AS barcode,
		// CASE WHEN Ingredient_usage.date IS NULL THEN Discrepancy_total.date ELSE
		// Ingredient_usage.date END AS date,
		// Ingredient_usage.quantity+Discrepancy_total.quantity AS quantity
		// FROM
		// (SELECT
		// Ingredient.item_barcode AS barcode,
		// Menu_item_usage.date AS date,
		// SUM(Ingredient.quantity+Menu_item_usage.quantity) AS quantity
		// FROM Ingredient
		// INNER JOIN (SELECT
		// Order_menu_item.menu_item_id AS menu_item_id,
		// CAST([Order].timestamp AS DATE) AS date,/*CAST(Order.timestamp AS DATE) AS
		// date*/
		// SUM(Order_menu_item.quantity) AS quantity
		// FROM Order_menu_item
		// INNER JOIN [Order] ON Order_menu_item.order_id = [Order].id
		// WHERE Order_menu_item.is_finished = 1
		// GROUP BY [Order_menu_item].[menu_item_id], CAST([Order].timestamp AS DATE))
		// AS Menu_item_usage
		// ON Ingredient.menu_item_id = Menu_item_usage.menu_item_id
		// GROUP BY Ingredient.item_barcode, Menu_item_usage.date) AS Ingredient_usage
		// FULL JOIN
		// (SELECT
		// Discrepancy.item_barcode AS barcode,
		// CAST(Stocktaking.timestamp AS DATE) AS date,
		// SUM(Discrepancy.quantity) AS quantity
		// FROM Discrepancy
		// INNER JOIN Stocktaking ON Discrepancy.stocktaking_id = stocktaking.id
		// GROUP BY Discrepancy.item_barcode, CAST(stocktaking.timestamp AS DATE)) AS
		// discrepancy_total
		// ON Ingredient_usage.barcode = Discrepancy_total.barcode AND
		// Ingredient_usage.date = Discrepancy_total.date
		// ) AS Item_usage ON Item.barcode = Item_usage.barcode
		// GROUP BY Item.barcode
		ArrayList<String[]> data = new ArrayList<String[]>();

		String query = "	SELECT \n" 
				+ "		item.barcode AS barcode,\n" 
				+ "		MIN(item.name) AS name,\n"
				+ "		MIN(item.unit) AS unit,\n" 
				+ "		SUM(Item_usage.quantity)/MIN(weekday.total) AS Average,\n"
				+ "		SUM(case when DATEPART(dw,Item_usage.date) = 2 then Item_usage.quantity else NULL end)/MIN(weekday.Monday) AS Monday,\n"
				+ "		SUM(case when DATEPART(dw,Item_usage.date) = 3 then Item_usage.quantity else NULL end)/MIN(weekday.Tuesday) AS Tuesday,\n"
				+ "		SUM(case when DATEPART(dw,Item_usage.date) = 4 then Item_usage.quantity else NULL end)/MIN(weekday.Wednesday) AS Wednesday,\n"
				+ "		SUM(case when DATEPART(dw,Item_usage.date) = 5 then Item_usage.quantity else NULL end)/MIN(weekday.Thursday) AS Thursday,\n"
				+ "		SUM(case when DATEPART(dw,Item_usage.date) = 6 then Item_usage.quantity else NULL end)/MIN(weekday.Friday) AS Friday,\n"
				+ "		SUM(case when DATEPART(dw,Item_usage.date) = 7 then Item_usage.quantity else NULL end)/MIN(weekday.Saturday) AS Saturday,\n"
				+ "		SUM(case when DATEPART(dw,Item_usage.date) = 1 then Item_usage.quantity else NULL end)/MIN(weekday.Sunday) AS Sunday\n"
				+ "		FROM		\n" 
				+ "		(SELECT \n" 
				+ "			COUNT(d.date) AS total,\n"
				+ "			COUNT(case when DATEPART(dw,d.date) = 2 then 1 else NULL end) AS monday,\n"
				+ "			COUNT(case when DATEPART(dw,d.date) = 3 then 1 else NULL end) AS tuesday,\n"
				+ "			COUNT(case when DATEPART(dw,d.date) = 4 then 1 else NULL end) AS wednesday,\n"
				+ "			COUNT(case when DATEPART(dw,d.date) = 5 then 1 else NULL end) AS thursday,\n"
				+ "			COUNT(case when DATEPART(dw,d.date) = 6 then 1 else NULL end) AS friday,\n"
				+ "			COUNT(case when DATEPART(dw,d.date) = 7 then 1 else NULL end) AS saturday,\n"
				+ "			COUNT(case when DATEPART(dw,d.date) = 1 then 1 else NULL end) AS sunday\n" 
				+ "		FROM\n"
				+ "		 (SELECT\n"
				+ "	   DISTINCT (CASE WHEN CAST([Order].timestamp AS DATE) IS NULL THEN CAST(Stocktaking.timestamp AS DATE) ELSE CAST([Order].timestamp AS DATE) END) AS date\n"
				+ "	FROM [Order] \n"
				+ "	FULL JOIN Stocktaking ON CAST([Order].timestamp AS DATE) = CAST(Stocktaking.timestamp AS DATE)) as d) weekday, item\n"
				+ "	 \n" 
				+ "		LEFT JOIN 		 \n" 
				+ "		(SELECT \n"
				+ "				Ingredient_usage.barcode AS barcode, \n"
				+ "				CASE WHEN Ingredient_usage.date IS NULL THEN Discrepancy_total.date ELSE Ingredient_usage.date END AS date,\n"
				+ "				Ingredient_usage.quantity+Discrepancy_total.quantity AS quantity\n"
				+ "			FROM 		 \n" 
				+ "			(SELECT \n"
				+ "					Ingredient.item_barcode AS barcode, \n"
				+ "					Menu_item_usage.date AS date,\n"
				+ "					SUM(Ingredient.quantity+Menu_item_usage.quantity) AS quantity\n"
				+ "				FROM Ingredient \n" 
				+ "					INNER JOIN 		 (SELECT \n"
				+ "							Order_menu_item.menu_item_id AS menu_item_id, \n"
				+ "							CAST([Order].timestamp AS DATE) AS date,/*CAST(Order.timestamp AS DATE) AS date*/\n"
				+ "							SUM(Order_menu_item.quantity) AS quantity\n"
				+ "						FROM Order_menu_item \n"
				+ "							INNER JOIN [Order] ON Order_menu_item.order_id = [Order].id\n"
				+ "						WHERE Order_menu_item.is_finished = 1\n"
				+ "						GROUP BY [Order_menu_item].[menu_item_id], CAST([Order].timestamp AS DATE)) AS Menu_item_usage \n"
				+ "					ON Ingredient.menu_item_id = Menu_item_usage.menu_item_id\n"
				+ "				GROUP BY Ingredient.item_barcode, Menu_item_usage.date) AS Ingredient_usage \n"
				+ "				FULL JOIN 		 \n" 
				+ "				(SELECT \n"
				+ "						Discrepancy.item_barcode AS barcode, \n"
				+ "						CAST(Stocktaking.timestamp AS DATE) AS date,\n"
				+ "						SUM(Discrepancy.quantity) AS quantity\n"
				+ "					FROM Discrepancy \n"
				+ "						INNER JOIN Stocktaking ON Discrepancy.stocktaking_id = stocktaking.id\n"
				+ "					GROUP BY Discrepancy.item_barcode, CAST(stocktaking.timestamp AS DATE)) AS discrepancy_total \n"
				+ "				ON Ingredient_usage.barcode = Discrepancy_total.barcode AND Ingredient_usage.date = Discrepancy_total.date\n"
				+ "		) AS Item_usage ON Item.barcode = Item_usage.barcode \n" + "		GROUP BY Item.barcode";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);

			ResultSet results = ps.executeQuery();
			while (results.next()) {
				data.add(new String[] { results.getString("barcode"), results.getString("name"),
						results.getString("unit"), results.getString("Average"), results.getString("Monday"),
						results.getString("Tuesday"), results.getString("Wednesday"), results.getString("Thursday"),
						results.getString("Friday"), results.getString("Saturday"), results.getString("Sunday"), });
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println("SELECT error");
			System.out.println(e.getMessage());
			System.out.println(query);
		}

		String[][] cast = new String[data.size()][data.get(0).length];
		for(int i=0;i<data.size();i++)
			cast[i] = data.get(i);
		return cast;
	}

}
