package uk.niccossystem.skypebot.listener;

import java.util.ArrayList;

import uk.niccossystem.skypebot.SkypeBot;
import uk.niccossystem.skypebot.hook.chat.ChatHook;
import uk.niccossystem.skypebot.command.CommandContainer;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

/**
 * Listens for messages and calls the respective hooks
 * 
 * @author NiccosSystem
 *
 */
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
		
		if (SkypeBot.getUserMessages().get(cMessage.getSenderId()) != null) {
			SkypeBot.getUserMessages().get(cMessage.getSenderId()).add(cMessage);
		} else {
			ArrayList<ChatMessage> initList = new ArrayList<ChatMessage>();
			initList.add(cMessage);
			SkypeBot.getUserMessages().put(cMessage.getSenderId(), initList);			
		}
		
		if (message.startsWith(SkypeBot.getSettingValue("commandPrefix"))) {
			CommandContainer cC = new CommandContainer(cMessage);
			SkypeBot.cmds().executeCommand(cC);
			return;
		}
		ChatHook cH = new ChatHook(cMessage);
		SkypeBot.hooks().callHook(cH);
	}	
}
