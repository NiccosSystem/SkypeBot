package net.niccossystem.skypebot.hook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import net.niccossystem.skypebot.SkypeBot;
import net.niccossystem.skypebot.plugin.Plugin;
import net.niccossystem.skypebot.plugin.PluginListener;
import net.niccossystem.skypebot.plugin.RegisteredPluginListener;

/**
 * Handles calling of hooks and registering of listeners
 * 
 * @author NiccosSystem
 * 
 */
public class HookExecutor {

    HashMap<Class<? extends Hook>, ArrayList<RegisteredPluginListener>> listeners = new HashMap<Class<? extends Hook>, ArrayList<RegisteredPluginListener>>();

    public void registerListener(PluginListener l, Plugin p) {
        Method[] methods = l.getClass().getDeclaredMethods();

        for (final Method method : methods) {

            final HookHandler handler = method.getAnnotation(HookHandler.class);
            if (handler == null) {
                continue;
            }

            Class<?>[] methodParams = method.getParameterTypes();
            if (methodParams.length != 1) {
                SkypeBot.log("Amount of parameters from method " + method.getName() + " is not 1!");
                continue;
            }

            Class<?> parameterClass = methodParams[0];
            if (!Hook.class.isAssignableFrom(parameterClass)) {
                SkypeBot.log("Parameter Class " + methodParams[0].getSimpleName() + " is not a subclass of Hook");
                continue;
            }

            if (!listeners.containsKey(parameterClass)) {
                listeners.put(parameterClass.asSubclass(Hook.class), new ArrayList<RegisteredPluginListener>());
            }

            HookDispatcher d = new HookDispatcher() {

                @Override
                public void execute(PluginListener listener, Hook hook) {
                    try {
                        method.invoke(listener, hook);
                    }
                    catch (IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            };

            listeners.get(parameterClass.asSubclass(Hook.class)).add(new RegisteredPluginListener(l, p, d));
        }
    }

    public void callHook(Hook h) {
        ArrayList<RegisteredPluginListener> pListeners = listeners.get(h.getClass().asSubclass(Hook.class));
        if (pListeners != null) {
            for (RegisteredPluginListener rPL : pListeners) {
                rPL.execute(h);
            }
        }
    }
}
