package uk.niccossystem.skypebot.plugin;

import uk.niccossystem.skypebot.hook.Hook;
import uk.niccossystem.skypebot.hook.HookDispatcher;

public class RegisteredPluginListener {
	
	private PluginListener listener;
	private Plugin plugin;
	private HookDispatcher hDispatcher;
	
	public RegisteredPluginListener(PluginListener l, Plugin p, HookDispatcher hd) {
		listener = l;
		plugin = p;
		hDispatcher = hd;
	}

	public PluginListener getListener() {
		return listener;
	}

	public Plugin getPlugin() {
		return plugin;
	}
	
	public void execute(Hook hook) {
		hDispatcher.execute(listener, hook);
	}
	
}