package uk.niccossystem.skypebot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import uk.niccossystem.skypebot.hook.HookExecutor;
import uk.niccossystem.skypebot.listener.*;

import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeBot {
	
	private static File configFolder;
	private static File settingsFile;
	private static Properties settingsProperties;
	private static final String author = "NiccosSystem";
	private static final String version = "ALPHA 0.1";
	private static HashMap<String, String> settings = new HashMap<String, String>();
	
	public static HookExecutor hooks = new HookExecutor();
	
	public static void main(String[] args) throws InterruptedException {
		
		log("Starting up SkypeBot version " + version + ", created by " + author);
		
		checkForConfig();
		storeSettings();
		registerSkype();
		
		while(true) {
			Thread.sleep(20);
		}
	}
	
	private static void storeSettings() {
		settings.put("commandPrefix", settingsProperties.getProperty("commandPrefix"));		
	}

	public static Properties getSettingsFile() {
		return settingsProperties;
	}
	
	public static String getSettingValue(String setting) {
		return settings.get(setting);
	}
	
	public static void log(String msg) {
		System.out.println(System.currentTimeMillis() + " " + msg);
	}
	
	private static void checkForConfig() {
		try {
			configFolder = new File("config/");
			if (!configFolder.isDirectory()) {
				log("config/ is not a directory! Creating it...");
				configFolder.mkdir();
				log("Folder config/ created.");
			}
			
			settingsFile = new File(configFolder + File.separator + "settings.cfg");
			if (!settingsFile.exists()) {
				log("config/settings.cfg does not exist! Creating it...");
				Properties props = new Properties();
				props.setProperty("commandPrefix", "]");
				props.store(new FileOutputStream(configFolder + File.separator + "settings.cfg"), null);
				log("File config/settings.cfg created");
			}
			settingsProperties = new Properties();
			settingsProperties.load(new FileInputStream(configFolder + File.separator + "settings.cfg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
}