package net.niccossystem.skypebot.command;

import java.util.HashMap;
import net.niccossystem.skypebot.SkypeBot;

/**
 * A command to list all registered commands.
 * 
 * @author NiccosSystem
 * 
 */
public class ListCommands implements CommandListener {

    @Command(help = "List all commands", name = "commands")
    public void list(CommandContainer cC) {
        HashMap<String, String> commands = SkypeBot.cmds().getCommands();
        String help = "Commands: (Command prefix is " + SkypeBot.getSettingValue("commandPrefix") + ")\n";
        for (String cmd : commands.keySet()) {
            help += cmd + " - " + commands.get(cmd) + "\n";
        }
        SkypeBot.chat(cC.getChat(), help);
    }
}
