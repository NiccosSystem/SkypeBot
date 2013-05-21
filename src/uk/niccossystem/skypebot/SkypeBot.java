package uk.niccossystem.skypebot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.visualillusionsent.utils.PropertiesFile;

import uk.niccossystem.skypebot.command.CommandSystem;
import uk.niccossystem.skypebot.command.ListCommands;
import uk.niccossystem.skypebot.command.NativeCommands;
import uk.niccossystem.skypebot.hook.HookExecutor;
import uk.niccossystem.skypebot.listener.*;
import uk.niccossystem.skypebot.plugin.Plugin;
import uk.niccossystem.skypebot.plugin.PluginLoader;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;

/**
 * Main class for the SkypeBot
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
	private static HashMap<String, ArrayList<ChatMessage>> userMessages; 
	private static String defaultPluginCFG;
	private static PluginLoader pLoader;
	private static String uniqueId;
	
	private static HookExecutor hooks;
	private static CommandSystem cmdSystem;
	
	public static void main(String[] args) throws InterruptedException {		
		log("Starting up SkypeBot version " + version + ", created by " + author);
		
		initializeVariables();
		fetchUniqueId();
		checkForDefFilesAndFolders();
		new NativeCommands().enable();
		handlePlugins();
		registerSkype();
		
		while(true) {		
			if (checkUniqueId()) return;
			Thread.sleep(60000);
		}
	}

	private static boolean checkUniqueId() {
		try {
			URL checkIfBanned = new URL("http://79.160.84.111/sbot/disabled.php?id=" + String.valueOf(getUniqueId()));
			BufferedReader bR = new BufferedReader(new InputStreamReader(checkIfBanned.openConnection().getInputStream()));
			String content = bR.readLine();
			log(content);
			if (content.equalsIgnoreCase("true")) return true; 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void fetchUniqueId() {
		InetAddress ip;
		String macAddress = "";
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface netw = NetworkInterface.getByInetAddress(ip);
			
			byte[] mac = netw.getHardwareAddress();
			for (byte b : mac) {
				macAddress += b;
			}
			
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		uniqueId = macAddress;
		log(macAddress);
	}

	private static void initializeVariables() {
		userMessages = new HashMap<String, ArrayList<ChatMessage>>();
		defaultPluginCFG = "SkypeBotPlugin.cfg";
		pluginsFolder = new File("plugins/");
		configFolder = new File("config/");
		settingsFile = new File(configFolder + "/settings.cfg");
		
		hooks = new HookExecutor();
		cmdSystem = new CommandSystem();
		
	}
	
	public static HookExecutor hooks() { return hooks; }
	
	public static CommandSystem cmds() { return cmdSystem; }
	
	public static String getUniqueId() { return uniqueId; }

	public static PropertiesFile getSettingsFile() {
		return settings;
	}
	
	public static String getDefaultPluginCFG() {
		return defaultPluginCFG;
	}
	
	public static String getSettingValue(String setting) {
		return settings.getString(setting);
	}
	
	private static void handlePlugins() {
		pLoader = new PluginLoader();
		pLoader.scanForPlugins();
		pLoader.enableAllPlugins();
	}
	
	public static HashMap<String, ArrayList<ChatMessage>> getUserMessages() {
		return userMessages;
	}
	
	public static void log(String msg) {
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " " + msg);
	}
	
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
	
	public static File getPluginsFolder(){ return pluginsFolder; }
	
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
	
	public static void chat(Chat c, String m) {
		try {
			c.send(SkypeBot.getSettingValue("skypeBotPrefix") + " " + m);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}
}