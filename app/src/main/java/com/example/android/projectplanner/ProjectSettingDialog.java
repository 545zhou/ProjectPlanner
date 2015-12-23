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
 * Created by tao on 8/17/15.
 */
public class ProjectSettingDialog extends DialogFragment {
    private int index;
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View myCustomView = inflater.inflate(R.layout.project_setting_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myCustomView)
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText project_name = (EditText) myCustomView.findViewById(R.id.project_to_modify);
                        String name = project_name.getText().toString();

                        EditText project_hour = (EditText) myCustomView.findViewById(R.id.hours_to_modify);
                        String stringHour = project_hour.getText().toString();
                        double hour = 0;
                        if(stringHour.length() != 0){
                            hour = Double.parseDouble(stringHour);
                        }

                        EditText project_hour_boost = (EditText) myCustomView.findViewById(R.id.hours_to_boost);
                        stringHour = project_hour_boost.getText().toString();
                        double hour_boost = 0;
                        if(stringHour.length() != 0){
                            hour_boost = Double.parseDouble(stringHour);
                        }

                        EditText project_hour_reduce = (EditText) myCustomView.findViewById(R.id.hours_to_reduce);
                        stringHour = project_hour_reduce.getText().toString();
                        double hour_reduce = 0;
                        if(stringHour.length() != 0){
                            hour_reduce = Double.parseDouble(stringHour);
                        }


                        mListener.onProjectSettingDialogPositiveClick(ProjectSettingDialog.this, name, hour, hour_boost, hour_reduce, index);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProjectSettingDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public interface ProjectSettingDialogListener {
        public void onProjectSettingDialogPositiveClick(DialogFragment dialog, String name, double hour, double hour_boost, double hour_reduce, int index);

        public void onProjectSettingDialogNegativeClick(DialogFragment dialog);

    }

    // Use this instance of the interface to deliver action events
    ProjectSettingDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ProjectSettingDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + "must implement NoticeDialogListener");
        }
    }

    public void getIndexInList(int index){
        this.index = index;
    }
}
