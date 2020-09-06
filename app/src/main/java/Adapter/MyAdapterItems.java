package Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starlist.ItemsActivity;
import com.example.starlist.R;

import java.util.List;

import Data.DatabaseHandler;
import Model.GroceryListItem;

public class MyAdapterItems extends RecyclerView.Adapter<MyAdapterItems.ViewHolder> {

    private Context context;
    private List<GroceryListItem> groceryListItems;

    public MyAdapterItems(Context context, List<GroceryListItem> groceryListItems) {
        this.context = context;
        this.groceryListItems = groceryListItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterItems.ViewHolder holder, int position) {

        GroceryListItem item = groceryListItems.get(position);
        holder.item_name.setText(item.getItem());
        holder.quantity.setText(item.getQuantity());
        holder.checkBox.setChecked(Boolean.parseBoolean(item.getChecked()));
        Log.d("is_checked", String.valueOf(Boolean.parseBoolean(item.getChecked())));

    }

    @Override
    public int getItemCount() {
        return groceryListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView item_name;
        private TextView quantity;
        private CheckBox checkBox;
        private ImageView moreOptionItems;
        private Button addItem;
        private TextView textViewEdit;
        private Button cancelItem;
        private EditText nameItem;
        private EditText item_quantity;
        private AlertDialog.Builder alertDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            final DatabaseHandler databaseHandler = new DatabaseHandler(context);

            item_name = (TextView) itemView.findViewById(R.id.item_name);
            quantity = (TextView) itemView.findViewById(R.id.quantity);

            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    DatabaseHandler items = new DatabaseHandler(context);

                    int checkBoxPosition = getAdapterPosition();
                    final GroceryListItem checkBoxPosItem = groceryListItems.get(checkBoxPosition);

                    if (b) {
                        Boolean marked_items = true;
                        checkBoxPosItem.setChecked(String.valueOf(marked_items));
                        Intent intent = new Intent(context, ItemsActivity.class);
                        intent.putExtra("completed_items", marked_items);

                        int updated_item = items.updateItem(checkBoxPosItem);
                    } else {
                        Boolean marked_items = false;
                        checkBoxPosItem.setChecked(String.valueOf(marked_items));
                        Intent intent = new Intent(context, ItemsActivity.class);
                        intent.putExtra("completed_items", marked_items);

                        int updated_item = items.updateItem(checkBoxPosItem);
                    }

                    Log.d("is_checked", String.valueOf(Boolean.parseBoolean(checkBoxPosItem.getChecked())));
                }
            });

            moreOptionItems = (ImageView) itemView.findViewById(R.id.moreActionsinItems);
            moreOptionItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    MenuInflater menuInflater = popupMenu.getMenuInflater();
                    menuInflater.inflate(R.menu.more_options_in_items, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()) {
                                case (R.id.editIDItems):

                                    int position = getAdapterPosition();

                                    final GroceryListItem item = groceryListItems.get(position);

                                    LayoutInflater layoutInflater = LayoutInflater.from(((ItemsActivity)context));
                                    View promptView = layoutInflater.inflate(R.layout.alert_dialog, null);

                                    addItem = (Button) promptView.findViewById(R.id.addItemButton);
                                    cancelItem = (Button) promptView.findViewById(R.id.cancelItem);
                                    item_name = (EditText) promptView.findViewById(R.id.nome_item);
                                    item_quantity = (EditText) promptView.findViewById(R.id.quantidade_item);
                                    textViewEdit = (TextView) promptView.findViewById(R.id.textViewEdit);

                                    textViewEdit.setText(R.string.editar_item);

                                    item_name.setText(item.getItem());
                                    item_quantity.setText(item.getQuantity());
                                    addItem.setText(R.string.confirmar);

                                    alertDialog = new AlertDialog.Builder(((ItemsActivity)context));

                                    addItem.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if ((!(item_name.getText().toString().equals(""))) && (!(item_quantity.getText().toString().equals("")))) {
                                                Intent addItemIntent = ((ItemsActivity)context).getIntent();
                                                addItemIntent.putExtra("idItem", String.valueOf(item.getId()));
                                                addItemIntent.putExtra("new_item_name", item_name.getText().toString());
                                                addItemIntent.putExtra("new_item_quantidade", item_quantity.getText().toString());
                                                Log.d("new_item_name", item_name.getText().toString());
                                                ((ItemsActivity)context).overridePendingTransition(0, 0);
                                                addItemIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                ((ItemsActivity)context).finish();
                                                ((ItemsActivity)context).overridePendingTransition(0, 0);
                                                context.startActivity(addItemIntent);
                                            } else {
                                                Toast.makeText(((ItemsActivity)context), "Por favor, informe o item e a quantidade.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    cancelItem.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent cancelIntent = ((ItemsActivity)context).getIntent();
                                            ((ItemsActivity)context).overridePendingTransition(0, 0);
                                            cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            ((ItemsActivity)context).finish();
                                            ((ItemsActivity)context).overridePendingTransition(0, 0);
                                            context.startActivity(cancelIntent);
                                        }
                                    });

                                    alertDialog.setView(promptView);
                                    alertDialog.create();

                                    alertDialog.show();

                                    return true;

                                case (R.id.deleteIDItems):

                                    int positiondelete = getAdapterPosition();

                                    final GroceryListItem itemdelete = groceryListItems.get(positiondelete);

                                    Log.d("itemID", String.valueOf(itemdelete.getId()));

                                    Intent deleteIntent = ((ItemsActivity)context).getIntent();
                                    deleteIntent.putExtra("deleted", true);
                                    deleteIntent.putExtra("idItemDeleted", String.valueOf(itemdelete.getId()));
                                    deleteIntent.putExtra("nameItemDeleted", itemdelete.getItem());
                                    deleteIntent.putExtra("nameDBItemDeleted", itemdelete.getItem_colunm());
                                    deleteIntent.putExtra("quantityItemDeleted", itemdelete.getQuantity());
                                    databaseHandler.deleteItem(itemdelete);
                                    ((ItemsActivity) context).overridePendingTransition(0, 0);
                                    deleteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    ((ItemsActivity)context).finish();
                                    ((ItemsActivity) context).overridePendingTransition(0, 0);
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
    }
}
