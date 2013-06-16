package uk.niccossystem.skypebot.command;

import java.util.HashMap;

import uk.niccossystem.skypebot.SkypeBot;

/**
 * A command to list all registered commands.
 * 
 * @author NiccosSystem
 *
 */
public class ListCommands implements CommandListener {
	
	private NativeCommands plugin;
	
	public ListCommands(NativeCommands instance) {
		plugin = instance;
	}
	
	@Command(help = "List all commands", name = "commands")
	public void list(CommandContainer cC) {
		HashMap<String, String> commands = SkypeBot.cmds().getCommands();
		String help = "Commands: (Command prefix is " + SkypeBot.getSettingValue("commandPrefix") + ")\n";
		for (String cmd : commands.keySet()) {
			help += cmd + " - " + commands.get(cmd) + "\n";
		}
		plugin.chat(cC.getChat(), help);
	}
}
