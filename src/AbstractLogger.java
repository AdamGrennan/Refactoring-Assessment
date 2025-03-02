
public abstract class AbstractLogger {
	public static int INFO = 1; public static int DEBUG = 2;
	public static int ERROR = 3; protected int level;
	//next element in chain or responsibility
	protected AbstractLogger nextLogger;
	public void setNextLogger(AbstractLogger nextLogger){
	this.nextLogger = nextLogger;
	}
	public void logMessage(int level, String message){
	if(this.level <= level){
	write(message);
	}
	if(nextLogger != null){
	nextLogger.logMessage(level, message);
	}
	}
	
	abstract protected void write(String message);

	
	public class ConsoleLogger extends AbstractLogger {
		public ConsoleLogger(int level){ this.level = level; }
		protected void write(String message) {
		System.out.println("Standard Console::Logger: " + message);
		}
		
		}
	
		public class ErrorLogger extends AbstractLogger {
		public ErrorLogger(int level){ this.level = level; }
		protected void write(String message) {
		System.out.println("Standard Error::Logger: " + message);
		}

		}
		
		public class FileLogger extends AbstractLogger {
		public FileLogger(int level){ this.level = level; }
		protected void write(String message) {
		System.out.println("Standard File::Logger: " + message);
		}
		}
}
