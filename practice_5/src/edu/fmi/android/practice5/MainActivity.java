package edu.fmi.android.practice5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends Activity {

	public static final String EXTRA_NOTIFICAITON = "edu.fmi.android.extra.NOTIFY";
	public static final String EXTRA_NAME = "edu.fmi.android.extra.NAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onLogin(View v) {
		final Intent login = new Intent(this, LoginActivity.class);
		login.putExtra(EXTRA_NOTIFICAITON, ((CheckBox) findViewById(R.id.notification)).isChecked());
		login.putExtra(EXTRA_NAME, ((EditText) findViewById(R.id.name)).getText().toString());

		startActivityForResult(login, 666);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == 666) {
				new AlertDialog.Builder(this)
				.setMessage(data.getStringExtra("messages"))
				.setTitle("Session History")
				.setPositiveButton("Yeah!", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						finish();
					}
				}).show();
			}
		}
	}
}
