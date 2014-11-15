package edu.fmi.android.practice6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		final Button join = (Button) findViewById(R.id.join);
		join.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final EditText nickname = (EditText) findViewById(R.id.nickname);
				final EditText chatroom = (EditText) findViewById(R.id.chatroom);
				
				boolean success = !TextUtils.isEmpty(nickname.getText().toString()) 
						&& !TextUtils.isEmpty(chatroom.getText().toString());
				
				if (success) {
					final Intent data = new Intent();
					data.putExtra("name", nickname.getText().toString());
					data.putExtra("chatroom", chatroom.getText().toString());
					
					setResult(RESULT_OK, data);
					finish();
				} else {
					nickname.setError("Not valid nickname");
					chatroom.setError("Not a valid chatroom");
					setResult(RESULT_CANCELED);
				}
			}
		});
	}
}
