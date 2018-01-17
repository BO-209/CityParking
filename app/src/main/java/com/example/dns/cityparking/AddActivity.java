package com.example.dns.cityparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by dns on 16.01.2018.
 */

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
        // Получаем, отмечен ли данный флажок
        boolean checked = ((CheckBox) view).isChecked();

        // Смотрим, какой именно из флажков отмечен
        switch(view.getId()) {
            case R.id.checkBoxNewPark:
                if (checked){
                    //maxPrice.setEnabled(true);
                    noParkChoiceBox.setChecked(false);
                }
                break;
            case R.id.checkBoxNewNoPark:
                if (checked)
                    //maxPrice.setEnabled(false);
                    parkChoiceBox.setChecked(false);
                break;
        }
    }



    @Override
    public void onClick(View v) {
        Log.d("my log", "get add activity");
        Intent intent = new Intent();
        intent.putExtra("available", parkChoiceBox.isChecked());
        intent.putExtra("price", priceTxt.getText().toString());
        intent.putExtra("description", descriptionTxt.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}

