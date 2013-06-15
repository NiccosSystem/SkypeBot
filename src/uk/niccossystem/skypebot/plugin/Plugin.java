package uk.niccossystem.skypebot.plugin;

import java.net.URLClassLoader;

import uk.niccossystem.skypebot.SkypeBot;

import com.skype.Chat;
import com.skype.SkypeException;

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
	
	public void chat(Chat c, String m) {
		try {
			c.send(SkypeBot.getSettingValue("skypeBotPrefix") + " " + m);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}
}