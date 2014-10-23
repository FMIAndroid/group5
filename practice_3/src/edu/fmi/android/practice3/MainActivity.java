package edu.fmi.android.practice3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText mName;
	private EditText mPassword;
	private EditText mEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		mPassword = (EditText) findViewById(R.id.password);
		
		final EditText firstName = (EditText) findViewById(R.id.firstlastname);
		firstName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() < 3) {
					firstName.setError("Short name");
				} else {
					firstName.setError(null);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		Button signup = (Button) findViewById(R.id.button1);
		signup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				signUp();
			}
		});
	}
	
	private void signUp() {
		boolean okay = false;
		View errorView = null;
		
		mPassword.setError("");
		
		final EditText password = (EditText) findViewById(R.id.passwordRepeat);
		
		if (!TextUtils.isEmpty(password.getText().toString()) && password.getText().toString().contentEquals(mPassword.getText().toString())) {
			okay = true;
		} else {
			mPassword.setError("Password is too short or empty");
			errorView = mPassword;
		}
		
		if (okay) {
			Intent login = new Intent(this, DetailsActivity.class);
			startActivity(login);
		} else {
			errorView.requestFocus();
		}
		
	}
}
