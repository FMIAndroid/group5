package edu.fmi.android.practice6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class BotMessageReceiver extends BroadcastReceiver {
	public BotMessageReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final String chatroom;
		
		if (intent != null && intent.hasExtra("chatroom")) {
			chatroom = intent.getStringExtra("chatroom");
		} else {
			chatroom = null;
		}
		
		
		Intent send = new Intent("edu.fmi.android.chat.action.SEND");
		send.putExtra("nickname", "Botyo");
		send.putExtra("msg", "Hello World!");
		send.putExtra("chatroom", chatroom);
		
		LocalBroadcastManager.getInstance(context).sendBroadcast(send);
	}
}
