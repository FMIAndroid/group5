package edu.fmi.android.practice2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.fmi.android.practice2.activities.SecondActivity;

public class MainActivity extends Activity {

	private static String TAG = "MainActivity";
	private EditText mInput;
	private Button mButton;
	
	private int clickedTimes = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i(TAG, "onCreate()");
		
		mInput = (EditText) findViewById(R.id.input);
		mButton = (Button) findViewById(R.id.button1);
			
	}
	
	public void clickMe(View view) {
		mButton.setText("Clicks = " + ++clickedTimes);
		
		if (clickedTimes % 5 == 0) {
			startActivity(new Intent(this, SecondActivity.class));
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.i(TAG, "onRestoreInstanceState()");
		
		mButton.setText("Clicked = " + savedInstanceState.getInt("clicks"));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart()");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume()");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "onRestart()");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause()");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop()");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy()");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState()");
		outState.putInt("clicks", clickedTimes);
	}
}
