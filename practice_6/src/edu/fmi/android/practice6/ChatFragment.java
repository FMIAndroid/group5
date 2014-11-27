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
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link ChatFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link ChatFragment#newInstance} factory method to create an instance of this
 * fragment.
 * 
 */
@SuppressLint("NewApi") public class ChatFragment extends Fragment {
	
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
				
				((ChatArrayAdaper)mChat.getAdapter()).add(new ChatMsg(nickname, msg, mPreferences.getString("nickname", "Guest").contentEquals(nickname)));
				((ChatArrayAdaper)mChat.getAdapter()).notifyDataSetChanged();
				mWriter.flush();
			}
		}
	};

	private OutputStream mHistory;
	private PrintWriter mWriter;
	private BufferedReader mReader;
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

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
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		
		Toast.makeText(getActivity(), mParam1, Toast.LENGTH_LONG).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.activity_chat_room, container, false);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		

		mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mChat = (ListView) getView().findViewById(R.id.chat);

		final String nickname = mPreferences.getString("nickname", "Guest");
		String chatroom = mPreferences.getString("chatroom", null);

		if (chatroom == null) {
			Intent login = new Intent(getActivity(), LoginActivity.class);
			startActivityForResult(login, LOGIN_REQUEST);
		} else {

			try {
				File history;
				if (Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED)) {
					history = new File(getActivity().getExternalCacheDir().getAbsolutePath() + "/" + chatroom + ".cht");
				} else {
					history = new File(getActivity().getCacheDir().getAbsolutePath()  + "/" + chatroom + ".cht");
				}

				mHistory = new FileOutputStream(history, true);

				mWriter = new PrintWriter(mHistory);
				mReader = new BufferedReader(new FileReader(history));

				ArrayList<ChatMsg> historyList = new ArrayList<ChatMsg>();
				String line;
				
				while ((line = mReader.readLine()) != null) {
					System.out.println(line);
					final String nick = line.split(": ")[0];
					final String msg = line.split(": ")[1]; 
					
					historyList.add(new ChatMsg(nick, msg, nick.contentEquals(nickname)));
				}
				
				ChatArrayAdaper adapter = new ChatArrayAdaper(getActivity(), 0, historyList);
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

			getActivity().getActionBar().setSubtitle(nickname + " @ " + chatroom);

		}
		final Button mSendButton = (Button) getView().findViewById(R.id.send);
		final EditText mMsgField = (EditText) getView().findViewById(R.id.msg);
		mSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mMsgField.getText().toString())) {
					final String msg = nickname + ": " + mMsgField.getText().toString();

					mWriter.append(msg);
					
					final String nick = msg.split(": ")[0];
					final String message = msg.split(": ")[1]; 
					
					((ChatArrayAdaper)mChat.getAdapter()).add(new ChatMsg(nick, message, nick.contentEquals(nickname)));
					((ChatArrayAdaper)mChat.getAdapter()).notifyDataSetChanged();

					mWriter.flush();
					getActivity().sendBroadcast(new Intent("edu.fmi.android.chat.action.SEND"));
				}
			}
		});
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == LOGIN_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				// TODO
				mPreferences.edit().putString("nickname", data.getStringExtra("name")).commit();
				mPreferences.edit().putString("chatroom", data.getStringExtra("chatroom")).commit();

				getActivity().getActionBar().setSubtitle(data.getStringExtra("name") + " @ " + data.getStringExtra("chatroom"));
			} else {
				getActivity().finish();
			}
		}
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
