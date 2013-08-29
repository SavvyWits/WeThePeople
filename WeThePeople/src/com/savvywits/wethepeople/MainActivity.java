/***
  Copyright (c) 2013 Rich Dudka
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

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

/*
 * This application uses just one activity, similar to an MVC web site.
 */

public class MainActivity extends FragmentActivity
	implements RESTReceiver.Receiver, OnClickListener {
	
	// Constants for onReceiveResult
	private static final int RUNNING = 1;
	private static final int FINISHED = 2;
	private static final int ERROR = 3;
	
	private EditText mData;
	// One FragmentManager is plenty
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
		// This makes a nice ten-key pad
		mData.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_CLASS_NUMBER);
		
		Button submitButton = (Button) findViewById(R.id.submit);
		submitButton.setOnClickListener(this);
		
	}
	
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch(resultCode) {
		case RUNNING:
			/*
			 * TODO: Check for server not responding errors
			 */
			break;
		case FINISHED:
			String data = resultData.getString("rest_result");
			RESTResultFragment.newInstance(data);
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
	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 
	 * Add an empty RESTResultFragment here, taking advantage of id/android:empty
	 * in the ListView to show progress wheel. An alternative approach is a fragment
	 * dedicated to showing a progress wheel. The approach used here is chosen because
	 * it takes fewer classes.
	 */
	public void onClick(View view) {
		mZipCode = mData.getText().toString();		
		if (validZipCode(mZipCode)) {
			Fragment fragment = mFragmentManager.findFragmentById(R.id.overlay);			
			if (fragment == null) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				RESTResultFragment emptyList = RESTResultFragment.newInstance(null);
				fragmentTransaction.add(emptyList, "results_list");
				fragmentTransaction.commit();
				}
			
			Intent intent = new Intent(Intent.ACTION_SYNC, null, this, RESTService.class);
			intent.putExtra("receiver", mReceiver);
			intent.putExtra("zipcode", mZipCode);
			startService(intent);
			
		} else {
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
	    switch (item.getItemId()) {
	        case R.id.about:
	        	Fragment aboutFragment = mFragmentManager.findFragmentByTag("about");
	        	if (aboutFragment == null) {
	        		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
	        		AboutFragment about = AboutFragment.newInstance();
	        		fragmentTransaction.add(about, "about");
	        		fragmentTransaction.commit();
	        	}
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
	
	@Override
	public void onBackPressed() {
		/*
		 * Checking for a view id instead of a fragment tag allows for the
		 * possibility of either an AboutFragment or RESTResultFragment.
		 */
		Fragment overlay = mFragmentManager.findFragmentById(R.id.overlay);
		if (overlay != null) {
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.remove(overlay);
			fragmentTransaction.commit();
		} else {
			super.onBackPressed();
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
		// The ResultReceiver's Handler needs to be released
		mReceiver.setReceiver(null);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mReceiver.setReceiver(this);
	}

}