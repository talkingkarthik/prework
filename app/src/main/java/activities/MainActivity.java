package activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.kartikkulkarni.prework.R;

import java.util.ArrayList;
import java.util.Date;

import adapters.ToDoItemsAdapter;
import database.CouchbaseDAO;
import models.ToDoItem;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ToDoItem> todolist;
    private ToDoItemsAdapter adapter;
    private CouchbaseDAO dao = null;

    @Override
    protected void onDestroy() {
        dao.persistList(todolist);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        dao.persistList(todolist);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        //First lets inflate custom action bar
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar_custom, null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        // Initiate the singleton DAO
        this.dao = CouchbaseDAO.getInstance(this);

        //Initiate the inmemory list to hold the todos, and add first one as example
        todolist = new ArrayList<ToDoItem>();
        todolist.add(new ToDoItem("Call Komi", "Else she may forget",
                (new Date()), false, true, null));

        //Populate the todos from persistent store
        dao.populateList(todolist);


        // Initiate the add button
        Button addbtn = (Button) findViewById(R.id.Add_button);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todolist.add(new ToDoItem("Default Title", "Default Note",
                        (new Date()), false, true, null));
                adapter.notifyDataSetChanged();
                adapter.showEditDialog(todolist.size() - 1);
            }
        });

        //instantiate custom adapter for populating todos in the listview
        adapter = new ToDoItemsAdapter(todolist, this, getSupportFragmentManager());
        //handle listview and assign adapter
        ListView lView = (ListView) findViewById(R.id.todo_list);
        lView.setAdapter(adapter);

    }
}
