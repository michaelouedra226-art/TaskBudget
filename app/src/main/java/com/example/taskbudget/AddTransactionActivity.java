package com.example.taskbudget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddTransactionActivity extends Activity {
    private DBHelper db;
    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_add_transaction);
        db=new DBHelper(this);
        final EditText amount=findViewById(R.id.amount_input);
        final EditText desc=findViewById(R.id.desc_input);
        final RadioGroup type=findViewById(R.id.type_group);
        Button save=findViewById(R.id.save_btn), cancel=findViewById(R.id.cancel_btn);
        save.setOnClickListener(v->{
            String a=amount.getText().toString().trim();
            if(a.isEmpty()) return;
            try{
                double val=Double.parseDouble(a);
                int sel=type.getCheckedRadioButtonId();
                String t = (sel==R.id.radio_income)?"income":"expense";
                db.addTx(val,t,desc.getText().toString().trim(), "aujourd'hui");
                finish();
            }catch(NumberFormatException e){}
        });
        cancel.setOnClickListener(v->finish());
    }
}
