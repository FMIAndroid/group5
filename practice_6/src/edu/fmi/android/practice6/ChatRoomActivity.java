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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChatRoomActivity extends Activity {

	public static int LOGIN_REQUEST = 666;

	private SharedPreferences mPreferences;
	private ListView mChat;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && intent.getAction().contentEquals("edu.fmi.android.chat.action.SEND")) {
				final String nickname = intent.getStringExtra("nickname");
				final String msg = intent.getStringExtra("msg");
				final String append = nickname + ": " + msg;

				mWriter.append(append);
				((ArrayAdapter<String>)mChat.getAdapter()).add(append);
				((ArrayAdapter<String>)mChat.getAdapter()).notifyDataSetChanged();
				mWriter.flush();
			}
		}
	};

	private OutputStream mHistory;
	private PrintWriter mWriter;
	private BufferedReader mReader;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);

		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mChat = (ListView) findViewById(R.id.chat);

		final String nickname = mPreferences.getString("nickname", "Guest");
		String chatroom = mPreferences.getString("chatroom", null);

		if (chatroom == null) {
			Intent login = new Intent(this, LoginActivity.class);
			startActivityForResult(login, LOGIN_REQUEST);
		} else {

			try {
				File history;
				if (Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED)) {
					history = new File(getExternalCacheDir().getAbsolutePath() + "/" + chatroom + ".cht");
				} else {
					history = new File(getCacheDir().getAbsolutePath()  + "/" + chatroom + ".cht");
				}

				System.out.println(history.getAbsolutePath());
				System.out.println("Exist: " + history.exists());

				mHistory = new FileOutputStream(history, true);

				mWriter = new PrintWriter(mHistory);
				mReader = new BufferedReader(new FileReader(history));

				ArrayList<String> historyList = new ArrayList<String>();
				String line;
				
				while ((line = mReader.readLine()) != null) {
					System.out.println(line);
					historyList.add(line);
				}
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historyList);
				mChat.setAdapter(adapter);
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

			getActionBar().setSubtitle(nickname + " @ " + chatroom);

		}
		final Button mSendButton = (Button) findViewById(R.id.send);
		final EditText mMsgField = (EditText) findViewById(R.id.msg);
		mSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mMsgField.getText().toString())) {
					final String msg = nickname + ": " + mMsgField.getText().toString();

					mWriter.append(msg);
					((ArrayAdapter<String>)mChat.getAdapter()).add(msg);
					((ArrayAdapter<String>)mChat.getAdapter()).notifyDataSetChanged();

					mWriter.flush();
					sendBroadcast(new Intent("edu.fmi.android.chat.action.SEND"));
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("edu.fmi.android.chat.action.SEND"));
	}

	@Override
	protected void onStop() {
		super.onStop();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
		if (mWriter != null) mWriter.close();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == LOGIN_REQUEST) {
			if (resultCode == RESULT_OK) {
				// TODO
				mPreferences.edit().putString("nickname", data.getStringExtra("name")).commit();
				mPreferences.edit().putString("chatroom", data.getStringExtra("chatroom")).commit();

				getActionBar().setSubtitle(data.getStringExtra("name") + " @ " + data.getStringExtra("chatroom"));
			} else {
				finish();
			}
		}
	}
}
