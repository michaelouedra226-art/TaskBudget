package com.example.taskbudget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends Activity {
    private DBHelper db;
    private ListView list;
    private TextView empty, bal;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_budget);
        db=new DBHelper(this);
        list=findViewById(R.id.tx_list);
        empty=findViewById(R.id.empty_tx);
        bal=findViewById(R.id.balance_text);
        Button add=findViewById(R.id.add_tx_btn);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new ArrayList<>());
        list.setAdapter(adapter);
        list.setOnItemLongClickListener((p,v,pos,id)->{ /* no-op placeholder */ return true; });
        add.setOnClickListener(v->startActivity(new Intent(this,AddTransactionActivity.class)));
    }
    @Override protected void onResume(){ super.onResume(); refresh(); }
    private void refresh(){
        List<String[]> tx=db.getTx();
        List<String> lines=new ArrayList<>();
        for(String[] t:tx) lines.add((t[2].equals("income")?"+ ":"- ")+t[1]+" €  "+t[3]+"  ("+t[4]+")");
        adapter.clear(); adapter.addAll(lines); adapter.notifyDataSetChanged();
        empty.setVisibility(tx.isEmpty()?View.VISIBLE:View.GONE);
        list.setVisibility(tx.isEmpty()?View.GONE:View.VISIBLE);
        bal.setText(String.format("Solde : %.2f €", db.balance()));
    }
}
