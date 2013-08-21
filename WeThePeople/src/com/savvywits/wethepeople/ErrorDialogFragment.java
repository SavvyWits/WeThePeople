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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment {
	
	public static DialogFragment newInstance(String zipCode) {
		ErrorDialogFragment fragment = new ErrorDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("zip_code", zipCode);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String zipCode = getArguments().getString("zip_code");
        
        if (zipCode != null) {
        	builder.setTitle(R.string.no_results_title)
        		   .setMessage(getString(R.string.no_results_before_zip)
        				   + zipCode
        				   + getString(R.string.no_results_after_zip))
        		   .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dismiss();
                   }
               });
        } else {
        	builder.setTitle(R.string.invalid_entry)
 		   .setMessage(R.string.invalid_message)
 		   .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
                }
            });
        }
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
	public ErrorDialogFragment() {}

}
