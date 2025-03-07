import javax.swing.*;
import java.text.DecimalFormat;

public class EmployeeObserver implements Observer {
    private EmployeeDetails employeeDetails;
    private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
    private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");

    public EmployeeObserver(EmployeeDetails employeeDetails, JTextField idField, JTextField ppsField, JTextField surnameField,
                            JTextField firstNameField, JTextField salaryField, JComboBox<String> genderCombo, 
                            JComboBox<String> departmentCombo, JComboBox<String> fullTimeCombo) {
        this.employeeDetails = employeeDetails;
        this.idField = idField;
        this.ppsField = ppsField;
        this.surnameField = surnameField;
        this.firstNameField = firstNameField;
        this.salaryField = salaryField;
        this.genderCombo = genderCombo;
        this.departmentCombo = departmentCombo;
        this.fullTimeCombo = fullTimeCombo;

        this.employeeDetails.attach(this); 
    }

    @Override
    public void update(Employee employee) {
        if (employee != null) {
                idField.setText(Integer.toString(employee.getEmployeeId()));
                ppsField.setText(employee.getPps().trim());
                surnameField.setText(employee.getSurname().trim());
                firstNameField.setText(employee.getFirstName());
                salaryField.setText(format.format(employee.getSalary()));

                genderCombo.setSelectedItem(String.valueOf(employee.getGender()));
                departmentCombo.setSelectedItem(employee.getDepartment().trim());
                fullTimeCombo.setSelectedIndex(employee.getFullTime() ? 1 : 2);
         
        }
    }
}
