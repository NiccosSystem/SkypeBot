package uk.niccossystem.skypebot.hook;

import uk.niccossystem.skypebot.plugin.PluginListener;

public abstract class HookDispatcher {
	public abstract void execute(PluginListener l, Hook h);
}