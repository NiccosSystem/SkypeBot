package net.niccossystem.skypebot.plugin;

import java.net.URLClassLoader;
import net.visualillusionsent.utils.PropertiesFile;

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
    private PropertiesFile props;
    private String jarName = "";

    public abstract boolean enable();

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public String getJarName() {
        return jarName;
    }

    public String getAuthor() {
        return author();
    }

    public String getVersion() {
        return version();
    }

    public URLClassLoader getLoader() {
        return loader;
    }

    public PropertiesFile getPropertiesFile() {
        return props;
    }

    public void setLoader(URLClassLoader jarLoader, PropertiesFile propFile, String newJarName) {
        loader = jarLoader;
        props = propFile;
        jarName = newJarName;
    }
}
