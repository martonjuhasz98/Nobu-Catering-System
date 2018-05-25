package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBEmployee {

    public ArrayList<Employee> getEmployees();
    public ArrayList<Employee> searchEmployees(String keyword);
    public Employee selectEmployee(String cpr);
    public Employee selectEmployee(String username,String password);
    public String insertEmployee(Employee employee);
    public boolean updateEmployee(Employee employee);
    public boolean deleteEmployee(Employee employee);
}