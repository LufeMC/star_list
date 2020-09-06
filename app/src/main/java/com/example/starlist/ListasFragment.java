package com.example.starlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Adapter.MyAdapter;
import Data.DatabaseHandlerLists;
import Model.GroceryList;

public class ListasFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private TextView addListText;
    private Context context;
    private ImageView logoMiddle;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<GroceryList> groceryListLists;
    private DatabaseHandlerLists listsDatabase;
    private View view;
    private int s = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_listas, container, false);

        content();

        return view;
    }

    private void content() {

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.addLista);

        addListText = (TextView) view.findViewById(R.id.addListText);
        addListText.setText(getResources().getString(R.string.addListText));

        listsDatabase = new DatabaseHandlerLists(getActivity());

        logoMiddle = (ImageView) view.findViewById(R.id.logoMiddle);

        context = getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewID);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        groceryListLists = listsDatabase.getAllLists();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, AddListaActivity.class);
                startActivity(intent);
                ((MainActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        if (listsDatabase.countLists() == 0) {
            logoMiddle.setVisibility(View.VISIBLE);
        } else {
            logoMiddle.setVisibility(View.INVISIBLE);
            addListText.setVisibility(View.INVISIBLE);

            recyclerView.setVisibility(View.VISIBLE);

            adapter = new MyAdapter(context, groceryListLists);
            recyclerView.setAdapter(adapter);
        }

        total_refresh();
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

    private void total_refresh() {
        s++;

        refresh(100);

        if (s > 2) {
            stop_refresh();
            s = 0;
        }
    }
}