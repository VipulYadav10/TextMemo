package com.vip.textmemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.vip.textmemo.data.DatabaseHandler;
import com.vip.textmemo.model.Memo;
import com.vip.textmemo.ui.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;
    private FloatingActionButton fab;
    private List<Memo> memoList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private TextView title;
    private EditText heading;
    private EditText text;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler_view_id);
        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        // experimental statements begins.

//        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        // experimental statements ends here.

        memoList = new ArrayList<>();
        memoList = databaseHandler.getAllMemo();


        recyclerViewAdapter = new RecyclerViewAdapter(this, memoList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupDialog();
            }
        });




    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        heading = view.findViewById(R.id.heading_popup_id);
        text = view.findViewById(R.id.text_popup_id);
        saveButton = view.findViewById(R.id.save_popup_button);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!heading.getText().toString().isEmpty()
                && !text.getText().toString().isEmpty()) {
                    saveItem(v);
                }
                else {
                    Snackbar.make(v, "Fields cannot be Empty!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveItem(View v) {
        Memo memo = new Memo();
        memo.setHeading(heading.getText().toString());
        memo.setText(text.getText().toString());
        databaseHandler.addMemo(memo);

        Snackbar.make(v, "Item Saved", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();

                startActivity(new Intent(ListActivity.this, ListActivity.class));
            }
        }, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_delete_all) {
            deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        databaseHandler.deleteAll();
        startActivity(new Intent(ListActivity.this, MainActivity.class));
    }
}