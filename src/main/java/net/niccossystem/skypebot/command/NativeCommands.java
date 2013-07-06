package net.niccossystem.skypebot.command;

import net.niccossystem.skypebot.SkypeBot;
import net.niccossystem.skypebot.plugin.Plugin;

/**
 * Internal plugin for native commands
 * 
 * @author NiccosSystem
 * 
 */
public class NativeCommands extends Plugin {

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
        SkypeBot.cmds().registerCommands(this, new ListCommands());
        return false;
    }
}
