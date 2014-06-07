package net.niccossystem.skypebot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.niccossystem.permission.PermissionsManager;
import net.niccossystem.skypebot.command.CommandSystem;
import net.niccossystem.skypebot.command.NativeCommands;
import net.niccossystem.skypebot.hook.HookExecutor;
import net.niccossystem.skypebot.listener.BotCallListener;
import net.niccossystem.skypebot.listener.BotEditListener;
import net.niccossystem.skypebot.listener.BotMessageListener;
import net.niccossystem.skypebot.plugin.PluginLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;

/**
 * Main class for the SkypeBot, the heart of it all
 * 
 * @author NiccosSystem
 */
public class SkypeBot {
	
	private static SkypeBot instance;

    private static final String author = "NiccosSystem";
    private static final String version = "ALPHA 0.1.2";

    private static File pluginsFolder;
    private static String pluginSettings;
    
    private static File configFolder;
    
    private static File settingsFile;
    private static JsonObject settings;
    
    private static File permissionsFile;
    private static JsonObject permissions;

    private static PluginLoader pLoader;
    private static HookExecutor hooks;
    private static CommandSystem cmdSystem;
    private static PermissionsManager permissionsManager;

    /**
     * The heart of it all.. Basically runs a few methods then loops forever :P
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
    	if(instance != null) {
    		log("An instance of SkypeBot is already running!");
    		return;
    	}
    	
    	
        SkypeBot.log("Starting up SkypeBot version " + SkypeBot.version + ", created by " + SkypeBot.author);
        
        pluginsFolder = new File("plugins/");
        configFolder = new File("config/");
        settingsFile = new File(String.format("%s/settings.json", configFolder));
        permissionsFile = new File(String.format("%s/permissions.json", configFolder));
        pluginSettings = "plugin.json";
        SkypeBot.checkForDefFilesAndFolders();
        SkypeBot.registerSkype();
        hooks = new HookExecutor();
        cmdSystem = new CommandSystem();
        permissionsManager = new PermissionsManager();
        permissionsManager.loadPermissionsFromFile();
        new NativeCommands().enable();
        SkypeBot.handlePlugins();

        while (true) {
            Thread.sleep(20);
        }
    }

    /**
     * Get the bot's HookExecutor
     * 
     * @return the hook executor
     */
    public static HookExecutor hooks() {
        return SkypeBot.hooks;
    }

    /**
     * Get the bot's CommandSystem
     * 
     * @return the command system
     */
    public static CommandSystem cmds() {
        return SkypeBot.cmdSystem;
    }

    /**
     * Get the bot's settings file (config/settings.cfg)
     * 
     * @return the settings file
     */
    public static JsonObject getSettingsFile() {
        return settings;
    }

    /**
     * Get the default plugin cfg filename. (lolwat might remove this)
     * 
     * @return the default plugin cfg filename
     */
    public static String getPluginSettings() {
        return pluginSettings;
    }

    /**
     * Executes some methods to handle plugin loading and enabling
     * 
     */
    private static void handlePlugins() {
        SkypeBot.pLoader = new PluginLoader();
        SkypeBot.pLoader.scanForPlugins();
        SkypeBot.pLoader.enableAllPlugins();
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
        if (!SkypeBot.configFolder.isDirectory()) {
            SkypeBot.log("config/ is not a directory! Creating it...");
            SkypeBot.configFolder.mkdir();
            SkypeBot.log("Folder config/ created.");
        }
        
        checkSettingsFile();
        
        if (!SkypeBot.pluginsFolder.isDirectory()) {
            SkypeBot.log("plugins/ is not a directory! Creating it...");
            SkypeBot.pluginsFolder.mkdir();
            SkypeBot.log("Folder plugins/ created.");
        }
    }
    
    private static void checkSettingsFile() {
    	JsonParser parser = new JsonParser();
        try {
			settings = parser.parse(new InputStreamReader(new FileInputStream(settingsFile))).getAsJsonObject();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
            log(String.format("%s does not exist! Creating it...", settingsFile));
			settings = new JsonObject();
            settings.addProperty("commandPrefix", "]");
            settings.addProperty("prefix", "[SkypeBot]");
            try {
				settingsFile.createNewFile();
				FileOutputStream file = new FileOutputStream(settingsFile);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String settingsJson = gson.toJson(settings);
				file.write(settingsJson.getBytes());
				file.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (JsonIOException e1) {
				e1.printStackTrace();
			}
            
            log(String.format("File %s created", settingsFile));
		}
    }
    
    public static void checkPermissionsFile() {
    	JsonParser parser = new JsonParser();
        try {
			permissions = parser.parse(new InputStreamReader(new FileInputStream(permissionsFile))).getAsJsonObject();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
            log(String.format("%s does not exist! Creating it...", permissionsFile));
            Map<String, List<String>> defaultPermissions = new HashMap<String, List<String>>();
            List<String> defaultUserPermissions = new ArrayList<String>();
            defaultUserPermissions.add("permissions.add");
            defaultUserPermissions.add("permissions.revoke");
            defaultUserPermissions.add("permissions.list");
            defaultPermissions.put("thelolzking", defaultUserPermissions);
            permissions = parser.parse(new Gson().toJson(defaultPermissions)).getAsJsonObject();
            //permissions = new JsonObject();
            try {
            	permissionsFile.createNewFile();
				savePermissionsFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (JsonIOException e1) {
				e1.printStackTrace();
			}
            
            log(String.format("File %s created", permissionsFile));
		}
    }
    
    public static void savePermissionsFile() throws IOException {
		FileOutputStream file = new FileOutputStream(permissionsFile);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String permissionsJson = gson.toJson(permissions);
		file.write(permissionsJson.getBytes());
		file.close();
    }

    /**
     * Get the plugins folder
     * 
     * @return the plugins folder
     */
    public static File getPluginsFolder() {
        return SkypeBot.pluginsFolder;
    }

    /**
     * Initializes the Skype event listeners
     * 
     */
    private static void registerSkype() {
        try {
            Skype.addChatMessageListener(new BotMessageListener());
            SkypeBot.log("Registered MessageListener!");

            Skype.addChatMessageEditListener(new BotEditListener());
            SkypeBot.log("Registered EditListener!");

            Skype.addCallListener(new BotCallListener());
            SkypeBot.log("Registered CallListener!");
        }
        catch (SkypeException e) {

            e.printStackTrace();
        }
    }

    /**
     * The method plugins should use for chatting unless they absolutely need to
     * chat directly.
     * 
     * @param c
     *            The Skype chat you want to send the message to
     * @param m
     *            The message you want to send
     */
    public static void chat(Chat c, String m) {
        try {
            c.send(String.format("%s %s", settings.get("prefix").getAsString(), m.trim()));
        }
        catch (SkypeException e) {
            e.printStackTrace();
        }
    }
    
    public static PluginLoader getPluginLoader() {
    	return pLoader;
    }

	public static JsonObject getPermissions() {
		return permissions;
	}
	
	public static void setPermissions(JsonObject permissions) {
		SkypeBot.permissions = permissions;
	}
	
	public static PermissionsManager getPermissionsManager() {
		return permissionsManager;
	}
}
