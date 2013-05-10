package uk.niccossystem.skypebot.listener;

import java.util.ArrayList;

import uk.niccossystem.skypebot.SkypeBot;
import uk.niccossystem.skypebot.hook.command.CommandHook;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

public class BotMessageListener implements ChatMessageListener {
	
	private ArrayList<ChatMessage> receivedCMessages = new ArrayList<ChatMessage>();
	
	@Override
	public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
		if (receivedCMessages.contains(arg0)) return;
		receivedCMessages.add(arg0);		
		handleMessage(arg0);
	}

	@Override
	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
		handleMessage(arg0);
	}
	
	public void handleMessage(ChatMessage cMessage) throws SkypeException {		
		String message = cMessage.getContent();		
		if (message.startsWith(SkypeBot.getSettingValue("commandPrefix"))) {
			CommandHook cmdHook = new CommandHook(cMessage);
			SkypeBot.hooks.callHook(cmdHook);
		}		
	}		
}
