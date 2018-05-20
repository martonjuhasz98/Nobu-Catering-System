package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import modlayer.*;

public class UnitController {

	private DBUnit dbUnit;

	public UnitController() {
		dbUnit = new DBUnit();
	}

	public ArrayList<Unit> getUnits() {
		return dbUnit.getUnits();
	}
	public Unit getUnit(String abbr) {
		return dbUnit.selectUnit(abbr);
	}
}
