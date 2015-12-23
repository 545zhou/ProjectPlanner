package com.example.android.projectplanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by tao on 7/20/15.
 */
public class AddNewProjectDialog extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View myCustomView = inflater.inflate(R.layout.add_newproject_dialog_layout, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myCustomView)
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText project_name = (EditText) myCustomView.findViewById(R.id.project_to_add);
                        String name = project_name.getText().toString();

                        EditText project_hour = (EditText) myCustomView.findViewById(R.id.hours_to_add);
                        String stringHour = project_hour.getText().toString();
                        double hour = 0;
                        if(stringHour.length() != 0){
                            hour = Float.parseFloat(stringHour);
                        }

                        mListener.onNewProjectDialogPositiveClick(AddNewProjectDialog.this, name, hour);


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddNewProjectDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public interface AddNewProjectDialogListener {
        public void onNewProjectDialogPositiveClick(DialogFragment dialog, String name, double hour);
        public void onNewProjectDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddNewProjectDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddNewProjectDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + "must implement NoticeDialogListener");
        }
    }

}
