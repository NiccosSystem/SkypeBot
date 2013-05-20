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
	 * Returns the command's Command annotation
	 * 
	 * @return
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
