package com.vip.textmemo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.vip.textmemo.data.DatabaseHandler;
import com.vip.textmemo.model.Memo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText heading;
    private EditText text;
    private Button saveButton;
    private DatabaseHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new DatabaseHandler(MainActivity.this);

        byPassActivity();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
    }

    private void byPassActivity() {
        if(handler.getCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        heading = view.findViewById(R.id.heading_popup_id);
        text = view.findViewById(R.id.text_popup_id);
        saveButton = view.findViewById(R.id.save_popup_button);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!heading.getText().toString().isEmpty()
                && !text.getText().toString().isEmpty()) {
                    saveMemo(v);
                }
                else {
                    Snackbar.make(v, "Fields cannot be Empty!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveMemo(View v) {
        Memo memo = new Memo();
        memo.setHeading(heading.getText().toString());
        memo.setText(text.getText().toString());
        handler.addMemo(memo);

        Snackbar.make(v, "Item Saved", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1000);
    }
}