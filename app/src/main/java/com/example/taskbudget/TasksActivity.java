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

public class TasksActivity extends Activity {
    private DBHelper db;
    private ListView list;
    private TextView empty;
    private ArrayAdapter<String> adapter;
    private List<String[]> tasks=new ArrayList<>();

    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_tasks);
        db=new DBHelper(this);
        list=findViewById(R.id.task_list);
        empty=findViewById(R.id.empty_tasks);
        Button add=findViewById(R.id.add_task_btn);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new ArrayList<>());
        list.setAdapter(adapter);
        list.setOnItemClickListener((p,v,pos,id)->{
            String[] t=tasks.get(pos);
            boolean done=t[3].equals("1");
            db.toggleTask(Long.parseLong(t[0]),!done);
            refresh();
        });
        list.setOnItemLongClickListener((p,v,pos,id)->{ db.deleteTask(Long.parseLong(tasks.get(pos)[0])); refresh(); return true; });
        add.setOnClickListener(v->startActivity(new Intent(this,AddTaskActivity.class)));
    }
    @Override protected void onResume(){ super.onResume(); refresh(); }
    private void refresh(){
        tasks=db.getTasks();
        List<String> lines=new ArrayList<>();
        for(String[] t:tasks) lines.add((t[3].equals("1")?"✓ ":"○ ")+t[1]+(t[2].isEmpty()?"":"  (éch. "+t[2]+")"));
        adapter.clear(); adapter.addAll(lines); adapter.notifyDataSetChanged();
        empty.setVisibility(tasks.isEmpty()?View.VISIBLE:View.GONE);
        list.setVisibility(tasks.isEmpty()?View.GONE:View.VISIBLE);
    }
}
