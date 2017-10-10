package com.example.witold.websocketchatter.JoinRoomFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.witold.websocketchatter.R;

/**
 * Created by Witold on 2017-03-17.
 */

public class AddRoomDialog extends DialogFragment {
    AddRoomDialogHolder holder;

    @Nullable

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.add_room_dialog, null);
        builder.setView(view)
                .setTitle("Add Room")
                // Add action buttons
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddRoomDialog.this.getDialog().cancel();
                    }
                });

        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(validateEditTexts())
                        {
                            holder.addRoomToServer(((EditText)getDialog().findViewById(R.id.editTextRoomName)).getText().toString(),Integer.parseInt(((EditText)getDialog().findViewById(R.id.editTextDialogMaxCapacity)).getText().toString()));
                            getDialog().dismiss();
                        }
                    }
                });
            }
        });
        return dialog;
    }

    private boolean validateEditTexts()
    {
        EditText roomName = ((EditText)getDialog().findViewById(R.id.editTextRoomName));
        EditText capacity = ((EditText)getDialog().findViewById(R.id.editTextDialogMaxCapacity));

        if(roomName.getText().toString().equals(""))
        {
            roomName.setError("That field is required");
            return false;
        }
        if(capacity.getText().toString().equals(""))
        {
            capacity.setError("That field is required");
            return false;
        }
        return true;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (AddRoomDialogHolder)context;
    }

    public interface AddRoomDialogHolder
    {
        public abstract void addRoomToServer(String roomName, int capacity);
    }
}