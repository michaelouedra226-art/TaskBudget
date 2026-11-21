package com.example.taskbudget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private DBHelper db;
    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_main);
        db=new DBHelper(this);
        Button t=findViewById(R.id.btn_tasks), b=findViewById(R.id.btn_budget);
        t.setOnClickListener(v->startActivity(new Intent(this,TasksActivity.class)));
        b.setOnClickListener(v->startActivity(new Intent(this,BudgetActivity.class)));
    }
    @Override
    protected void onResume(){
        super.onResume();
        TextView bal=findViewById(R.id.balance_text);
        bal.setText(String.format("Solde : %.2f €", db.balance()));
    }
}
