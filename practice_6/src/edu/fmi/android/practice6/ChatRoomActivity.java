package edu.fmi.android.practice6;

import edu.fmi.android.practice6.ChatRoomFragment.OnFragmentInteractionListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatRoomActivity extends Activity implements OnFragmentInteractionListener {

	public static int LOGIN_REQUEST = 666;
	
	private SharedPreferences mPreferences;
	
	private SlidingPaneLayout mPanes;

	private int mOrientation;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);
		
		mOrientation = getResources().getConfiguration().orientation;
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		if (mOrientation == 1) {
			getFragmentManager()
			.beginTransaction()
			.replace(R.id.chatrooms, ChatRoomFragment.newInstance("general", "news"))
			.commit();
		} else {
			mPanes = (SlidingPaneLayout) findViewById(R.id.panes);

			mPanes.openPane();
			
			getFragmentManager()
			.beginTransaction()
			.add(R.id.chatrooms, ChatRoomFragment.newInstance("general", "news"), "chatrooms")
			.commit();
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		mOrientation = newConfig.orientation;
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			
		} else {
			
		}
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if (!mPreferences.contains("chatroom")) {
			Intent login = new Intent(this, LoginActivity.class);
			startActivityForResult(login, LOGIN_REQUEST);
		} else {
			onFragmentInteraction(mPreferences.getString("chatroom", "default"));
		}
	}

	@Override
	public void onFragmentInteraction(String item) {	
		Fragment selected = ChatFragment.newInstance(item, null);
		((ChatRoomFragment) getFragmentManager().findFragmentById(R.id.chatrooms)).addIfNotExist(item);
		
		if (mOrientation == 1) {
			getFragmentManager().beginTransaction().replace(R.id.chatrooms, selected).addToBackStack(null).commit();
		} else {
			getFragmentManager().beginTransaction().replace(R.id.pane2, selected, "chat").commit();
			mPanes.closePane();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == LOGIN_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				// TODO
				mPreferences.edit().putString("nickname", data.getStringExtra("name")).commit();
				mPreferences.edit().putString("chatroom", data.getStringExtra("chatroom")).commit();

				getActionBar().setSubtitle(data.getStringExtra("name") + " @ " + data.getStringExtra("chatroom"));
				onFragmentInteraction(data.getStringExtra("chatroom"));
			} else {
				finish();
			}
		}
	}
	
	public static class DummyFragment extends Fragment {
		
		public DummyFragment(){}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_chat, container, false);
		}
	}
}
