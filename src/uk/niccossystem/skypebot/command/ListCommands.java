package uk.niccossystem.skypebot.command;

import java.util.ArrayList;

import com.skype.Chat;
import com.skype.SkypeException;

import uk.niccossystem.skypebot.SkypeBot;
import uk.niccossystem.skypebot.hook.HookHandler;
import uk.niccossystem.skypebot.hook.command.CommandHook;
import uk.niccossystem.skypebot.plugin.PluginListener;

public class ListCommands implements PluginListener {
	@HookHandler
	public static void onCommand(CommandHook hook) {
		switch(hook.getCommand()) {
		case "listcommands":
			listCommands(hook.getChat());
		}
	}
	
	public static void listCommands(Chat chat) {
		String commandList = "Available commands:\n";
		ArrayList<String> commands = new ArrayList<String>(SkypeBot.cmdSystem.getRegisteredCommands().keySet());
		for (String command : commands) {
			commandList += command + ": " + SkypeBot.cmdSystem.getCommandHelpText(command) + "\n";
		}
		try {
			chat.send(commandList);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}
}
