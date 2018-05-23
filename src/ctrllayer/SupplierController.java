package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import modlayer.*;

public class SupplierController {

	private DBSupplier dbSupplier;

	public SupplierController() {
		dbSupplier = new DBSupplier();
	}

	// Suppliers
	public ArrayList<Supplier> getSuppliers() {
		return dbSupplier.getSuppliers();
	}

	public ArrayList<Supplier> searchSuppliers(String keyword) {
		return dbSupplier.searchSuppliers(keyword);
	}

	public Supplier getSupplier(String barcode) {
		return dbSupplier.selectSupplier(barcode);
	}

	public boolean createSupplier(String cvr, String name, String address, String zipCode, String phone ,String email) {
		Supplier supplier = new Supplier();
		supplier.setCvr(cvr);
		supplier.setName(name);
		supplier.setAddress(address);
		supplier.setCity(new City(zipCode));
		supplier.setPhone(phone);
		supplier.setEmail(email);
		
		boolean success = dbSupplier.insertSupplier(supplier) != null;
		
		return success;
	}

	public boolean updateSupplier(Supplier supplier) {
		return dbSupplier.updateSupplier(supplier);
	}

	public boolean deleteSupplier(Supplier supplier) {
		boolean success = dbSupplier.deleteSupplier(supplier);
		return success;
	}

}
