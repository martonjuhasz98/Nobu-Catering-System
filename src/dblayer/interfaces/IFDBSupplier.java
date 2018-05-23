package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBSupplier {

    public ArrayList<Supplier> getSuppliers();
    public ArrayList<Supplier> searchSuppliers(String keyword);
    public Supplier selectSupplier(String cvr);
    public String insertSupplier(Supplier supplier);
    public boolean updateSupplier(Supplier supplier);
    public boolean deleteSupplier(Supplier supplier);
}