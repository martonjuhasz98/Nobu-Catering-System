package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBUnit {

    public ArrayList<Unit> getUnits();
    public Unit selectUnit(String abbr);
}