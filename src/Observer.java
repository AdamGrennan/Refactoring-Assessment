import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public interface Observer {
	public void update(Employee employee);
}

class EmployeeDetailsObserver implements Observer {
    private EmployeeDetails employeeDetails;
    private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
	private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");

public EmployeeDetailsObserver(EmployeeDetails employeeDetails, JTextField idField, JTextField ppsField, JTextField surnameField, JTextField firstNameField,JTextField salaryField, 
            JComboBox<String> genderCombo, JComboBox<String> departmentCombo, JComboBox<String> fullTimeCombo) {
		
this.employeeDetails = employeeDetails;
this.idField = idField;
this.ppsField = ppsField;
this.surnameField = surnameField;
this.firstNameField = firstNameField;
this.salaryField = salaryField;
this.genderCombo = genderCombo;
this.departmentCombo = departmentCombo;
this.fullTimeCombo = fullTimeCombo;
}


    @Override
    public void update(Employee employee) {
        if (employee != null) {
        	idField.setText(Integer.toString(employee.getEmployeeId()));
            ppsField.setText(employee.getPps().trim());
        	surnameField.setText(employee.getSurname().trim());
        	firstNameField.setText(employee.getFirstName());

            int genderIndex = 0;
            int departmentIndex = 0;
            for (int i = 0; i < employeeDetails.gender.length; i++) {
                if (Character.toString(employee.getGender()).equalsIgnoreCase(employeeDetails.gender[i])) {
                    genderIndex = i;
                    break;
                }
            }

            for (int i = 0; i < employeeDetails.department.length; i++) {
                if (employee.getDepartment().trim().equalsIgnoreCase(employeeDetails.department[i])) {
                    departmentIndex = i;
                    break;
                }
            }

            genderCombo.setSelectedIndex(genderIndex);
            departmentCombo.setSelectedIndex(departmentIndex);
            salaryField.setText(format.format(employee.getSalary()));
            fullTimeCombo.setSelectedIndex(employee.getFullTime() ? 1 : 2);
        }
    }
}
