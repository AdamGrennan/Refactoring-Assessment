
/* * 
 * This is a menu driven system that will allow users to define a data structure representing a collection of 
 * records that can be displayed both by means of a dialog that can be scrolled through and by means of a table
 * to give an overall view of the collection contents.
 * 
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

public class EmployeeDetails extends JFrame implements ActionListener, ItemListener, DocumentListener, WindowListener {

	// decimal format for inactive currency text field
	// decimal format for active currency text field
	private static final DecimalFormat fieldFormat = new DecimalFormat("0.00");
	// hold object start position in file
	private long currentByteStart = 0;
	RandomFile application = RandomFile.getInstance();
	private boolean change = false;
	// holds true or false if any changes are made for file content
	boolean changesMade = false;
	private JMenuItem open, save, saveAs, create, modify, delete, firstItem, lastItem, nextItem, prevItem, searchById,
			searchBySurname, listAll, closeApp;
	private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname,
			saveChange, cancelChange;
	private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
	private static EmployeeDetails frame = new EmployeeDetails();
	// font for labels, text fields and combo boxes
	Font font1 = new Font("SansSerif", Font.BOLD, 16);
	// holds automatically generated file name
	String generatedFileName;
	// holds current Employee object
	Employee currentEmployee;
	JTextField searchByIdField, searchBySurnameField;
	// gender combo box values
	String[] gender = { "", "M", "F" };
	// department combo box values
	String[] department = { "", "Administration", "Production", "Transport", "Management" };
	// full time combo box values
	String[] fullTime = { "", "Yes", "No" };

	private List<Observer> observers = new ArrayList<Observer>();
	CommandInvoker invoker = new CommandInvoker();
	private FileManager fileManager;
	private File file;

	public EmployeeDetails() {
		 fileManager = new FileManager(this);
		 file = fileManager.getFile();
		
	}
	
	public void updateView() {
	    displayRecords(currentEmployee);
	}
	
	public Employee getCurrentEmployee() {
	    return currentEmployee;
	}
	
	public long getCurrentByteStart() {
	    return currentByteStart;
	}
	
	public String getIdFieldText() {
	    return idField.getText();
	}
	
	public boolean hasChanges() {
	    return change;  
	}


	public void attach(Observer observer){ observers.add(observer); }
	public void detach(Observer o){observers.remove(o);}
	public void notifyAllObservers(){
			for (Observer observer : observers) {
			observer.update(currentEmployee);
			
		}
		 
	}
	public void displayRecords(Employee thisEmployee) {
	    this.currentEmployee = thisEmployee;
		if (thisEmployee == null) {
		} else if (thisEmployee.getEmployeeId() == 0) {
		} else {
	    notifyAllObservers();
		}
	    change = false;
	}

	// initialize menu bar
	private JMenuBar menuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu, recordMenu, navigateMenu, closeMenu;

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		recordMenu = new JMenu("Records");
		recordMenu.setMnemonic(KeyEvent.VK_R);
		navigateMenu = new JMenu("Navigate");
		navigateMenu.setMnemonic(KeyEvent.VK_N);
		closeMenu = new JMenu("Exit");
		closeMenu.setMnemonic(KeyEvent.VK_E);

		menuBar.add(fileMenu);
		menuBar.add(recordMenu);
		menuBar.add(navigateMenu);
		menuBar.add(closeMenu);

		fileMenu.add(open = new JMenuItem("Open")).addActionListener(this);
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(save = new JMenuItem("Save")).addActionListener(this);
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(saveAs = new JMenuItem("Save As")).addActionListener(this);
		saveAs.setMnemonic(KeyEvent.VK_F2);
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));

		recordMenu.add(create = new JMenuItem("Create new Record")).addActionListener(this);
		create.setMnemonic(KeyEvent.VK_N);
		create.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		recordMenu.add(modify = new JMenuItem("Modify Record")).addActionListener(this);
		modify.setMnemonic(KeyEvent.VK_E);
		modify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		recordMenu.add(delete = new JMenuItem("Delete Record")).addActionListener(this);

		navigateMenu.add(firstItem = new JMenuItem("First"));
		firstItem.addActionListener(this);
		navigateMenu.add(prevItem = new JMenuItem("Previous"));
		prevItem.addActionListener(this);
		navigateMenu.add(nextItem = new JMenuItem("Next"));
		nextItem.addActionListener(this);
		navigateMenu.add(lastItem = new JMenuItem("Last"));
		lastItem.addActionListener(this);
		navigateMenu.addSeparator();
		navigateMenu.add(searchById = new JMenuItem("Search by ID")).addActionListener(this);
		navigateMenu.add(searchBySurname = new JMenuItem("Search by Surname")).addActionListener(this);
		navigateMenu.add(listAll = new JMenuItem("List all Records")).addActionListener(this);

		closeMenu.add(closeApp = new JMenuItem("Close")).addActionListener(this);
		closeApp.setMnemonic(KeyEvent.VK_F4);
		closeApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));

		return menuBar;
	}// end menuBar

	// initialize search panel
	private JPanel searchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout());

		searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		searchPanel.add(new JLabel("Search by ID:"), "growx, pushx");
		searchPanel.add(searchByIdField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchByIdField.addActionListener(this);
		searchByIdField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(searchId = new JButton("Go"),
				"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchId.addActionListener(this);
		searchId.setToolTipText("Search Employee By ID");

		searchPanel.add(new JLabel("Search by Surname:"), "growx, pushx");
		searchPanel.add(searchBySurnameField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchBySurnameField.addActionListener(this);
		searchBySurnameField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(
				searchSurname = new JButton("Go"),"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchSurname.addActionListener(this);
		searchSurname.setToolTipText("Search Employee By Surname");

		return searchPanel;
	}// end searchPanel

	// initialize navigation panel
	private JPanel navigPanel() {
		JPanel navigPanel = new JPanel();

		navigPanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
		navigPanel.add(first = new JButton(new ImageIcon(
				new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		first.setPreferredSize(new Dimension(17, 17));
		first.addActionListener(this);
		first.setToolTipText("Display first Record");

		navigPanel.add(previous = new JButton(new ImageIcon(new ImageIcon("prev.png").getImage()
				.getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		previous.setPreferredSize(new Dimension(17, 17));
		previous.addActionListener(this);
		previous.setToolTipText("Display next Record");

		navigPanel.add(next = new JButton(new ImageIcon(
				new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		next.setPreferredSize(new Dimension(17, 17));
		next.addActionListener(this);
		next.setToolTipText("Display previous Record");

		navigPanel.add(last = new JButton(new ImageIcon(
				new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		last.setPreferredSize(new Dimension(17, 17));
		last.addActionListener(this);
		last.setToolTipText("Display last Record");

		return navigPanel;
	}// end naviPanel

	private JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(add = new JButton("Add Record"), "growx, pushx");
		add.addActionListener(this);
		add.setToolTipText("Add new Employee Record");
		buttonPanel.add(edit = new JButton("Edit Record"), "growx, pushx");
		edit.addActionListener(this);
		edit.setToolTipText("Edit current Employee");
		buttonPanel.add(deleteButton = new JButton("Delete Record"), "growx, pushx, wrap");
		deleteButton.addActionListener(this);
		deleteButton.setToolTipText("Delete current Employee");
		buttonPanel.add(displayAll = new JButton("List all Records"), "growx, pushx");
		displayAll.addActionListener(this);
		displayAll.setToolTipText("List all Registered Employees");

		return buttonPanel;
	}

	// initialize main/details panel
	private JPanel detailsPanel() {
		JPanel empDetails = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();
		JTextField field;

		empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		empDetails.add(new JLabel("ID:"), "growx, pushx");
		empDetails.add(idField = new JTextField(20), "growx, pushx, wrap");
		idField.setEditable(false);

		empDetails.add(new JLabel("PPS Number:"), "growx, pushx");
		empDetails.add(ppsField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Surname:"), "growx, pushx");
		empDetails.add(surnameField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("First Name:"), "growx, pushx");
		empDetails.add(firstNameField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Gender:"), "growx, pushx");
		empDetails.add(genderCombo = new JComboBox<String>(gender), "growx, pushx, wrap");

		empDetails.add(new JLabel("Department:"), "growx, pushx");
		empDetails.add(departmentCombo = new JComboBox<String>(department), "growx, pushx, wrap");

		empDetails.add(new JLabel("Salary:"), "growx, pushx");
		empDetails.add(salaryField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Full Time:"), "growx, pushx");
		empDetails.add(fullTimeCombo = new JComboBox<String>(fullTime), "growx, pushx, wrap");

		buttonPanel.add(saveChange = new JButton("Save"));
		saveChange.addActionListener(this);
		saveChange.setVisible(false);
		saveChange.setToolTipText("Save changes");
		buttonPanel.add(cancelChange = new JButton("Cancel"));
		cancelChange.addActionListener(this);
		cancelChange.setVisible(false);
		cancelChange.setToolTipText("Cancel edit");

		empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");

		// loop through panel components and add listeners and format
		for (int i = 0; i < empDetails.getComponentCount(); i++) {
			empDetails.getComponent(i).setFont(font1);
			if (empDetails.getComponent(i) instanceof JTextField) {
				field = (JTextField) empDetails.getComponent(i);
				field.setEditable(false);
				if (field == ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
					field.setDocument(new JTextFieldLimit(20));
				field.getDocument().addDocumentListener(this);
			} // end if
			else if (empDetails.getComponent(i) instanceof JComboBox) {
				empDetails.getComponent(i).setBackground(Color.WHITE);
				empDetails.getComponent(i).setEnabled(false);
				((JComboBox<String>) empDetails.getComponent(i)).addItemListener(this);
				((JComboBox<String>) empDetails.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
					// set foregroung to combo boxes
					public void paint(Graphics g) {
						setForeground(new Color(65, 65, 65));
						super.paint(g);
					}// end paint
				});
			} // end else if
		} // end for
		EmployeeObserver observer = new EmployeeObserver(this, idField, ppsField, surnameField, 
                firstNameField, salaryField, genderCombo, 
                departmentCombo, fullTimeCombo);
		attach(observer);
		return empDetails;
		
	}// end detailsPanel
	

	private void displayEmployeeSummaryDialog() {
		// display Employee summary dialog if these is someone to display
		if (isSomeoneToDisplay())
			new EmployeeSummaryDialog(getAllEmloyees());
	}// end displaySummaryDialog

	// display search by ID dialog
	private void displaySearchByIdDialog() {
		if (isSomeoneToDisplay())
			new SearchByIdDialog(EmployeeDetails.this);
	}// end displaySearchByIdDialog

	// display search by surname dialog
	private void displaySearchBySurnameDialog() {
		if (isSomeoneToDisplay())
			new SearchBySurnameDialog(EmployeeDetails.this);
	}// end displaySearchBySurnameDialog

	// find byte start in file for first active record
	public void firstRecord() {
	    invoker.setCommand(new FirstCommand(this, file));
	    invoker.executeCommand();
	    notifyAllObservers();  
	}

	public void previousRecord() {
	    invoker.setCommand(new PreviousCommand(this, file));
	    invoker.executeCommand();
	    notifyAllObservers();
	}

	public void nextRecord() {
	    invoker.setCommand(new NextCommand(this, file));
	    invoker.executeCommand();
	    notifyAllObservers();
	}

	public void lastRecord() {
	    invoker.setCommand(new LastCommand(this, file));
	    invoker.executeCommand();
	    notifyAllObservers();
	}


	// search Employee by ID
	public void searchEmployeeById() {
		boolean found = false;

		try {// try to read correct correct from input
				// if any active Employee record search for ID else do nothing
			if (isSomeoneToDisplay()) {
				firstRecord();// look for first record
				int firstId = currentEmployee.getEmployeeId();
				// if ID to search is already displayed do nothing else loop
				// through records
				if (searchByIdField.getText().trim().equals(idField.getText().trim()))
					found = true;
				else if (searchByIdField.getText().trim().equals(Integer.toString(currentEmployee.getEmployeeId()))) {
					found = true;
					displayRecords(currentEmployee);
				} // end else if
				else {
					nextRecord();// look for next record
					// loop until Employee found or until all Employees have
					// been checked
					while (firstId != currentEmployee.getEmployeeId()) {
						// if found break from loop and display Employee details
						// else look for next record
						if (Integer.parseInt(searchByIdField.getText().trim()) == currentEmployee.getEmployeeId()) {
							found = true;
							displayRecords(currentEmployee);
							break;
						} else
							nextRecord();// look for next record
					} // end while
				} // end else
					// if Employee not found display message
				if (!found)
					JOptionPane.showMessageDialog(null, "Employee not found!");
			} // end if
		} // end try
		catch (NumberFormatException e) {
			searchByIdField.setBackground(new Color(255, 150, 150));
			JOptionPane.showMessageDialog(null, "Wrong ID format!");
		} // end catch
		searchByIdField.setBackground(Color.WHITE);
		searchByIdField.setText("");
	}// end searchEmployeeByID

	// search Employee by surname
	public void searchEmployeeBySurname() {
		boolean found = false;
		// if any active Employee record search for ID else do nothing
		if (isSomeoneToDisplay()) {
			firstRecord();// look for first record
			String firstSurname = currentEmployee.getSurname().trim();
			// if ID to search is already displayed do nothing else loop through
			// records
			if (searchBySurnameField.getText().trim().equalsIgnoreCase(surnameField.getText().trim()))
				found = true;
			else if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
				found = true;
				displayRecords(currentEmployee);
			} // end else if
			else {
				nextRecord();// look for next record
				// loop until Employee found or until all Employees have been
				// checked
				while (!firstSurname.trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
					// if found break from loop and display Employee details
					// else look for next record
					if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
						found = true;
						displayRecords(currentEmployee);
						break;
					} // end if
					else
						nextRecord();// look for next record
				} // end while
			} // end else
				// if Employee not found display message
			if (!found)
				JOptionPane.showMessageDialog(null, "Employee not found!");
		} // end if
		searchBySurnameField.setText("");
	}// end searchEmployeeBySurname

	// get next free ID from Employees in the file
	public int getNextFreeId() {
		int nextFreeId = 0;
		// if file is empty or all records are empty start with ID 1 else look
		// for last active record
		if (file.length() == 0 || !isSomeoneToDisplay())
			nextFreeId++;
		else {
			lastRecord();// look for last active record
			// add 1 to last active records ID to get next ID
			nextFreeId = currentEmployee.getEmployeeId() + 1;
		}
		return nextFreeId;
	}// end getNextFreeId

	// get values from text fields and create Employee object
	public Employee getChangedDetails() {
		boolean fullTime = false;
		Employee theEmployee;
		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;
		
		 String salaryText = salaryField.getText().replace("€", "").replace(",", "").trim();
		    double salary = Double.parseDouble(salaryText); 

		    theEmployee = EmployeeFactory.createEmployee(Integer.parseInt(idField.getText()), ppsField.getText().toUpperCase(),
				surnameField.getText().toUpperCase(), firstNameField.getText().toUpperCase(),
				genderCombo.getSelectedItem().toString().charAt(0), departmentCombo.getSelectedItem().toString(),
				salary, fullTime);

		return theEmployee;
	}// end getChangedDetails

	// add Employee object to fail
	public void addRecord(Employee newEmployee) {
		// open file for writing
		application.openWriteFile(file.getAbsolutePath());
		// write into a file
		currentByteStart = application.addRecords(newEmployee);
		application.closeWriteFile();// close file for writing
	}// end addRecord

	// delete (make inactive - empty) record from file
	private void deleteRecord() {
		if (isSomeoneToDisplay()) {// if any active record in file display
									// message and delete record
			int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to delete record?", "Delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			// if answer yes delete (make inactive - empty) record
			if (returnVal == JOptionPane.YES_OPTION) {
				// open file for writing
				application.openWriteFile(file.getAbsolutePath());
				// delete (make inactive - empty) record in file proper position
				application.deleteRecords(currentByteStart);
				application.closeWriteFile();// close file for writing
				// if any active record in file display next record
				if (isSomeoneToDisplay()) {
					nextRecord();// look for next record
					displayRecords(currentEmployee);
				} // end if
			} // end if
		} // end if
	}// end deleteDecord

	// create vector of vectors with all Employee details
	private Vector<Object> getAllEmloyees() {
		// vector of Employee objects
		Vector<Object> allEmployee = new Vector<Object>();
		Vector<Object> empDetails;// vector of each employee details
		long byteStart = currentByteStart;
		int firstId;

		firstRecord();// look for first record
		firstId = currentEmployee.getEmployeeId();
		// loop until all Employees are added to vector
		do {
			empDetails = new Vector<Object>();
			empDetails.addElement(new Integer(currentEmployee.getEmployeeId()));
			empDetails.addElement(currentEmployee.getPps());
			empDetails.addElement(currentEmployee.getSurname());
			empDetails.addElement(currentEmployee.getFirstName());
			empDetails.addElement(new Character(currentEmployee.getGender()));
			empDetails.addElement(currentEmployee.getDepartment());
			empDetails.addElement(new Double(currentEmployee.getSalary()));
			empDetails.addElement(new Boolean(currentEmployee.getFullTime()));

			allEmployee.addElement(empDetails);
			nextRecord();// look for next record
		} while (firstId != currentEmployee.getEmployeeId());// end do - while
		currentByteStart = byteStart;

		return allEmployee;
	}// end getAllEmployees

	// activate field for editing
	private void editDetails() {
		// activate field for editing if there is records to display
		if (isSomeoneToDisplay()) {
			// remove euro sign from salary text field
			salaryField.setText(fieldFormat.format(currentEmployee.getSalary()));
			change = false;
			setEnabled(true);// enable text fields for editing
		} // end if
	}// end editDetails

	// ignore changes and set text field unenabled
	private void cancelChange() {
		setEnabled(false);
		displayRecords(currentEmployee);
	}// end cancelChange

	// check if any of records in file is active - ID is not 0
	private boolean isSomeoneToDisplay() {
		  if (fileManager.getFile() == null || !file.exists()) {
		        JOptionPane.showMessageDialog(null, "No file selected or file does not exist!");
		        return false;
		    }
		boolean someoneToDisplay = false;
		// open file for reading
		application.openReadFile(file.getAbsolutePath());
		// check if any of records in file is active - ID is not 0
		someoneToDisplay = application.isSomeoneToDisplay();
		application.closeReadFile();// close file for reading
		// if no records found clear all text fields and display message
		if (!someoneToDisplay) {
			currentEmployee = null;
			idField.setText("");
			ppsField.setText("");
			surnameField.setText("");
			firstNameField.setText("");
			salaryField.setText("");
			genderCombo.setSelectedIndex(0);
			departmentCombo.setSelectedIndex(0);
			fullTimeCombo.setSelectedIndex(0);
			JOptionPane.showMessageDialog(null, "No Employees registered!");
		}
		return someoneToDisplay;
	}// end isSomeoneToDisplay
	

	// check for correct PPS format and look if PPS already in use
	public boolean correctPps(String pps, long currentByte) {
		boolean ppsExist = false;
		// check for correct PPS format based on assignment description
		if (pps.length() == 8 || pps.length() == 9) {
			if (Character.isDigit(pps.charAt(0)) && Character.isDigit(pps.charAt(1))
					&& Character.isDigit(pps.charAt(2))	&& Character.isDigit(pps.charAt(3)) 
					&& Character.isDigit(pps.charAt(4))	&& Character.isDigit(pps.charAt(5)) 
					&& Character.isDigit(pps.charAt(6))	&& Character.isLetter(pps.charAt(7))
					&& (pps.length() == 8 || Character.isLetter(pps.charAt(8)))) {
				// open file for reading
				application.openReadFile(file.getAbsolutePath());
				// look in file is PPS already in use
				ppsExist = application.isPpsExist(pps, currentByte);
				application.closeReadFile();// close file for reading
			} // end if
			else
				ppsExist = true;
		} // end if
		else
			ppsExist = true;

		return ppsExist;
	}// end correctPPS


	// check if any changes text field where made
	private boolean checkForChanges() {
		boolean anyChanges = false;
		// if changes where made, allow user to save there changes
		if (change) {
			saveChanges();// save changes
			anyChanges = true;
		} // end if
			// if no changes made, set text fields as unenabled and display
			// current Employee
		else {
			setEnabled(false);
			displayRecords(currentEmployee);
		} // end else

		return anyChanges;
	}// end checkForChanges

	// check for input in text fields
	private boolean checkInput() {
		boolean valid = true;
		// if any of inputs are in wrong format, colour text field and display
		// message
		if (ppsField.isEditable() && ppsField.getText().trim().isEmpty()) {
			ppsField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (ppsField.isEditable() && correctPps(ppsField.getText().trim(), currentByteStart)) {
			ppsField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (surnameField.isEditable() && surnameField.getText().trim().isEmpty()) {
			surnameField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (firstNameField.isEditable() && firstNameField.getText().trim().isEmpty()) {
			firstNameField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (genderCombo.getSelectedIndex() == 0 && genderCombo.isEnabled()) {
			genderCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (departmentCombo.getSelectedIndex() == 0 && departmentCombo.isEnabled()) {
			departmentCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		try {// try to get values from text field
			Double.parseDouble(salaryField.getText());
			// check if salary is greater than 0
			if (Double.parseDouble(salaryField.getText()) < 0) {
				salaryField.setBackground(new Color(255, 150, 150));
				valid = false;
			} // end if
		} // end try
		catch (NumberFormatException num) {
			if (salaryField.isEditable()) {
				salaryField.setBackground(new Color(255, 150, 150));
				valid = false;
			} // end if
		} // end catch
		if (fullTimeCombo.getSelectedIndex() == 0 && fullTimeCombo.isEnabled()) {
			fullTimeCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
			// display message if any input or format is wrong
		if (!valid)
			JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
		// set text field to white colour if text fields are editable
		if (ppsField.isEditable())
			setToWhite();

		return valid;
	}

	// set text field background colour to white
	private void setToWhite() {
		ppsField.setBackground(UIManager.getColor("TextField.background"));
		surnameField.setBackground(UIManager.getColor("TextField.background"));
		firstNameField.setBackground(UIManager.getColor("TextField.background"));
		salaryField.setBackground(UIManager.getColor("TextField.background"));
		genderCombo.setBackground(UIManager.getColor("TextField.background"));
		departmentCombo.setBackground(UIManager.getColor("TextField.background"));
		fullTimeCombo.setBackground(UIManager.getColor("TextField.background"));
	}// end setToWhite

	// enable text fields for editing
	public void setEnabled(boolean booleanValue) {
		boolean search;
		if (booleanValue)
			search = false;
		else
			search = true;
		ppsField.setEditable(booleanValue);
		surnameField.setEditable(booleanValue);
		firstNameField.setEditable(booleanValue);
		genderCombo.setEnabled(booleanValue);
		departmentCombo.setEnabled(booleanValue);
		salaryField.setEditable(booleanValue);
		fullTimeCombo.setEnabled(booleanValue);
		saveChange.setVisible(booleanValue);
		cancelChange.setVisible(booleanValue);
		searchByIdField.setEnabled(search);
		searchBySurnameField.setEnabled(search);
		searchId.setEnabled(search);
		searchSurname.setEnabled(search);
	}// end setEnabled


	// save changes to current Employee
	private void saveChanges() {
		int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes to current Employee?", "Save",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		// if user choose to save changes, save changes
		if (returnVal == JOptionPane.YES_OPTION) {
			// open file for writing
			application.openWriteFile(file.getAbsolutePath());
			// get changes for current Employee
			currentEmployee = getChangedDetails();
			// write changes to file for corresponding Employee record
			application.changeRecords(currentEmployee, currentByteStart);
			application.closeWriteFile();// close file for writing
			changesMade = false;// state that all changes has bee saved
		} // end if
		displayRecords(currentEmployee);
		setEnabled(false);
	}// end saveChanges



	public void actionPerformed(ActionEvent e) {
	    boolean validInput = checkInput() && !checkForChanges();

	    if (e.getSource() == closeApp) {
	        if (validInput) fileManager.exitApp();
	    } else if (e.getSource() == open) {
	        if (validInput) fileManager.openFile();
	    } else if (e.getSource() == save) {
	        if (validInput) fileManager.saveFile();
	        change = false;
	    } else if (e.getSource() == saveAs) {
	        if (validInput) fileManager.saveFileAs();
	        change = false;
	    } else if (e.getSource() == searchById) {
	        if (validInput) displaySearchByIdDialog();
	    } else if (e.getSource() == searchBySurname) {
	        if (validInput) displaySearchBySurnameDialog();
	    } else if (e.getSource() == searchId || e.getSource() == searchByIdField) {
	        searchEmployeeById();
	    } else if (e.getSource() == searchSurname || e.getSource() == searchBySurnameField) {
	        searchEmployeeBySurname();
	    } else if (e.getSource() == saveChange) {
	        if (validInput) saveChanges();
	    } else if (e.getSource() == cancelChange) {
	        cancelChange();
	    } else if (e.getSource() == firstItem || e.getSource() == first) {
	        firstRecord();
	    } else if (e.getSource() == prevItem || e.getSource() == previous) {
	        previousRecord();
	    } else if (e.getSource() == nextItem || e.getSource() == next) {
	       nextRecord();
	    } else if (e.getSource() == lastItem || e.getSource() == last) {
	        lastRecord();
	    } else if (e.getSource() == listAll || e.getSource() == displayAll) {
	        if (validInput && isSomeoneToDisplay()) {
	            displayEmployeeSummaryDialog();
	        }
	    } else if (e.getSource() == create || e.getSource() == add) {
	        if (validInput) new AddRecordDialog(EmployeeDetails.this);
	    } else if (e.getSource() == modify || e.getSource() == edit) {
	        if (validInput) editDetails();
	    } else if (e.getSource() == delete || e.getSource() == deleteButton) {
	        if (validInput) deleteRecord();
	    } else if (e.getSource() == searchBySurname) {
	        if (validInput) new SearchBySurnameDialog(EmployeeDetails.this);
	    }
	} 

	// content pane for main dialog
	private void createContentPane() {
		setTitle("Employee Details");
		fileManager.createRandomFile();// create random file name
		JPanel dialog = new JPanel(new MigLayout());

		setJMenuBar(menuBar());// add menu bar to frame
		// add search panel to frame
		dialog.add(searchPanel(), "width 400:400:400, growx, pushx");
		// add navigation panel to frame
		dialog.add(navigPanel(), "width 150:150:150, wrap");
		// add button panel to frame
		dialog.add(buttonPanel(), "growx, pushx, span 2,wrap");
		// add details panel to frame
		dialog.add(detailsPanel(), "gap top 30, gap left 150, center");

		JScrollPane scrollPane = new JScrollPane(dialog);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		addWindowListener(this);
	}// end createContentPane

	// create and show main dialog
	private static void createAndShowGUI() {

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.createContentPane();// add content pane to frame
		frame.setSize(760, 600);
		frame.setLocation(250, 200);
		frame.setVisible(true);
	}// end createAndShowGUI

	// main method
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}// end main

	// DocumentListener methods
	public void changedUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	public void insertUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	public void removeUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	// ItemListener method
	public void itemStateChanged(ItemEvent e) {
		change = true;
	}

	// WindowsListener methods
	public void windowClosing(WindowEvent e) {
		// exit application
		fileManager.exitApp();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public void setCurrentByteStart(long currentByteStart) {
		this.currentByteStart = currentByteStart;
		
	}
	
}// end class EmployeeDetails