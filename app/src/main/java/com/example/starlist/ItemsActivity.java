package com.example.starlist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import Adapter.MyAdapterItems;
import Data.DatabaseHandler;
import Data.DatabaseHandlerLists;
import Model.GroceryList;
import Model.GroceryListItem;

public class ItemsActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private FloatingActionButton floatingActionButton2;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Bundle extras;
    private Intent intent;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private DatabaseHandler items;
    private DatabaseHandlerLists lists;
    private List<GroceryListItem> groceryListItemList;
    private List<GroceryListItem> allItems;
    private String total_quantity;
    private String title_name_items;
    private String final_marked_itens;
    private String outdated_name;
    private ImageView back_button;
    private ImageView breadImage;
    private TextView addItemText;
    private TextView addItemText2;
    private TextView title_items;
    private Button addItem;
    private Button cancelItem;
    private EditText item_name;
    private EditText item_quantity;
    private int count = 0;
    private int marked_items = 0;
    private int s = 0;
    private int list_id;
    private boolean edited;
    private boolean initialBought;
    private AdView adViewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        content();
    }

    public void content() {

        setUpUI();

        startAdView();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        initialBought = sharedPreferences.getBoolean("bought", false);

        intent = getIntent();

        lists = new DatabaseHandlerLists(this);

        items = new DatabaseHandler(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        final TypedArray a = this.obtainStyledAttributes(new TypedValue().data,  textSizeAttr);
        int ActBarHeight = a.getDimensionPixelSize(0, 0);
        a.recycle();

        int rcvHeight = height - ActBarHeight;

        recyclerView.setMinimumHeight(rcvHeight);

        extras = getIntent().getExtras();

        list_id = extras.getInt("list_id");

        groceryListItemList = new ArrayList<>();

        if (extras != null) {
            title_name_items = extras.getString("list_name");
        }

        allItems = items.getAllItens();

        for (GroceryListItem groceryListItem : allItems) {
            outdated_name = extras.getString("outdated_name");
            edited = extras.getBoolean("edited");
            Log.d("contains_edited", String.valueOf(edited));

            Log.d("outdated_name_new", String.valueOf(outdated_name));

            if (groceryListItem.getItem() == null) {
                items.deleteItem(groceryListItem);
            }

            if ((groceryListItem.getItem() != null) && groceryListItem.getItem_colunm().equals(outdated_name)) {

                Log.d("obtaining_id", String.valueOf(groceryListItem.getId()));

                GroceryListItem item = items.getItem(groceryListItem.getId());

                groceryListItemList.remove(groceryListItem);

                GroceryListItem updated_item = item;
                updated_item.setItem_colunm(title_name_items);
                Log.d("title_name", String.valueOf(title_name_items));
                int update_item = items.updateItem(updated_item);

                groceryListItemList.add(item);

                Log.d("updated_item", String.valueOf(item.getItem_colunm()));

                GroceryList new_list = lists.getList(list_id);
                new_list.setWasEdited("false");
                int update_list = lists.updateList(new_list);

                s++;

                refresh(50);

                Log.v("value_of_s", String.valueOf(s));

                items.close();

                if (s > 2) {
                    stop_refresh();
                    s = 0;
                }
            }
        }

        for (GroceryListItem item : items.getAllItens()) {
            Log.d("all_items", "name: " + item.getItem() + " column: " + item.getItem_colunm());
        }

        title_items.setText(title_name_items);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FABAction();
            }
        });

        boolean key_exists = false;
        boolean deleted = false;

        Log.d("exists_real", String.valueOf(key_exists));
        Log.d("existed_deleted_real", String.valueOf(deleted));

        if (extras != null) {

            key_exists = extras.containsKey("new_item_name");
            deleted = extras.containsKey("deleted");

            if (key_exists && (!deleted)) {
                int idItem = Integer.parseInt(extras.getString("idItem"));
                Log.d("itemID", String.valueOf(idItem));
                GroceryListItem updated_item = items.getItem(idItem);
                updated_item.setItem(extras.getString("new_item_name"));
                updated_item.setQuantity(extras.getString("new_item_quantidade"));

                int updatedItem = items.updateItem(updated_item);

                extras.remove("new_item_name");
                extras.remove("new_item_quantidade");

                s++;

                refresh(50);

                Log.v("value_of_s", String.valueOf(s));

                items.close();

                if (s > 2) {
                    stop_refresh();
                    s = 0;
                }

            } else if (deleted && !key_exists) {
                int idItemDeleted = Integer.parseInt(extras.getString("idItemDeleted"));
                for (GroceryListItem element : groceryListItemList) {
                    if (element.getId() == idItemDeleted) {
                        groceryListItemList.remove(element);
                    }
                }
            }
        }

        for (GroceryListItem item : allItems) {
            Log.d("info", String.valueOf(item.getItem()));
        }



        for (GroceryListItem item1 : allItems) {

            if ((item1.getItem_colunm().equals(title_name_items)) && (item1.getItem() != null)) {
                groceryListItemList.add(item1);
                count = 0;
                for (GroceryListItem element : groceryListItemList) {
                    count++;
                }
                Log.d("count", String.valueOf(count));
            } else if (item1.getItem() == null) {
                items.deleteItem(item1);
            }
        }

        if (count == 0) {
            breadImage.setVisibility(View.VISIBLE);
            final int count_real = count;

            total_quantity = String.valueOf(count_real);
            Log.d("total_itemsss", total_quantity);

            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
        } else {
            breadImage.setVisibility(View.INVISIBLE);
            addItemText.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new MyAdapterItems(this, groceryListItemList);
            recyclerView.setAdapter(adapter);

            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goBack();
                }
            });
        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    final Handler handler = new Handler();

    Runnable my_runnable = new Runnable() {
        @Override
        public void run() {
            content();
        }
    };

    public void refresh(int milliseconds) {
        handler.postDelayed(my_runnable, milliseconds);
    }

    public void stop_refresh() {
        handler.removeCallbacks(my_runnable);
    }

    public void goBack() {
        marked_items = 0;
        for (GroceryListItem element : groceryListItemList) {
            if (Boolean.parseBoolean(element.getChecked())) {
                marked_items++;
            }
        }

        total_quantity = String.valueOf(count);
        final_marked_itens = String.valueOf(marked_items);

        Log.d("list_id", String.valueOf(list_id));

        Log.d("count_real_back", String.valueOf(total_quantity));
        Log.d("market_real_back", String.valueOf(final_marked_itens));

        for (GroceryListItem element : groceryListItemList) {
            Log.d("element_real", element.getItem());
        }

        GroceryList updated_list = lists.getList(list_id);
        updated_list.setTotalQuantity(final_marked_itens + "/" + total_quantity);

        int update_list = lists.updateList(updated_list);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void startAdView() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        adViewMain = (AdView) findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        if (initialBought) {
            adViewMain.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            addItemText.setVisibility(View.GONE);
            floatingActionButton2.setVisibility(View.VISIBLE);
            floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FABAction();
                }
            });
            addItemText2.setVisibility(View.VISIBLE);
        } else {
            try {
                adViewMain.loadAd(adRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void FABAction() {
        LayoutInflater layoutInflater = LayoutInflater.from(ItemsActivity.this);
        View promptView = layoutInflater.inflate(R.layout.alert_dialog, null);

        addItem = (Button) promptView.findViewById(R.id.addItemButton);
        cancelItem = (Button) promptView.findViewById(R.id.cancelItem);
        item_name = (EditText) promptView.findViewById(R.id.nome_item);
        item_quantity = (EditText) promptView.findViewById(R.id.quantidade_item);

        alertDialogBuilder = new AlertDialog.Builder(ItemsActivity.this);

        alertDialog = alertDialogBuilder.create();

        alertDialog.setView(promptView);
        alertDialog.show();

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!(item_name.getText().toString().equals(""))) && (!(item_quantity.getText().toString().equals("")))) {
                    alertDialog.dismiss();

                    String item = item_name.getText().toString();
                    String quantity = item_quantity.getText().toString();
                    items.createItem(new GroceryListItem(title_name_items, item, quantity, null));

                    for (GroceryListItem item1 : items.getAllItens()) {
                        Log.d("obtaining_new_id", String.valueOf(item1.getId()));
                    }

                    s++;

                    refresh(50);

                    if (s > 2) {
                        stop_refresh();
                        s = 0;
                    }

                } else {
                    Toast.makeText(ItemsActivity.this, "Por favor, informe o item e a quantidade.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = getIntent();
                overridePendingTransition(0, 0);
                cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(cancelIntent);
            }
        });
    }

    private void setUpUI() {
        addItemText = (TextView) findViewById(R.id.addItemText2);
        addItemText2 = (TextView) findViewById(R.id.addItemText3);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.addItem2);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewIDinItems);
        breadImage = (ImageView) findViewById(R.id.breadImage);
        title_items = (TextView) findViewById(R.id.title_name_items);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.addItem);
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button = (ImageView) findViewById(R.id.back_button);
    }

}