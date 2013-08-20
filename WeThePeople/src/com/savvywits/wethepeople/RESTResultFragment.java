package com.savvywits.wethepeople;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class RESTResultFragment extends DialogFragment {
	
	private static final String TAG = "RESTResultFragment";
	
	private static final String RESULTS = "results";
	private static final String NAME = "name";
	private static final String PARTY = "party";
	private static final String STATE = "state";
	private static final String DISTRICT = "district";
	private static final String OFFICE_ADDRESS = "office";
	private static final String LINK = "link";
	private static final String PHONE = "phone";
	
	ArrayList<HashMap<String, String>> mReplist =
			new ArrayList<HashMap<String, String>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		String data = getArguments().getString("json_result");
        
        try {
    		JSONObject jobj = new JSONObject(data);
    		JSONArray jarray = new JSONArray();
    		jarray = jobj.getJSONArray(RESULTS);
    		
        	int length = jarray.length();
        	
        	for (int i = 0; i < length; i++) {
        		
        		HashMap<String, String> map = new HashMap<String, String>();
        		
    			JSONObject r = jarray.getJSONObject(i);
    			
    			map.put("id", String.valueOf(i));
    			map.put("name", r.getString(NAME));
    			map.put("party", r.getString(PARTY));
    			map.put("state", r.getString(STATE));
    			map.put("district", r.getString(DISTRICT));
    			map.put("address", r.getString(OFFICE_ADDRESS));    			
    			map.put("phone", r.getString(PHONE));
    			map.put("link", r.getString(LINK));
    			
    			mReplist.add(map);        		
        	}
    	} catch (JSONException e){
    		Log.e("RESTResult", "Error parsing data [" + e.getMessage()+"] " + data);
    	}
    	
        ListAdapter adapter = new SimpleAdapter(getActivity(), mReplist, R.layout.results_item,
        		new String[] { "name", "party", "state", "district", "address", "phone", "link" },
        		new int[] { R.id.name, R.id.party, R.id.state,
        			R.id.district, R.id.address, R.id.phone, R.id.link });
        
        return new AlertDialog.Builder(getActivity())
        	.setAdapter(adapter, null)
        	.create();
		
	}
	
	public RESTResultFragment() {
		// The fragment's empty constructor
	}

}