package Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starlist.AddListaActivity;
import com.example.starlist.ItemsActivity;
import com.example.starlist.MainActivity;
import com.example.starlist.R;

import java.util.List;

import Data.DatabaseHandler;
import Data.DatabaseHandlerLists;
import Model.GroceryList;
import Model.GroceryListItem;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<GroceryList> groceryListList;

    public MyAdapter(Context context, List<GroceryList> groceryListList) {
        this.context = context;
        this.groceryListList = groceryListList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

        GroceryList list = groceryListList.get(position);
        holder.list_name.setText(list.getListName());
        holder.filBar.setText(list.getTotalQuantity());

    }

    @Override
    public int getItemCount() {
        return groceryListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView list_name;
        private TextView filBar;
        private ImageView moreActions;
        private List<GroceryListItem> items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            final DatabaseHandlerLists databaseHandlerLists = new DatabaseHandlerLists(context);
            final DatabaseHandler databaseHandler = new DatabaseHandler(context);

            list_name = (TextView) itemView.findViewById(R.id.listName);
            filBar = (TextView) itemView.findViewById(R.id.fillBar);

            moreActions = (ImageView) itemView.findViewById(R.id.moreActions);
            moreActions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.more_options, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            int position = getAdapterPosition();

                            GroceryList list = groceryListList.get(position);

                            switch (menuItem.getItemId()) {
                                case R.id.editID:
                                    Intent editIntent = new Intent(context, AddListaActivity.class);
                                    editIntent.putExtra("id", String.valueOf(list.getId()));
                                    editIntent.putExtra("dbName", list.getDatabaseName());
                                    editIntent.putExtra("listName", list.getListName());
                                    editIntent.putExtra("totalQuantity", list.getTotalQuantity());
                                    editIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(editIntent);
                                    ((MainActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                                    return true;
                                case R.id.deleteID:
                                    String list_name = list.getListName();
                                    items = databaseHandler.getAllItens();

                                    for (GroceryListItem item : items) {
                                        if (item.getItem_colunm().equals(list_name)) {
                                            databaseHandler.deleteItem(item);
                                        }
                                    }

                                    databaseHandlerLists.deleteList(list);
                                    Intent deleteIntent = new Intent(context, MainActivity.class);
                                    ((MainActivity) context).overridePendingTransition(0, 0);
                                    deleteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    ((MainActivity)context).finish();
                                    deleteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ((MainActivity) context).overridePendingTransition(0, 0);
                                    context.startActivity(deleteIntent);

                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    popupMenu.show();

                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            GroceryList list = groceryListList.get(position);

            Intent enterListIntent = new Intent(context, ItemsActivity.class);
            enterListIntent.putExtra("list_name", list.getListName());
            enterListIntent.putExtra("list_id", list.getId());
            enterListIntent.putExtra("outdated_name", String.valueOf(list.getOutdated_name()));
            Log.d("outdated_list_thing", String.valueOf(list.getOutdated_name()));
            Log.d("outdated_list_thing", list.getListName());
            Log.d("outdated_list_thing", list.getWasEdited());
            enterListIntent.putExtra("edited", Boolean.parseBoolean(list.getWasEdited()));

            ((MainActivity)context).finish();
            enterListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(enterListIntent);
            ((MainActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

}
