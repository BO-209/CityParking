package com.example.dns.cityparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class FiltActivity extends Activity implements View.OnClickListener {

    private CheckBox parkCheckBox;
    private CheckBox noParkCheckBox;
    private Button showBtn;
    private EditText maxPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filt);

        parkCheckBox = (CheckBox) findViewById(R.id.checkBoxPark);
        noParkCheckBox = (CheckBox) findViewById(R.id.checkBoxNoPark) ;
        showBtn = (Button) findViewById(R.id.btnShow);
        maxPrice = (EditText) findViewById(R.id.textMaxPrice);
        showBtn.setOnClickListener(this);

    }

    public void onCheckboxClicked(View view) {
        // Получаем, отмечен ли данный флажок
        boolean checked = ((CheckBox) view).isChecked();

        // Смотрим, какой именно из флажков отмечен
        switch(view.getId()) {
            case R.id.checkBoxPark:
                if (checked){
                    maxPrice.setEnabled(true);
                    noParkCheckBox.setChecked(false);
                }
                break;
            case R.id.checkBoxNoPark:
                if (checked)
                    maxPrice.setEnabled(false);
                    parkCheckBox.setChecked(false);
                break;
        }
    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        if (parkCheckBox.isChecked()){
            String price = maxPrice.getText().toString();
            if(price == null){ price = "0";}
            intent.putExtra("price", price);
            intent.putExtra("available", 1);
        } else if(noParkCheckBox.isChecked()){
            intent.putExtra("price", "0");
            intent.putExtra("available", 0);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
