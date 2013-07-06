package net.niccossystem.skypebot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.niccossystem.skypebot.command.CommandSystem;
import net.niccossystem.skypebot.command.NativeCommands;
import net.niccossystem.skypebot.hook.HookExecutor;
import net.niccossystem.skypebot.listener.BotCallListener;
import net.niccossystem.skypebot.listener.BotEditListener;
import net.niccossystem.skypebot.listener.BotMessageListener;
import net.niccossystem.skypebot.plugin.PluginLoader;
import net.visualillusionsent.utils.PropertiesFile;
import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;

/**
 * Main class for the SkypeBot, the heart of it all
 * 
 * @author NiccosSystem
 */
public class SkypeBot {

    private static final String author = "NiccosSystem";
    private static final String version = "ALPHA 0.1";

    private static final File pluginsFolder = new File("plugins/");
    private static final File configFolder = new File("config/");
    private static final File settingsFile = new File(SkypeBot.configFolder + "/settings.cfg");
    private static final String defaultPluginCFG = "SkypeBotPlugin.cfg";
    private static PropertiesFile settings;

    private static PluginLoader pLoader;
    private static HookExecutor hooks = new HookExecutor();
    private static CommandSystem cmdSystem = new CommandSystem();

    /**
     * The heart of it all.. Basically runs a few methods then loops forever :P
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        SkypeBot.log("Starting up SkypeBot version " + SkypeBot.version + ", created by " + SkypeBot.author);

        SkypeBot.checkForDefFilesAndFolders();
        new NativeCommands().enable();
        SkypeBot.handlePlugins();
        SkypeBot.registerSkype();

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
    public static PropertiesFile getSettingsFile() {
        return SkypeBot.settings;
    }

    /**
     * Get the default plugin cfg filename. (lolwat might remove this)
     * 
     * @return the default plugin cfg filename
     */
    public static String getDefaultPluginCFG() {
        return SkypeBot.defaultPluginCFG;
    }

    /**
     * Get a setting's value from settings.cfg
     * 
     * @param setting
     * @return the wanted setting's value
     */
    public static String getSettingValue(String setting) {
        return SkypeBot.settings.getString(setting);
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

        SkypeBot.settings = new PropertiesFile("config/settings.cfg");
        if (!SkypeBot.settingsFile.exists()) {
            SkypeBot.log("config/settings.cfg does not exist! Creating it...");
            SkypeBot.settings.setString("commandPrefix", "]");
            SkypeBot.settings.setString("skypeBotPrefix", "[SkypeBot]");
            SkypeBot.settings.save();
            SkypeBot.log("File config/settings.cfg created");
        }

        if (!SkypeBot.pluginsFolder.isDirectory()) {
            SkypeBot.log("plugins/ is not a directory! Creating it...");
            SkypeBot.pluginsFolder.mkdir();
            SkypeBot.log("Folder plugins/ created.");
        }
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
            c.send(SkypeBot.getSettingValue("skypeBotPrefix") + " " + m);
        }
        catch (SkypeException e) {
            e.printStackTrace();
        }
    }
}
