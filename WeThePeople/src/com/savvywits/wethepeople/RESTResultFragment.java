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

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class RESTResultFragment extends ListFragment {
	
	private static final String TAG = "RESTResultFragment";
	
	private static final String RESULTS = "results";
	private static final String NAME = "name";
	private static final String PARTY = "party";
	private static final String STATE = "state";
	private static final String DISTRICT = "district";
	private static final String OFFICE_ADDRESS = "office";
	private static final String PHONE = "phone";
	private static final String LINK = "link";
	
	private static SimpleAdapter mAdapter;
	
	static ArrayList<HashMap<String, String>> mReplist =
			new ArrayList<HashMap<String, String>>();
	
	public static RESTResultFragment newInstance(String string) {
		RESTResultFragment fragment = new RESTResultFragment();
		Bundle bundle = new Bundle();
		bundle.putString("result_string", string);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new SimpleAdapter(getActivity(), mReplist, R.layout.results_item,
				new String[] { "name", "party", "state", "district", "address", "phone", "link" },
				new int[] { R.id.name, R.id.party, R.id.state,
					R.id.district, R.id.address, R.id.phone, R.id.link });
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {    		
		
		View view = inflater.inflate(R.layout.results_list, container, false);
		mReplist.clear();
		setListAdapter(mAdapter);
		return view;
	}
	
	public static void updateResults(String results) {
		
		try {
			JSONObject jobj = new JSONObject(results);
			JSONArray jarray = new JSONArray();
			jarray = jobj.getJSONArray(RESULTS);
	
			int length = jarray.length();
	
			for (int i = 0; i < length; i++) {
				// Use a hash map because Java does not do associative array
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
			Log.e(TAG, "Error parsing data [" + e.getMessage()+"] " + results);
		}
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();	
	}
	
	public RESTResultFragment() {}

}