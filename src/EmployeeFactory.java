public class EmployeeFactory {
    public static Employee createEmployee(int employeeId, String pps, String surname, String firstName, 
                                          char gender, String department, double salary, boolean fullTime) {
        return new Employee(employeeId, pps, surname, firstName, gender, department, salary, fullTime);
    }
}
