package main;

/**
 * Main entry point into JCC.
 * 
 * @author troy
 */
public class Main {

	private static CommandLineParser cmdParser;
	
	public static void main(String[] args) {
		cmdParser = new CommandLineParser(args);
		
		if (cmdParser.wantsHelp()) {
			System.out.println(Info.getHelp());
		}
		
		if (cmdParser.wantsVersion()) {
			System.out.println(Info.getVersion());
		}
		
		if (!cmdParser.intendsCompilation() && cmdParser.nonCompilationCommandEntered()) {
			//User just wanted information, so exit program
			System.exit(0);
		}
		
		if (cmdParser.intendsCompilation() && !cmdParser.hasInputFiles()) {
			//They wanted to compile but no files were given, error
			System.out.println("JCC: fatal error: no input files");
			System.exit(1);
		}
		
		//They want to compile and have some files, so compile
		compile();
	}
	
	/**
	 * Performs compilation using whatever arguments were specified.
	 */
	private static void compile() {
		
	}
}
