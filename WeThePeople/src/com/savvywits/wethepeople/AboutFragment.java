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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
	
	public static AboutFragment newInstance() {
		return new AboutFragment();
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView text = (TextView) inflater.inflate(R.layout.about_layout, container, false);
        text.setText(Html.fromHtml(AboutText.ABOUT));

        Linkify.addLinks(text, Linkify.ALL);
        
        return text;
    }
    @Override
    public void onResume() {
    	super.onResume();
    	}
    
    @Override
    public void onStart() {
    	super.onStart();
    	}
    
    @Override
    public void onPause() {
    	super.onPause();
    	}
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	}
    
    @Override
    public void onStop() {
    	super.onStop();
    	}
    
    public AboutFragment(){}
}