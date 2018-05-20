package dblayer;

import java.util.ArrayList;
import java.util.Arrays;

import dblayer.interfaces.IFDBUnit;
import modlayer.Unit;

public class DBUnit implements IFDBUnit {

	private ArrayList<Unit> units = new ArrayList<Unit>(Arrays.asList(
			new Unit("g", "gramm"),
			new Unit("dkg", "dekagramm"),
			new Unit("kg", "kilogramm"),
			new Unit("ml", "mililiter"),
			new Unit("cl", "centiliter"),
			new Unit("dl", "deciliter"),
			new Unit("l", "liter"),
			new Unit("pcs", "pieces")
	));
	
	@Override
	public ArrayList<Unit> getUnits() {
		// TODO Auto-generated method stub
		return units;
	}
	@Override
	public Unit selectUnit(String abbr) {
		// TODO Auto-generated method stub
		Unit result = null;
		
		for (Unit unit : units) {
			if (unit.getAbbr().equals(abbr)) {
				result = unit;
			}
		}
		
		return result;
	}
}
