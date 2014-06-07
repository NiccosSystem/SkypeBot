package net.niccossystem.skypebot.command;

import java.util.HashMap;
import java.util.List;

import net.niccossystem.skypebot.SkypeBot;
import net.niccossystem.skypebot.plugin.Plugin;

/**
 * Internal plugin for native commands
 * 
 * @author NiccosSystem
 * 
 */
public class NativeCommands extends Plugin implements CommandListener {

    @Override
    public String author() {
        return "Native";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public boolean enable() {
        SkypeBot.cmds().registerCommands(this, this);
        return false;
    }
    
    @Command(help = "List all commands", name = "commands")
    public void commands(CommandContainer cC) {
        HashMap<String, String> commands = SkypeBot.cmds().getCommands();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Commands: (Command prefix is %s)", SkypeBot.getSettingsFile().get("commandPrefix").getAsString()));
        for (String cmd : commands.keySet()) {
            sb.append(String.format("\n%s - %s", cmd, commands.get(cmd)));
        }
        SkypeBot.chat(cC.getChat(), sb.toString());
    }
    
    @Command(help = "List all plugins", name = "plugins")
    public void plugins(CommandContainer cC) {
    	StringBuilder pluginsSb = new StringBuilder();
    	List<Plugin> plugins = SkypeBot.getPluginLoader().getEnabledPlugins();
    	
    	boolean isFirst = true;
    	for (Plugin plugin : plugins) {
    		if (!isFirst) pluginsSb.append(", ");
    		pluginsSb.append(plugin.getName());
    		isFirst = false;
    	}
    	SkypeBot.chat(cC.getChat(), String.format("Plugins: %s", pluginsSb.toString()));
    }
    
    @Command(help = "Manage permissions", name = "permissions")
    public void permission(CommandContainer cC) {
    	String subCommand = cC.getParameters()[0];
    	
    	if (!SkypeBot.getPermissionsManager().hasPermission(cC.getSender().getId(), new String[] { String.format("permissions.%s", subCommand) })) {
    		SkypeBot.chat(cC.getChat(), String.format("%s: You do not have permission to execute this command", cC.getSenderDisplayName()));
    		return;
    	}
    	
    	if (subCommand.equalsIgnoreCase("add")) {
    		if (cC.getParameters().length != 3) {
    			SkypeBot.chat(cC.getChat(), String.format("%s: permissions add <skypeid> <permission>", cC.getSenderDisplayName()));
    			return;
    		}
    		String user = cC.getParameters()[1];
    		String permission = cC.getParameters()[2];
    		SkypeBot.getPermissionsManager().givePermission(user, permission);
    		SkypeBot.chat(cC.getChat(), String.format("%s: Gave user %s permission %s", cC.getSenderDisplayName(), user, permission));
    		return;
    	} 
    	else if (subCommand.equalsIgnoreCase("revoke")) {
    		if (cC.getParameters().length != 3) {
    			SkypeBot.chat(cC.getChat(), String.format("%s: permissions revoke <skypeid> <permission>", cC.getSenderDisplayName()));
    			return;
    		}
    		String user = cC.getParameters()[1];
    		String permission = cC.getParameters()[2];
    		SkypeBot.getPermissionsManager().revokePermission(user, permission);
    		SkypeBot.chat(cC.getChat(), String.format("%s: Revoked user %s of permission %s", cC.getSenderDisplayName(), user, permission));
    		return;
    	}
    	else if (subCommand.equalsIgnoreCase("list")) {
    		if (cC.getParameters().length != 2) {
    			SkypeBot.chat(cC.getChat(), String.format("%s: permissions list <skypeid>", cC.getSenderDisplayName()));
    			return;
    		}
    		String user = cC.getParameters()[1];
    		if (SkypeBot.getPermissionsManager().getPermissions(user) == null) {
    			SkypeBot.chat(cC.getChat(), String.format("%s: User %s does not exist, or does not have any permissions set.", cC.getSenderDisplayName(), user));
    			return;
    		}
    		StringBuilder sb = new StringBuilder();
    		boolean isFirst = true;
    		for (String permission : SkypeBot.getPermissionsManager().getPermissions(user)) {
    			if (!isFirst) sb.append(", ");
    			sb.append(permission);
    			isFirst = false;
    		}
    		SkypeBot.chat(cC.getChat(), String.format("%s: User %s has these permissions: %s", cC.getSenderDisplayName(), user, sb.toString()));
    		return;
    	}
    	
    	
    }
}
