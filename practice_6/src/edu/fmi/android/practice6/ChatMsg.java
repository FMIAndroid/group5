package edu.fmi.android.practice6;

public class ChatMsg {
	public final String mNickname;
	public final String mMessage;
	public final boolean mIsOur;
	
	public ChatMsg(String nickname, String message,boolean isOur) {
		mNickname = nickname;
		mMessage = message;
		mIsOur = isOur;
	}
}
