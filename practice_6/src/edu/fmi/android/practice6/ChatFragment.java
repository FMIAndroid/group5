package edu.fmi.android.practice6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

@SuppressLint("NewApi") public class ChatFragment extends Fragment {

	private SharedPreferences mPreferences;
	private ListView mChat;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && intent.getAction().contentEquals("edu.fmi.android.chat.action.SEND")) {
				final String nickname = intent.getStringExtra("nickname");
				final String msg = intent.getStringExtra("msg");
				final String chatroom = intent.getStringExtra("chatroom");
				final String append = nickname + ": " + msg;

				if (chatroom != null && chatroom.contentEquals(mChatroom)) {
					mWriter.append(append + "\n");

					((ChatArrayAdaper)mChat.getAdapter()).add(new ChatMsg(nickname, msg, mPreferences.getString("nickname", "Guest").contentEquals(nickname)));
					((ChatArrayAdaper)mChat.getAdapter()).notifyDataSetChanged();
					mWriter.flush();
				}
			}
		}
	};

	private OutputStream mHistory;
	private PrintWriter mWriter;
	private BufferedReader mReader;

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mChatroom;

	private String mNickname;

	private ChatArrayAdaper mAdapter;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ChatFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ChatFragment newInstance(String param1, String param2) {
		ChatFragment fragment = new ChatFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public ChatFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mChatroom = getArguments().getString(ARG_PARAM1);
		}

		mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mPreferences.edit().putString("chatroom", mChatroom).commit();

		mNickname = mPreferences.getString("nickname", "Guest");

		try {
			File history;
			if (Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED)) {
				history = new File(getActivity().getExternalCacheDir().getAbsolutePath() + "/" + mChatroom + ".cht");
			} else {
				history = new File(getActivity().getCacheDir().getAbsolutePath()  + "/" + mChatroom + ".cht");
			}

			mHistory = new FileOutputStream(history, true);

			mWriter = new PrintWriter(mHistory);
			mReader = new BufferedReader(new FileReader(history));

			ArrayList<ChatMsg> historyList = new ArrayList<ChatMsg>();
			String line;

			while ((line = mReader.readLine()) != null) {
				final String nick = line.split(": ")[0];
				final String msg = line.split(": ")[1]; 

				historyList.add(new ChatMsg(nick, msg, nick.contentEquals(mNickname)));
			}

			mAdapter = new ChatArrayAdaper(getActivity(), 0, historyList);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (mReader != null) {
					mReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		getActivity().getActionBar().setSubtitle(mNickname + " @ " + mChatroom);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.activity_chat_room, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mChat = (ListView) getView().findViewById(R.id.chat);

		final Button mSendButton = (Button) getView().findViewById(R.id.send);
		final EditText mMsgField = (EditText) getView().findViewById(R.id.msg);
		mSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mMsgField.getText().toString())) {
					final String msg = mNickname + ": " + mMsgField.getText().toString();

					mWriter.append(msg + "\n");

					final String nick = msg.split(": ")[0];
					final String message = msg.split(": ")[1]; 

					((ChatArrayAdaper)mChat.getAdapter()).add(new ChatMsg(nick, message, nick.contentEquals(mNickname)));
					((ChatArrayAdaper)mChat.getAdapter()).notifyDataSetChanged();

					mWriter.flush();
					final Intent send = new Intent("edu.fmi.android.chat.action.SEND");
					send.putExtra("chatroom", mChatroom);
					getActivity().sendBroadcast(send);
				}
			}
		});

		mChat.setAdapter(mAdapter);
	}

	@Override
	public void onStart() {
		super.onStart();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, new IntentFilter("edu.fmi.android.chat.action.SEND"));
	}

	@Override
	public void onStop() {
		super.onStop();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
		if (mWriter != null) mWriter.close();
	}

}
