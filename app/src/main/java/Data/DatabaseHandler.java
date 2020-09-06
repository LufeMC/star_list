package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import Model.GroceryListItem;
import Util.Util;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_ITEM_DATABASE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.KEY_ID + " INTEGER PRIMARY KEY," + Util.KEY_LIST_NAME_ITEM +
                " TEXT, " + Util.KEY_ITEM + " TEXT," + Util.KEY_QUANTITY + " TEXT," + Util.KEY_CHECKED + " Text)";

        sqLiteDatabase.execSQL(CREATE_ITEM_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void deleteDB(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);
    }

    public void createItem(GroceryListItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_LIST_NAME_ITEM, item.getItem_colunm());
        contentValues.put(Util.KEY_ITEM, item.getItem());
        contentValues.put(Util.KEY_QUANTITY, item.getQuantity());
        contentValues.put(Util.KEY_CHECKED, item.getChecked());

        db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();

    }

    public int updateItem(GroceryListItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_LIST_NAME_ITEM, item.getItem_colunm());
        contentValues.put(Util.KEY_ITEM, item.getItem());
        contentValues.put(Util.KEY_QUANTITY, item.getQuantity());
        contentValues.put(Util.KEY_CHECKED, item.getChecked());

        return db.update(Util.TABLE_NAME, contentValues, Util.KEY_ID + "=?", new String[] {String.valueOf(item.getId())});

    }

    public GroceryListItem getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME, new String[] {Util.KEY_ID, Util.KEY_LIST_NAME_ITEM,  Util.KEY_ITEM, Util.KEY_QUANTITY, Util.KEY_CHECKED},
                Util.KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        GroceryListItem groceryListItem = new GroceryListItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4));

        return groceryListItem;
    }

    public List<GroceryListItem> getAllItens() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<GroceryListItem> allItems = new ArrayList<>();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                GroceryListItem item = new GroceryListItem();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setItem_colunm(cursor.getString(1));
                item.setItem(cursor.getString(2));
                item.setQuantity(cursor.getString(3));
                item.setChecked(cursor.getString(4));

                allItems.add(item);

            } while (cursor.moveToNext());
        }

        return allItems;
    }

    public void deleteItem(GroceryListItem groceryListItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?", new String[] {String.valueOf(groceryListItem.getId())});

        db.close();
    }

    public int countItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        return cursor.getCount();
    }
}
