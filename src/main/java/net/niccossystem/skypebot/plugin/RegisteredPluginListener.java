package net.niccossystem.skypebot.plugin;

import net.niccossystem.skypebot.hook.Hook;
import net.niccossystem.skypebot.hook.HookDispatcher;

/**
 * Contains information about a PluginListener, like
 * its owner (Plugin) and its HookDispatcher
 * 
 * @author NiccosSystem
 * 
 */
public class RegisteredPluginListener {

    private final PluginListener listener;
    private final Plugin plugin;
    private final HookDispatcher hDispatcher;

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
