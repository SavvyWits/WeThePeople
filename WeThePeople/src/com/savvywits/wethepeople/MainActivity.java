package com.savvywits.wethepeople;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends FragmentActivity
	implements RESTReceiver.Receiver, OnClickListener {
	
	// Constants for onReceiveResult
	private static final int RUNNING = 1;
	private static final int FINISHED = 2;
	private static final int ERROR = 3;
	
	private EditText mData;
	private FragmentManager mFragmentManager = getSupportFragmentManager();
	private String mZipCode;
	
	public RESTReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mReceiver = new RESTReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		mData = (EditText) findViewById(R.id.zipcode);
		mData.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_CLASS_NUMBER);
		
		Button submitButton = (Button) findViewById(R.id.submit);
		submitButton.setOnClickListener(this);
		
	}
	
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch(resultCode) {
		case RUNNING:
			break;
		case FINISHED:
			Fragment fragment = mFragmentManager.findFragmentById(R.id.loading);			
			if (fragment != null) {
				FragmentTransaction ft = mFragmentManager.beginTransaction();
				ft.commit();
			}
				
			if (resultData != null) {
					RESTResultFragment result = (RESTResultFragment)
						mFragmentManager.findFragmentByTag("results");
					if(result == null) {
						String data = resultData.getString("json_result");
						Bundle bundle = new Bundle();
						bundle.putString("json_result", data);
						RESTResultFragment resultList = new RESTResultFragment();
						resultList.setArguments(bundle);
						resultList.show(mFragmentManager, "results");
				}
			}
			break;
		case ERROR:
			Fragment errorFragment = mFragmentManager.findFragmentByTag("error_dialog");
			if (errorFragment == null) {
				DialogFragment error = ErrorDialogFragment.newInstance(mZipCode);
				error.show(mFragmentManager, "error_dialog");
			}
			break;
		}
	}
	
	public void onClick(View view) {

		mZipCode = mData.getText().toString();
		
		if (validZipCode(mZipCode)) {
			Intent intent = new Intent(Intent.ACTION_SYNC, null, this, RESTService.class);
			intent.putExtra("receiver", mReceiver);
			intent.putExtra("data", mZipCode);
			startService(intent);
			
			Fragment fragment = mFragmentManager.findFragmentByTag("in_progress");
			
			if (fragment == null) {
				FragmentTransaction ft = mFragmentManager.beginTransaction();
				ft.commit();
				}
		}else {
			Fragment errorFragment = mFragmentManager.findFragmentByTag("zip_error");
			if (errorFragment == null) {
				DialogFragment error = ErrorDialogFragment.newInstance(null);
				error.show(mFragmentManager, "zip_error");
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.about:
	            return true;
	        case R.id.eula:
	    		Intent intent = new Intent(Intent.ACTION_VIEW);
	    		Uri uri = Uri.parse("http://wethepeopleeula.blogspot.com/");
	    		intent.setData(uri);
	    		startActivity(intent); 
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public boolean validZipCode(String zipCode) {
		boolean valid = false;
		if (zipCode.length() == 5) {
			char[] chars = zipCode.toCharArray();
			for (int i=0; i<5; i++) {
				if (Character.isDigit(chars[i])) {
					valid = true;
				}
			}
		}
		return valid;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mReceiver.setReceiver(null);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mReceiver.setReceiver(this);
	}

}