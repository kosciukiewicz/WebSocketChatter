package com.example.witold.websocketchatter.ViewControllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.witold.websocketchatter.R;

/**
 * Created by Witold on 2017-03-14.
 */

public class NickNameDialog extends  DialogFragment{

    NicknameDialogHolder holder;

    @Nullable

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.nickname_dialog_layout, null);
        builder.setView(view)
                .setTitle("Choose your nickname")
                // Add action buttons
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NickNameDialog.this.getDialog().cancel();
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
                            holder.connectToRoomWithNickname(getArguments().getString("roomId"), ((EditText)getDialog().findViewById(R.id.editTextDialogNickname)).getText().toString() );
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
        EditText nickname = ((EditText)getDialog().findViewById(R.id.editTextDialogNickname));

        if(nickname.getText().toString().equals(""))
        {
            nickname.setError("That field is required");
            return false;
        }
        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (NicknameDialogHolder)context;
    }

    public interface NicknameDialogHolder
    {
        public abstract void connectToRoomWithNickname(String roomId, String nick);
    }
}
