package com.savvywits.wethepeople;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        inflater.inflate(R.layout.about_layout, container, false);
        
        String text = new String();
        TextView tv = (TextView)getActivity().findViewById(R.id.aboutpage);
        tv.setText(Html.fromHtml(text));
        
        return tv;
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
}