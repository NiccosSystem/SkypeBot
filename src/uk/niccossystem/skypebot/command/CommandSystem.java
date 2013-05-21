package uk.niccossystem.skypebot.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.skype.SkypeException;

import uk.niccossystem.skypebot.SkypeBot;
import uk.niccossystem.skypebot.plugin.Plugin;
import uk.niccossystem.skypebot.plugin.PluginListener;

/**
 * The command system.
 * 
 * @author NiccosSystem
 *
 */
public class CommandSystem {
	
	private HashMap<String, BotCommand> commands = new HashMap<String, BotCommand>();
	
	public void executeCommand(CommandContainer cContainer) {
		if (!commands.containsKey(cContainer.getCommand())) {
			commandNotFound(cContainer);
			return;
		}
		BotCommand cmd = commands.get(cContainer.getCommand());
		cmd.execute(cContainer);
		return;
	}
	
	private void commandNotFound(CommandContainer c) {
		SkypeBot.chat(c.getChat(), "Command not found! Do " + SkypeBot.getSettingValue("commandPrefix") + "commands to see a list of commands.");
		return;
	}

	/**
	 * Register a command. Returns false if it fails.
	 * 
	 * @param plugin
	 * @param listener
	 * @return
	 */
	public void registerCommands(Plugin plugin, final CommandListener listener) {
		Method[] methods = listener.getClass().getDeclaredMethods();
		
		for (final Method method : methods) {
			if (!method.isAnnotationPresent(Command.class)) continue;
			
			Command anno = method.getAnnotation(Command.class);
			Class<?>[] params = method.getParameterTypes();
			
			if (params.length != 1) {
				SkypeBot.log("Command method " + method.getName() + " in plugin " + plugin.getName() + " has too many/little parameters!");
				continue;
			}
			
			BotCommand cmd = new BotCommand(anno) {
				@Override
				public void execute(CommandContainer cmdContainer) {
					try {
						method.invoke(listener, cmdContainer);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}					
				}				
			};			
			commands.put(anno.name(), cmd);
		}
	}
	
	public HashMap<String, String> getCommands() {
		HashMap<String, String> cmds = new HashMap<String, String>();
		for (BotCommand b : commands.values()) {
			cmds.put(b.getCommandAnno().name(), b.getCommandAnno().help());
		}
		return cmds;
	}
}
