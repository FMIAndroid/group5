package edu.fmi.android.practice5;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private String lastMsg = "";
	private boolean show;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (getIntent() != null && getIntent().hasExtra(MainActivity.EXTRA_NAME))
			getActionBar().setTitle(getIntent().getStringExtra(MainActivity.EXTRA_NAME));
		
		show = getIntent().getBooleanExtra(MainActivity.EXTRA_NOTIFICAITON, true);
	}


	public void sendMessage(View v) {
		final String text = ((EditText) findViewById(R.id.message)).getText().toString();
		
		// Reset EditText and focus
		((EditText) findViewById(R.id.message)).setText("");
		((EditText) findViewById(R.id.message)).requestFocus();

		lastMsg += text + '\n';

		Intent m = new Intent(this, MessagesActivity.class);
		m.putExtra("message", lastMsg);

		final PendingIntent pending = PendingIntent.getActivity(this, 0, m, PendingIntent.FLAG_UPDATE_CURRENT);

		if (show && !TextUtils.isEmpty(text)) {
			Notification notify = new NotificationCompat.Builder(this)
			.setAutoCancel(true)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentText(text)
			.setContentTitle(getString(R.string.app_name))
			.setTicker(text)
			.setContentIntent(pending)
			.build();

			NotificationManagerCompat.from(this).notify(666, notify);
			
			// Chat result
			Intent data = new Intent();
			data.putExtra("messages", lastMsg);
			setResult(RESULT_OK, data);
		} else {
			setResult(RESULT_CANCELED);
		}
	}
}
