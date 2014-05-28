package main;

/**
 * Class contains methods to display information to the user about JCC.
 * The help and version information is supplied by this class.
 * 
 * @author troy
 */
public class Info {

	private Info() {
		// Stop instantiation
	}
	
	/**
	 * Gets the version information.
	 * 
	 * @return
	 */
	public static String getVersion() {
		return "JCC version 0.0.1";
	}

	/**
	 * Gets the help information.
	 * 
	 * @return
	 */
	public static String getHelp() {
		String help = "";
		
		help += "-o        specify output file\n";
		help += "-v        display the version information\n";
		help += "-help     display this help information\n";
		help += "\n";
		help += "any other arguments not associated with an option is considered a file input";
		
		return help;
	}
}
