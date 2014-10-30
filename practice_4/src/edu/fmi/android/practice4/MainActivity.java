package edu.fmi.android.practice4;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText password = (EditText) findViewById(R.id.password);
		password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() < 6) {
					password.setError("short password");
				} else {
					password.setError(null);
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
	}

	public void onSignUp(View v) {
		showProgress(true);

		// WRONG!!!!		
		//		Thread t = new Thread(new Runnable() {
		//			
		//			@Override
		//			public void run() {
		//				try {
		//					Thread.sleep(10000);
		//				} catch (InterruptedException e) {
		//					// TODO Auto-generated catch block
		//					e.printStackTrace();
		//				}
		//				showProgress(false);
		//			}
		//		});
		//		
		//		t.start();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				showProgress(false);
			}
		}, 5000);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1) private void showProgress(final boolean show) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final View container = findViewById(R.id.hide);
				final View progress = findViewById(R.id.progress);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {

					int animTime = getResources().getInteger(android.R.integer.config_longAnimTime);

					container.setVisibility(show ? View.GONE : View.VISIBLE);
					container
					.animate()
					.alpha(show ? 0 : 1)
					.setDuration(animTime)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							super.onAnimationEnd(animation);
							progress.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

					progress.setVisibility(show ? View.VISIBLE : View.GONE);
					progress.animate()
					.alpha(show ? 1 : 0)
					.setDuration(animTime).setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							super.onAnimationEnd(animation);
							container.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});

				} else {
					container.setVisibility(show ? View.GONE : View.VISIBLE);
					progress.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			} 
		});
	}
}
