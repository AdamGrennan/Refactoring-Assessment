public class EmployeeFactory {
	
    public static Employee createEmployee(int id, String pps, String surname, String firstName, char gender, 
                                          String department, double salary, boolean fullTime) {
    	
        return new Employee(id, pps, surname, firstName, gender, department, salary, fullTime);
    }
}
