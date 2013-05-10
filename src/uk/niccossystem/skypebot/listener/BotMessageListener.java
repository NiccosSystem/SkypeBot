package uk.niccossystem.skypebot.listener;

import java.util.ArrayList;

import uk.niccossystem.skypebot.SkypeBot;
import uk.niccossystem.skypebot.hook.command.CommandHook;
import uk.niccossystem.skypebot.command.ListCommands;

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
		
		if (SkypeBot.getUserMessages().get(cMessage.getSenderId()) != null) {
			SkypeBot.getUserMessages().get(cMessage.getSenderId()).add(cMessage);
		} else {
			ArrayList<ChatMessage> initList = new ArrayList<ChatMessage>();
			initList.add(cMessage);
			SkypeBot.getUserMessages().put(cMessage.getSenderId(), initList);			
		}		
		
		if (message.startsWith(SkypeBot.getSettingValue("commandPrefix"))) {
			CommandHook cmdHook = new CommandHook(cMessage);
			if (callNativeCommands(cmdHook)) return;
			SkypeBot.hooks.callHook(cmdHook);
		}		
	}

	private boolean callNativeCommands(CommandHook cmdHook) {
		switch (cmdHook.getCommand()) {
		case "listcommands":
			ListCommands.onCommand(cmdHook);
			return true;
		default:
			break;
		}
		return false;
	}		
}
