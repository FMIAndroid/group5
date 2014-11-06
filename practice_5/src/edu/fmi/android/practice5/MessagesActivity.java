package edu.fmi.android.practice5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MessagesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();

		if (intent != null && intent.hasExtra("message")) {
			View textView = findViewById(R.id.messages);
			((TextView) textView).setText(getIntent().getStringExtra("message"));
		}
	}
}
