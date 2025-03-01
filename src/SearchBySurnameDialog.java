/*
 * 
 * This is a dialog for searching Employees by their surname.
 * 
 * */

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class SearchBySurnameDialog extends SearchDialog{
	EmployeeDetails parent;
	JButton search, cancel;
	JTextField searchField;

	public SearchBySurnameDialog(EmployeeDetails parent) {
		super(parent, "Surname", "Search by Surname");
	}
	
	@Override
	protected void search() {
		this.parent.searchBySurnameField.setText(searchField.getText());
		this.parent.searchEmployeeBySurname();
		dispose();
}
}