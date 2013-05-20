package uk.niccossystem.skypebot.command;

import java.util.Date;

import uk.niccossystem.skypebot.SkypeBot;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

public class CommandContainer {
	private String[] parameters;
	private String senderDisplayName;
	private User sender;
	private Chat chat;
	private Date date;
	private ChatMessage message;
	private String command;
	
	public CommandContainer(ChatMessage cMsg) throws SkypeException {
		parameters = convertToParams(cMsg.getContent());
		senderDisplayName = cMsg.getSenderDisplayName();
		sender = cMsg.getSender();
		chat = cMsg.getChat();
		date = cMsg.getTime();
		message = cMsg;
		command = cMsg.getContent().split(" ")[0].trim().substring(SkypeBot.getSettingValue("commandPrefix").length(), cMsg.getContent().split(" ")[0].length());
	}
	
	private String[] convertToParams(String message) {
		if (message.split(" ").length == 1) return new String[] { "" };
		return message.substring(message.split(" ")[0].length() + 1).split(" ");
	}
	
	public String[] getParameters() {
		return parameters;
	}
	
	public String getSenderDisplayName() {
		return senderDisplayName;
	}
	
	public User getSender() {
		return sender;
	}
	
	public Chat getChat() {
		return chat;
	}
	
	public Date getTime() {
		return date;
	}
	
	public ChatMessage getChatMessage() {
		return message;
	}

	public String getCommand() {
		return command;
	}	
}
