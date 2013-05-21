package uk.niccossystem.skypebot.command;

import uk.niccossystem.skypebot.SkypeBot;
import uk.niccossystem.skypebot.plugin.Plugin;

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
		SkypeBot.cmds().registerCommands(this, new ListCommands(this));
		return false;
	}
}
