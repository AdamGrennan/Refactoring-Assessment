
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

	    public FirstCommand(EmployeeDetails details) {
	        this.details = details;
	    }

	    public void execute() {
	        details.firstRecord();
	        details.updateView(); 
	    }
	}

class LastCommand implements Command {
	private EmployeeDetails details;

    public LastCommand(EmployeeDetails details) {
        this.details = details;
    }
	  public void execute() {
		  details.lastRecord();
		  details.updateView(); 
	  }
	}

class NextCommand implements Command {
	private EmployeeDetails details;

    public NextCommand(EmployeeDetails details) {
        this.details = details;
    }
	  public void execute() {
		  details.nextRecord();
		  details.updateView(); 
	  }
	}


class PreviousCommand implements Command {
	private EmployeeDetails details;

    public PreviousCommand(EmployeeDetails details) {
        this.details = details;
    }
	  public void execute() {
		  details.previousRecord();
		  details.updateView(); 
	  }
	}


