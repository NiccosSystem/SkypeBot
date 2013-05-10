package uk.niccossystem.skypebot.command;

import java.util.HashMap;

import uk.niccossystem.skypebot.SkypeBot;
import uk.niccossystem.skypebot.plugin.Plugin;

public class CommandSystem {
	private static HashMap<String, String> registeredCommands = new HashMap<String, String>();
	private static HashMap<String, Plugin> commandPluginRegister = new HashMap<String, Plugin>();
	
	public HashMap<String, String> getRegisteredCommands() {
		return registeredCommands;
	}
	
	public String getCommandHelpText(String command) {
		return registeredCommands.get(command);
	}
	
	public boolean registerCommand(Plugin plugin, String command, String helpText) {
		if (registeredCommands.containsKey(command)) {
			if (commandPluginRegister.containsKey(command)) {
				SkypeBot.log("Command \"" + command + "\" is already registered by plugin + " + plugin.getName() + "! Possible command conflict?");
				return false;
			}
			SkypeBot.log("Command \"" + command + "\" is in registeredCommands but not commandPluginRegister? WTF?");
		}
		registeredCommands.put(command, helpText);
		commandPluginRegister.put(command, plugin);
		SkypeBot.log("Registered command \"" + command + "\" by plugin \"" + plugin.getName() + "\"!");
		return true;
	}
}
