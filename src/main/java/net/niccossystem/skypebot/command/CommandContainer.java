package net.niccossystem.skypebot.command;

import java.util.Date;
import net.niccossystem.skypebot.SkypeBot;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

/**
 * Contains all information about a command.
 * This is the parameter that is invoked into methods annotated by Command.
 * 
 * @author NiccosSystem
 * 
 */
public class CommandContainer {

    private final String[] parameters;
    private final String senderDisplayName;
    private final User sender;
    private final Chat chat;
    private final Date date;
    private final ChatMessage message;
    private final String command;

    public CommandContainer(ChatMessage cMsg) throws SkypeException {
        parameters = convertToParams(cMsg.getContent());
        senderDisplayName = cMsg.getSenderDisplayName();
        sender = cMsg.getSender();
        chat = cMsg.getChat();
        date = cMsg.getTime();
        message = cMsg;
        command = cMsg.getContent().split(" ")[0].trim().substring(SkypeBot.getSettingValue("commandPrefix").length(), cMsg.getContent().split(" ")[0].length());
    }

    /**
     * Converts a String (really just the content of a ChaTMessage)
     * to parameters of a command
     * 
     * @param message
     *            The String you want to convert into params
     * @return
     */
    private String[] convertToParams(String message) {
        if (message.split(" ").length == 1) {
            return new String[] { "" };
        }
        return message.substring(message.split(" ")[0].length() + 1).split(" ");
    }

    /**
     * Get the command's parameters
     * (The command "test param1 and two" will return this array:
     * "param1", "and", "two"
     * 
     * @return the parameters of the command
     */
    public String[] getParameters() {
        return parameters;
    }

    /**
     * Get the sender's display name
     * 
     * @return the sender's display name
     */
    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    /**
     * Get the sender's User object
     * 
     * @return the sender
     */
    public User getSender() {
        return sender;
    }

    /**
     * Get the Chat from where the command was executed
     * 
     * @return the chat
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Get the time the message was sent
     * 
     * @return
     */
    public Date getTime() {
        return date;
    }

    /**
     * Get the ChatMessage object itself, should not be needed in most cases
     * 
     * @return the message which contained the command
     */
    public ChatMessage getChatMessage() {
        return message;
    }

    /**
     * Get the command, plugins really don't have any use of this.
     * (Command "test 1 2" will return "test")
     * 
     * @return the command
     */
    public String getCommand() {
        return command;
    }
}
