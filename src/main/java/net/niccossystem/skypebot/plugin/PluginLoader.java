package net.niccossystem.skypebot.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import net.niccossystem.skypebot.SkypeBot;
import net.visualillusionsent.utils.PropertiesFile;

/**
 * Loads plugins in the plugins folder.
 * 
 * @author NiccosSystem
 * 
 */
public class PluginLoader {

    private final ArrayList<Plugin> plugins;
    private final ArrayList<String> pluginsToLoad;
    private final HashMap<String, String> realJars;
    private final HashMap<String, URLClassLoader> loadList;

    public PluginLoader() {
        plugins = new ArrayList<Plugin>();
        realJars = new HashMap<String, String>();
        loadList = new HashMap<String, URLClassLoader>();
        pluginsToLoad = new ArrayList<String>();
    }

    public void scanForPlugins() {
        plugins.clear();
        File pluginsF = SkypeBot.getPluginsFolder();
        if (pluginsF == null) {
            return;
        }

        for (String fileName : pluginsF.list()) {
            if (!fileName.endsWith(".jar")) {
                continue;
            }
            if (!scanPlugin(fileName)) {
                continue;
            }
            if (!pluginsToLoad.contains(fileName)) {
                pluginsToLoad.add(fileName);
            }
            realJars.put(fileName.substring(0, fileName.lastIndexOf(".")), fileName);
        }
        loadPlugins();
    }

    public boolean loadPlugins() {
        for (String plugin : pluginsToLoad) {
            URLClassLoader loader = loadList.get(plugin);
            load(plugin, loader);
        }
        loadList.clear();
        return true;
    }

    public boolean scanPlugin(String jar) {
        File jarFile = new File("plugins/" + jar);

        if (!jarFile.isFile()) {
            return false;
        }
        try {
            URLClassLoader jarLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() });
            loadList.put(jar, jarLoader);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean load(String pluginName, URLClassLoader jar) {
        String mainClass = "";
        try {
            PropertiesFile pluginCfg = new PropertiesFile("plugins/" + pluginName, "SkypeBotPlugin.cfg");

            if (!pluginCfg.containsKey("mainClass")) {
                SkypeBot.log("No mainClass attribute in SkypeBotPlugin.cfg! Plugin: " + pluginName);
                return false;
            }
            mainClass = pluginCfg.getString("mainClass");

            if (getPlugin(mainClass) != null) {
                SkypeBot.log("Plugin " + pluginName + " already loaded!");
                return false;
            }

            Class<?> pluginClass = jar.loadClass(mainClass);
            Plugin plugin = (Plugin) pluginClass.newInstance();

            plugin.setLoader(jar, pluginCfg, pluginName);

            plugins.add(plugin);
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            SkypeBot.log("Something went wrong while activating plugin " + pluginName + "!");
            e.printStackTrace();
        }
        return true;
    }

    public boolean enablePlugin(String pluginName) {
        Plugin p = getPlugin(pluginName);
        if (p == null) {
            return false;
        }

        return enablePlugin(p);
    }

    private boolean enablePlugin(Plugin plugin) {
        if (plugin == null) {
            return false;
        }

        boolean enabled = false;
        try {
            File file = new File("plugins/" + plugin.getJarName());
            URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI().toURL() }, Thread.currentThread().getContextClassLoader());
            String pluginName = plugin.getJarName();
            PropertiesFile pluginConfig = new PropertiesFile("plugins/" + pluginName, SkypeBot.getDefaultPluginCFG());
            Class<?> cls = loader.loadClass(plugin.getClass().getName());
            plugin = (Plugin) cls.newInstance();
            plugin.setLoader(loader, pluginConfig, pluginName);
            enabled = plugin.enable();
        }
        catch (Throwable t) {
            SkypeBot.log("Exception enabling plugin " + plugin.getName() + ": " + t.getMessage());
            t.printStackTrace();
        }

        return enabled;
    }

    public void enableAllPlugins() {
        int count = 0;
        for (Plugin plugin : plugins) {
            if (enablePlugin(plugin)) {
                count++;
            }
        }
        SkypeBot.log("Enabled " + count + " plugins!");
    }

    private Plugin getPlugin(String pluginName) {
        for (Plugin plugin : plugins) {
            if (plugin.getName().equalsIgnoreCase(pluginName)) {
                return plugin;
            }
        }
        return null;
    }
}
