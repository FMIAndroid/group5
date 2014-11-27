package edu.fmi.android.practice6;

import edu.fmi.android.practice6.ChatRoomFragment.OnFragmentInteractionListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatRoomActivity extends Activity implements OnFragmentInteractionListener {

	private SlidingPaneLayout mPanes;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		mPanes = (SlidingPaneLayout) findViewById(R.id.panes);

		mPanes.openPane();

		Fragment chat = ChatFragment.newInstance(null, null);

		getFragmentManager()
		.beginTransaction()
		.add(R.id.chatrooms, ChatRoomFragment.newInstance("elsys", "unss"), "chatrooms")
		.add(R.id.pane2, chat, "chat")
		.commit();
	}

	@Override
	public void onFragmentInteraction(String item) {
		
		"unss".equals(item);
		item.equals("unss");
		
		Fragment selected = "unss".equals(item) ? ChatFragment.newInstance(item, null) : new DummyFragment();
		getFragmentManager().beginTransaction().replace(R.id.pane2, selected, "chat").commit();
		mPanes.closePane();
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
