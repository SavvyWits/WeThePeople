package com.savvywits.wethepeople;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

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
        		   .setMessage(R.string.no_results_before_zip + zipCode + R.string.no_results_after_zip)
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
