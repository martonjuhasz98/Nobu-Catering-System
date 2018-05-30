package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBCity;
import modlayer.City;

public class DBCity implements IFDBCity {

	private DBConnection dbCon;
	private Connection con;

	public DBCity() {
		dbCon = DBConnection.getInstance();
		con = dbCon.getConnection();
	}
	
	@Override
	public ArrayList<City> getCities() {
		ArrayList<City> cities = new ArrayList<>();
		
		String query = "SELECT * FROM [City]";
		try {
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			City city;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				city = buildCity(results);
				cities.add(city);
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("Citys were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return cities;
	}
	
	@Override
	public City selectCity(String zipCode) {
		City city = null;
		
		String query = "SELECT * FROM [City] WHERE zip_code = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, zipCode);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				city = buildCity(results);
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println("City was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return city;
	}
	
	private City buildCity(ResultSet results) throws SQLException {
		City city = null;
		
		String query = "";
		try {
			
			city = new City();
			city.setZipCode(results.getString("zip_code"));
			city.setName(results.getString("name"));
		}
		catch (SQLException e) {
			System.out.println("City was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return city;
	}
}
