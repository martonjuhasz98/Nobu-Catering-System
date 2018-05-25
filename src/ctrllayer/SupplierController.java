package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import dblayer.interfaces.IFDBCity;
import dblayer.interfaces.IFDBSupplier;
import modlayer.*;

public class SupplierController {

	private IFDBSupplier dbSupplier;
	private IFDBCity dbCity;

	public SupplierController() {
		dbSupplier = new DBSupplier();
		dbCity = new DBCity();
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

	public boolean createSupplier(String cvr, String name, String address, String zipCode, String phone, String email) {
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

	public City getCity(String zipCode) {
		return dbCity.selectCity(zipCode);
	}

}
