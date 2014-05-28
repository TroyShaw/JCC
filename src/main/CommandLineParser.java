package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handles the parsing of command-line arguments, providing easy access to 
 * what the user specifies.
 * 
 * @author troy
 */
public class CommandLineParser {
	
	private String[] commands;
	
	private String outputFilename;
	private List<String> inputFilenames = new ArrayList<String>();
	
	private boolean wantsHelp;
	private boolean wantsVersion;
	
	//if true the user has given a command which allows JCC to run without compiling
	private boolean nonCompilationCommand;
	//if true the user has given a command which implies they want compilation to occur
	private boolean intendsCompilation;
	
	/**
	 * Instantiates a new CommandLineParser.
	 * @param commands
	 */
	public CommandLineParser(String[] commands) {
		this.commands = commands;
	}

	/**
	 * Parses the arguments.
	 */
	public void parseArguments() {
		for (int i = 0; i < commands.length; i++) {
			String c = commands[i];
			
			if (c.equals("-help")) {
				wantsHelp = true;
				
				nonCompilationCommand = true;
			} else if (c.equals("-v")) {
				wantsVersion = true;
				
				nonCompilationCommand = true;
			} else if (c.equals("-o")) {
				outputFilename = commands[i + 1];
				
				intendsCompilation = true;
			} else {
				//nothing specified, so must be an input file
				inputFilenames.add(c);
				
				intendsCompilation = true;
			}
		}
	}
	
	/**
	 * Returns the name the user wants the output file to be called.
	 * This value is null if the user doesn't specify anything.
	 * 
	 * @return
	 */
	public String getOutputFilename() {
		return outputFilename;
	}
	
	/**
	 * Returns true if the user specified any input files.
	 * 
	 * @return
	 */
	public boolean hasInputFiles() {
		return !inputFilenames.isEmpty();
	}
	
	/**
	 * The input files the user specifies.
	 * 
	 * @return
	 */
	public List<String> getInputFilenames() {
		return inputFilenames;
	}
	
	/**
	 * If the user wants JCC to display help.
	 * 
	 * @return
	 */
	public boolean wantsHelp() {
		return wantsHelp;
	}
	
	/**
	 * If the user wants JCC to display version information.
	 * 
	 * @return
	 */
	public boolean wantsVersion() {
		return wantsVersion;
	}
	
	/**
	 * Returns true if the user entered a command which implies they want
	 * compilation to occur.
	 * 
	 * @return
	 */
	public boolean intendsCompilation() {
		return intendsCompilation;
	}
	
	/**
	 * Returns true if the user entered a command which implied they didn't necessarily 
	 * want compilation to occur, e.g. -help, -v, etc.
	 * 
	 * @return
	 */
	public boolean nonCompilationCommandEntered() {
		return nonCompilationCommand;
	}
}
