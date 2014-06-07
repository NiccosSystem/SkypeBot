package net.niccossystem.skypebot.plugin;

import java.net.URLClassLoader;

import com.google.gson.JsonObject;

/**
 * The base plugin class. Extend this in your main plugin class.
 * 
 * @author NiccosSystem
 * 
 */
public abstract class Plugin {

    public abstract String author();

    public abstract String version();

    private URLClassLoader loader;
    private JsonObject settings;
    private String jarName = "";

    public abstract boolean enable();

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public String getJarName() {
        return jarName;
    }

    public URLClassLoader getLoader() {
        return loader;
    }

    public JsonObject getSettings() {
        return settings;
    }

    public void setLoader(URLClassLoader jarLoader, JsonObject pluginSettings, String newJarName) {
        loader = jarLoader;
        settings = pluginSettings;
        jarName = newJarName;
    }
}
