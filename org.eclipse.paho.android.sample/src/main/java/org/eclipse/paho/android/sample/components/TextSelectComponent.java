package org.eclipse.paho.android.sample.components;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.eclipse.paho.android.sample.R;

import java.util.ArrayList;


public class TextSelectComponent extends RelativeLayout {

    private static final String TAG = "TextSelectComponent";

    private RelativeLayout textSelectLayout;
    private TextView mainLabel;
    private TextView subLabel;

    private String inputTitle;
    private String defaultText;



    private String setText;
    private boolean numberInput;

    private ArrayList<ITextSelectCallback> registeredCallbacks = new ArrayList<ITextSelectCallback>();

    Context context;

    public TextSelectComponent(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.text_select, this);
        this.mainLabel = (TextView) findViewById(R.id.mainLabel);
        this.subLabel = (TextView) findViewById(R.id.subLabel);
        this.textSelectLayout = (RelativeLayout) findViewById(R.id.container);
        final TypedArray attributeArray = context.obtainStyledAttributes(attr, R.styleable.TextSelectComponent);
        this.mainLabel.setText(attributeArray.getString(R.styleable.TextSelectComponent_main_label));
        this.subLabel.setText(attributeArray.getString(R.styleable.TextSelectComponent_default_value));
        this.inputTitle = attributeArray.getString(R.styleable.TextSelectComponent_input_title);
        this.defaultText = attributeArray.getString(R.styleable.TextSelectComponent_default_value);
        setText = this.defaultText;
        this.numberInput = attributeArray.getBoolean(R.styleable.TextSelectComponent_number, false);
        textSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
    }



    protected void showInputDialog(){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View promptView = layoutInflater.inflate(R.layout.text_input_dialog, null);
        TextView promptText = (TextView) promptView.findViewById(R.id.textView);
        promptText.setText(inputTitle);
        final EditText promptEditText = (EditText) promptView.findViewById(R.id.edittext);
        if(this.numberInput == true){
            Log.i(TAG, "NUMBER INPUT");
            promptEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            Log.i(TAG, "NOT A NUMBER INPUT");
            promptEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        Log.i(TAG, "Setting text to: " + setText);
        Log.i(TAG, "input Type: " + promptEditText.getInputType());
        promptEditText.setText(setText);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        // Set up a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String text = promptEditText.getText().toString();
                        subLabel.setText(text);
                        for (ITextSelectCallback callback : registeredCallbacks) {
                            callback.onTextUpdate(text);
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.setOnKeyListener(new Dialog.OnKeyListener(){
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    dialog.cancel();
                }
                return true;
            }
        });

        // Create the alert Dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void register(ITextSelectCallback callback){
        registeredCallbacks.add(callback);
    }


    public String getSetText() {
        return setText;
    }

    public void setSetText(String setText) {
        this.setText = setText;

        this.subLabel.setText(setText);
    }

    public int getSetInt(){
        return Integer.parseInt(setText);
    }

    public void setSetInt(int value){
        this.setText = String.valueOf(value);
    }






}
