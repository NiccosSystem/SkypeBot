package net.niccossystem.skypebot.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import net.niccossystem.skypebot.SkypeBot;
import net.niccossystem.skypebot.plugin.Plugin;

/**
 * Handles execution of commands, bot-wide
 * 
 * @author NiccosSystem
 * 
 */
public class CommandSystem {

    private final HashMap<String, BotCommand> commands = new HashMap<String, BotCommand>();

    public void executeCommand(CommandContainer cContainer) {
        BotCommand cmd = null;
        for (String command : commands.keySet()) {
            if (command.equalsIgnoreCase(cContainer.getCommand())) {
                cmd = commands.get(command);
            }
        }
//        BotCommand cmd = commands.get(cContainer.getCommand());
        if (cmd == null) {
            commandNotFound(cContainer);
            return;
        }
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
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }

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
                    }
                    catch (IllegalAccessException | IllegalArgumentException
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
