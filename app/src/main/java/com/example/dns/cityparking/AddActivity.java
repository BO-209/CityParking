package com.example.dns.cityparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddActivity extends Activity implements View.OnClickListener {

    private CheckBox parkChoiceBox;
    private CheckBox noParkChoiceBox;
    private Button okBtn;
    private EditText priceTxt;
    private EditText descriptionTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        parkChoiceBox = (CheckBox) findViewById(R.id.checkBoxNewPark);
        noParkChoiceBox = (CheckBox) findViewById(R.id.checkBoxNewNoPark) ;
        okBtn = (Button) findViewById(R.id.btnOK);
        priceTxt = (EditText) findViewById(R.id.textPrice);
        descriptionTxt = (EditText) findViewById(R.id.textDescription);
        okBtn.setOnClickListener(this);
    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkBoxNewPark:
                if (checked){
                    noParkChoiceBox.setChecked(false);
                }
                break;
            case R.id.checkBoxNewNoPark:
                if (checked)
                    parkChoiceBox.setChecked(false);
                break;
        }
    }



    @Override
    public void onClick(View v) {
        Log.d("my log", "get add activity");
        Intent intent = new Intent();
        int av;
        if (parkChoiceBox.isChecked()) {
            av = 1;
        } else av = 0;
        intent.putExtra("available", av);
        intent.putExtra("price", priceTxt.getText().toString());
        intent.putExtra("description", descriptionTxt.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}

