package uk.niccossystem.skypebot.hook.chat;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

import uk.niccossystem.skypebot.hook.Hook;

public class ChatHook extends Hook {
	private Chat chat;
	private ChatMessage message;
	private String message2;
	private User sender;
	private String senderName;
	
	public ChatHook(ChatMessage m) throws SkypeException {
		setChat(m.getChat());
		setMessage(m);
		setMessage2(m.getContent());
		setSender(m.getSender());
		setSenderName(m.getSenderDisplayName());
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public ChatMessage getMessage() {
		return message;
	}

	public void setMessage(ChatMessage message) {
		this.message = message;
	}

	public String getMessage2() {
		return message2;
	}

	public void setMessage2(String message2) {
		this.message2 = message2;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	
}
