package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBCity {

	public ArrayList<City> getCities();
    public City selectCity(String zipCode);
}