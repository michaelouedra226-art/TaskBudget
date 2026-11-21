package com.example.taskbudget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends Activity {
    private DBHelper db;
    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_add_task);
        db=new DBHelper(this);
        final EditText title=findViewById(R.id.title_input);
        final EditText due=findViewById(R.id.due_input);
        Button save=findViewById(R.id.save_btn), cancel=findViewById(R.id.cancel_btn);
        save.setOnClickListener(v->{ String t=title.getText().toString().trim(); if(t.isEmpty()) return; db.addTask(t,due.getText().toString().trim()); finish(); });
        cancel.setOnClickListener(v->finish());
    }
}
