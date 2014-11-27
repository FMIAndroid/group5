package edu.fmi.android.practice6;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatArrayAdaper extends ArrayAdapter<ChatMsg> {
	
	public ChatArrayAdaper(Context context, int resource,
			List<ChatMsg> objects) {
		super(context, 0, objects);
	}
	
	@Override
	public ChatMsg getItem(int position) {
		return super.getItem(position);
	}
	
	@Override
	public int getPosition(ChatMsg item) {
		return super.getPosition(item);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ChatMsg current = getItem(position);
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		final View view;
		
		if (current.mIsOur) {
			view = inflater.inflate(R.layout.chat_row_me, parent, false);
		} else {
			view = inflater.inflate(R.layout.chat_row_you, parent, false);
		}
		
		TextView nickname = (TextView) view.findViewById(R.id.nickname);
		nickname.setText(current.mNickname);
		
		TextView msg = (TextView) view.findViewById(R.id.msg);
		msg.setText(current.mMessage);
		
		ImageView avatar = (ImageView) view.findViewById(R.id.imageView1);
		avatar.setImageDrawable(new LetterAvatar(getContext(), current.mNickname, Color.RED));
		
		return view;
	}
	
}