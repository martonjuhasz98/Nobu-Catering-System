package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import dblayer.interfaces.IFDBUnit;
import modlayer.Unit;

public class DBUnit implements IFDBUnit {
	
	private Connection con;
	
	public DBUnit() {
		con = DBConnection.getConnection();
	}
	
	@Override
	public ArrayList<Unit> getUnits() {
		ArrayList<Unit> units = new ArrayList<>();
		
		String query = "SELECT * FROM [Unit]";
		try {
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			Unit unit;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				unit = buildUnit(results);
				units.add(unit);
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("Units were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return units;
	}
	@Override
	public Unit selectUnit(String abbr) {
		Unit unit = null;
		
		String query = "SELECT * FROM [Unit] WHERE abbreviation = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, abbr);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				unit = buildUnit(results);
			}
		} catch (SQLException e) {
			System.out.println("Unit was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return unit;
	}
	private Unit buildUnit(ResultSet results) throws SQLException {
		Unit unit = null;
		
		String query = "";
		try {
			
			unit = new Unit();
			unit.setAbbr(results.getString("abbreviation"));
			unit.setName(results.getString("name"));
		}
		catch (SQLException e) {
			System.out.println("Unit was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return unit;
	}
}
