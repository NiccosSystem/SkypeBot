package uk.niccossystem.skypebot.hook.chat;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

import uk.niccossystem.skypebot.hook.Hook;

/**
 * A hook that contains information about a message.
 * Is called when a message is not a command.
 * 
 * @author NiccosSystem
 *
 */
public class ChatHook extends Hook {
	private Chat chat;
	private ChatMessage chatMessage;
	private String message;
	private User sender;
	private String senderDisplayName;
	
	public ChatHook(ChatMessage m) throws SkypeException {
		chat = m.getChat();
		chatMessage = m;
		message = m.getContent();
		sender = m.getSender();
		senderDisplayName = m.getSenderDisplayName();
	}
	
	/**
	 * Get the chat from where the message was sent from
	 * 
	 * @return the chat
	 */
	public Chat getChat() {
		return chat;
	}
	
	/**
	 * Get the ChatMessage object
	 * 
	 * @return the message
	 */
	public ChatMessage getChatMessage() {
		return chatMessage;
	}
	
	/**
	 * Get the message's content
	 * 
	 * @return the message content
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Set the message's content
	 * 
	 * @param msg New content of the message
	 */
	public void setMessage(String msg) {
		this.message = msg;
	}
	
	/**
	 * Get the user who sent the message
	 * 
	 * @return the sender
	 */
	public User getSender() {
		return sender;
	}
	
	/**
	 * Get the sender's display name
	 * 
	 * @return the sender's display name
	 */
	public String getSenderDisplayName() {
		return senderDisplayName;
	}
}
