import java.io.File;

import javax.swing.JOptionPane;

public interface Command {
	void execute();
}

class Client {
	  Command c;  
	  void setCommand(Command c) {
	    this.c = c;  
	  }
}

class FirstCommand implements Command {
    private EmployeeDetails details;
    private File file;
    
	    public FirstCommand(EmployeeDetails details, File file) {
	        this.details = details;
	        this.file = file;
	    }
	    public void execute() {
	    	RandomFile application = RandomFile.getInstance();
	        application.openReadFile(file.getAbsolutePath());
	        long firstByteStart = application.getFirst();
	        Employee firstEmployee = application.readRecords(firstByteStart);
	        application.closeReadFile();
	        while (firstEmployee != null && firstEmployee.getEmployeeId() == 0) {
	            firstByteStart = application.getNext(firstByteStart);
	            firstEmployee = application.readRecords(firstByteStart);
	        }

	        details.displayRecords(firstEmployee);
	    }
	}



class LastCommand implements Command {
    private EmployeeDetails details;
    private File file;

    public LastCommand(EmployeeDetails details, File file) {
        this.details = details;
        this.file = file;
    }

    @Override
    public void execute() {
    	RandomFile application = RandomFile.getInstance();
        application.openReadFile(file.getAbsolutePath());

        long currentByteStart = application.getLast();
        Employee currentEmployee = application.readRecords(currentByteStart);

        application.closeReadFile();

        while (currentEmployee != null && currentEmployee.getEmployeeId() == 0) {
            currentByteStart = application.getPrevious(currentByteStart);
            currentEmployee = application.readRecords(currentByteStart);
        }

        details.displayRecords(currentEmployee);
    }
}

class NextCommand implements Command {
    private EmployeeDetails details;
    private File file;

    public NextCommand(EmployeeDetails details, File file) {
        this.details = details;
        this.file = file;
    }

    @Override
    public void execute() {
    	RandomFile application = RandomFile.getInstance();
    	 application.openReadFile(file.getAbsolutePath());

          long currentByteStart = details.getCurrentByteStart();
          currentByteStart = application.getNext(currentByteStart);
          Employee currentEmployee = application.readRecords(currentByteStart);

          while (currentEmployee != null && currentEmployee.getEmployeeId() == 0) {
              currentByteStart = application.getNext(currentByteStart);
              currentEmployee = application.readRecords(currentByteStart);
          }

          application.closeReadFile();

          details.setCurrentByteStart(currentByteStart);
          details.displayRecords(currentEmployee);
    }
}


class PreviousCommand implements Command {
    private EmployeeDetails details;
    private File file;

    public PreviousCommand(EmployeeDetails details, File file) {
        this.details = details;
        this.file = file;
    }

    @Override
    public void execute() {
    	RandomFile application = RandomFile.getInstance();
        application.openReadFile(file.getAbsolutePath());
        long currentByteStart = details.getCurrentByteStart();
        
        currentByteStart = application.getPrevious(currentByteStart);
        Employee currentEmployee = application.readRecords(currentByteStart);

        while (currentEmployee != null && currentEmployee.getEmployeeId() == 0) {
            currentByteStart = application.getPrevious(currentByteStart);
            currentEmployee = application.readRecords(currentByteStart);
        }

        application.closeReadFile();

        details.setCurrentByteStart(currentByteStart);
        details.displayRecords(currentEmployee);
    }
}


