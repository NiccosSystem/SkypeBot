package uk.niccossystem.skypebot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.visualillusionsent.utils.PropertiesFile;

import uk.niccossystem.skypebot.command.CommandSystem;
import uk.niccossystem.skypebot.command.NativeCommands;
import uk.niccossystem.skypebot.hook.HookExecutor;
import uk.niccossystem.skypebot.listener.*;
import uk.niccossystem.skypebot.plugin.PluginLoader;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;

/**
 * Main class for the SkypeBot, the heart of it all
 * 
 * @author NiccosSystem
 */
public class SkypeBot {
	
	private static File configFolder;
	private static File settingsFile;
	private static final String author = "NiccosSystem";
	private static final String version = "ALPHA 0.1";
	private static PropertiesFile settings;
	private static File pluginsFolder;
	private static String defaultPluginCFG;
	private static PluginLoader pLoader;
	
	private static HookExecutor hooks;
	private static CommandSystem cmdSystem;
	
	/**
	 * The heart of it all.. Basically runs a few methods then loops forever :P
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {		
		log("Starting up SkypeBot version " + version + ", created by " + author);
		
		initializeVariables();
		checkForDefFilesAndFolders();
		new NativeCommands().enable();
		handlePlugins();
		registerSkype();
		
		while(true) {		
			Thread.sleep(20);
		}
	}
	
	/**
	 * Initialize all needed variables
	 *  
	 */
	private static void initializeVariables() {
		defaultPluginCFG = "SkypeBotPlugin.cfg";
		pluginsFolder = new File("plugins/");
		configFolder = new File("config/");
		settingsFile = new File(configFolder + "/settings.cfg");
		
		hooks = new HookExecutor();
		cmdSystem = new CommandSystem();
		
	}
	
	/**
	 * Get the bot's HookExecutor
	 * 
	 * @return the hook executor
	 */
	public static HookExecutor hooks() { 
		return hooks;
	}
	
	/**
	 * Get the bot's CommandSystem
	 * 
	 * @return the command system
	 */
	public static CommandSystem cmds() { 
		return cmdSystem; 
	}
	
	/**
	 * Get the bot's settings file (config/settings.cfg)
	 * 
	 * @return the settings file
	 */
	public static PropertiesFile getSettingsFile() {
		return settings;
	}
	
	/** 
	 * Get the default plugin cfg filename. (lolwat might remove this)
	 * 
	 * @return the default plugin cfg filename
	 */
	public static String getDefaultPluginCFG() {
		return defaultPluginCFG;
	}
	
	/**
	 * Get a setting's value from settings.cfg
	 * 
	 * @param setting
	 * @return the wanted setting's value
	 */
	public static String getSettingValue(String setting) {
		return settings.getString(setting);
	}
	
	/**
	 * Executes some methods to handle plugin loading and enabling
	 * 
	 */
	private static void handlePlugins() {
		pLoader = new PluginLoader();
		pLoader.scanForPlugins();
		pLoader.enableAllPlugins();
	}
	
	/**
	 * Log some text!
	 * 
	 * @param msg
	 */
	public static void log(String msg) {
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " " + msg);
	}
	
	/**
	 * Method with a stupid abbrevation for "Default" that checks for the 
	 * default files and folders like "plugins" and "config/settings.cfg" etc.
	 * 
	 */
	private static void checkForDefFilesAndFolders() {
		if (!configFolder.isDirectory()) {
			log("config/ is not a directory! Creating it...");
			configFolder.mkdir();
			log("Folder config/ created.");
		}
		
		settings = new PropertiesFile("config/settings.cfg");
		if (!settingsFile.exists()) {
			log("config/settings.cfg does not exist! Creating it...");
			settings.setString("commandPrefix", "]");
			settings.setString("skypeBotPrefix", "[SkypeBot]");
			settings.save();
			log("File config/settings.cfg created");
		}
		
		if (!pluginsFolder.isDirectory()) {
			log("plugins/ is not a directory! Creating it...");
			pluginsFolder.mkdir();
			log("Folder plugins/ created.");
		}
	}
	
	/**
	 * Get the plugins folder
	 * 
	 * @return the plugins folder
	 */
	public static File getPluginsFolder(){ 
		return pluginsFolder; 
	}
	
	/**
	 * Initializes the Skype event listeners
	 * 
	 */
	private static void registerSkype() {
		try {			
			Skype.addChatMessageListener(new BotMessageListener());
			log("Registered MessageListener!");
			
			Skype.addChatMessageEditListener(new BotEditListener());
			log("Registered EditListener!");
			
			Skype.addCallListener(new BotCallListener());
			log("Registered CallListener!");
		} catch (SkypeException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * The method plugins should use for chatting unless they absolutely need to
	 * chat directly.
	 * 
	 * @param c The Skype chat you want to send the message to
	 * @param m The message you want to send
	 */
	public static void chat(Chat c, String m) {
		try {
			c.send(SkypeBot.getSettingValue("skypeBotPrefix") + " " + m);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}
}
