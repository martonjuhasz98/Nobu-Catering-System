package dblayer;

import java.util.ArrayList;

import dblayer.interfaces.IFDBUnit;
import modlayer.Unit;

public class DBUnit implements IFDBUnit {

	private ArrayList<Unit> units = new ArrayList<Unit>();
	
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
