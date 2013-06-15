package uk.niccossystem.skypebot.command;

/**
 * A class to contain info about a command
 * 
 * @author NiccosSystem
 *
 */
public abstract class BotCommand {
	private Command commandAnno;
	
	public BotCommand(Command anno) {
		commandAnno = anno;
	}
	
	/**
	 * The Command annotation of the command.
	 * 
	 * @return the command's Command annotation
	 */
	public Command getCommandAnno() {
		return commandAnno;
	}
	
	/**
	 * Executes the command
	 * 
	 * @param cmdContainer
	 */
	public abstract void execute(CommandContainer cmdContainer);
}
