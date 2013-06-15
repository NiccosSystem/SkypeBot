package uk.niccossystem.skypebot.hook;

import uk.niccossystem.skypebot.plugin.PluginListener;

/**
 * Handles execution on hooks inside a PluginListener
 * 
 * @author NiccosSystem
 *
 */
public abstract class HookDispatcher {
	public abstract void execute(PluginListener l, Hook h);
}