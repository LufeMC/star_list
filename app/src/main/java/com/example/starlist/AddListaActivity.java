package com.example.starlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import Data.DatabaseHandler;
import Data.DatabaseHandlerLists;
import Model.GroceryList;

public class AddListaActivity extends AppCompatActivity implements View.OnClickListener{

    private Button addButton;
    private Button cancelButton;
    private static EditText enterListName;
    private DatabaseHandler itemsInList;
    private GroceryList outdated_list;
    private DatabaseHandlerLists lists;
    private Bundle extras;
    private TextView addListaTitle;
    private String list_name;
    private String database_list_name;
    private String fullfilBar;
    private String past_list_name;
    private int list_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lista);

        setUpUI();
    }

    public void setUpUI() {
        addButton = (Button) findViewById(R.id.addButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        enterListName = (EditText) findViewById(R.id.enterListName);

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        lists = new DatabaseHandlerLists(getApplicationContext());
        itemsInList = new DatabaseHandler(getApplicationContext());

        addListaTitle = (TextView) findViewById(R.id.addListaTitle);

        extras = getIntent().getExtras();

        if (extras != null) {

            list_name = extras.getString("listName");
            database_list_name = extras.getString("dbName");


            list_id = Integer.parseInt(extras.getString("id"));

            addListaTitle.setText(list_name);

            addButton.setText(getResources().getString(R.string.confirmar));
            enterListName.setHint(getResources().getString(R.string.novo_nome_lista));

            outdated_list = lists.getList(list_id);
            past_list_name = String.valueOf(outdated_list.getListName());

            enterListName.setText(past_list_name);

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.addButton):
                if (extras != null) {
                    GroceryList updated_list = lists.getList(list_id);
                    updated_list.setListName(enterListName.getText().toString());
                    updated_list.setDatabaseName((enterListName.getText().toString()).replace(" ", "_").toLowerCase());
                    updated_list.setWasEdited("true");
                    updated_list.setOutdated_name(past_list_name);
                    int updatedList = lists.updateList(updated_list);
                    Log.d("Renaming...", past_list_name);
                    Log.d("Renaming new name", String.valueOf(updated_list.getListName()));
                    lists.close();

                    Intent returnIntent = new Intent(AddListaActivity.this, MainActivity.class);
                    returnIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    returnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(returnIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

                } else {

                    Log.d("is_this_this_working", "yes");

                    fullfilBar = "0/0";

                    lists.createList(new GroceryList(lists.countLists() + 1, enterListName.getText().toString(), fullfilBar,
                            (enterListName.getText().toString()).replace(" ", "_").toLowerCase()
                            , "false", null));

                    String outdated_name = String.valueOf(lists.getList(lists.countLists()).getOutdated_name());
                    Intent intent = new Intent(AddListaActivity.this, ItemsActivity.class);
                    intent.putExtra("list_name", enterListName.getText().toString());
                    intent.putExtra("list_id", lists.countLists());
                    intent.putExtra("edited", false);
                    intent.putExtra("outdated_name", outdated_name);
                    Log.d("creating_list...", outdated_name);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    break;

                }
            case (R.id.cancelButton):
                Intent intent2 = new Intent();
                intent2.setClass(AddListaActivity.this, MainActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(AddListaActivity.this, MainActivity.class);
        setIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        setIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(setIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}