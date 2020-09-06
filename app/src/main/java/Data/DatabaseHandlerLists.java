package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import Model.GroceryList;
import Util.Lists;
import Util.Util;

public class DatabaseHandlerLists extends SQLiteOpenHelper {

    public DatabaseHandlerLists(@Nullable Context context) {
        super(context, Lists.DATABASE_NAME, null, Lists.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_LISTS_DATABASE = "CREATE TABLE " + Lists.TABLE_NAME + "(" + Lists.KEY_ID2 + " INTEGER PRIMARY KEY," + Lists.KEY_LIST_NAME + " TEXT," +
                Lists.KEY_LIST_NAME_DB + " TEXT," + Lists.KEY_TOTAL_QUANTITY + " TEXT," + Lists.KEY_EDITED + " TEXT, "
                + Lists.KEY_OUTDATED_NAME + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_LISTS_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Lists.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void createList(GroceryList list) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Lists.KEY_LIST_NAME, list.getListName());
        contentValues.put(Lists.KEY_LIST_NAME_DB, list.getDatabaseName());
        contentValues.put(Lists.KEY_TOTAL_QUANTITY, list.getTotalQuantity());
        contentValues.put(Lists.KEY_EDITED, list.getWasEdited());
        contentValues.put(Lists.KEY_OUTDATED_NAME, list.getOutdated_name());

        db.insert(Lists.TABLE_NAME, null, contentValues);
        db.close();

    }

    public int updateList(GroceryList list) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Lists.KEY_LIST_NAME, list.getListName());
        contentValues.put(Lists.KEY_LIST_NAME_DB, list.getTotalQuantity());
        contentValues.put(Lists.KEY_TOTAL_QUANTITY, list.getTotalQuantity());
        contentValues.put(Lists.KEY_EDITED, list.getWasEdited());
        contentValues.put(Lists.KEY_OUTDATED_NAME, list.getOutdated_name());

        return db.update(Lists.TABLE_NAME, contentValues, Lists.KEY_ID2 + "=?", new String[] {String.valueOf(list.getId())});

    }

    public GroceryList getList(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Lists.TABLE_NAME, new String[] {Lists.KEY_ID2, Lists.KEY_LIST_NAME, Lists.KEY_LIST_NAME_DB,
                        Lists.KEY_TOTAL_QUANTITY, Lists.KEY_EDITED, Lists.KEY_OUTDATED_NAME}, Lists.KEY_ID2 + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        GroceryList groceryList = new GroceryList(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        return groceryList;
    }

    public List<GroceryList> getAllLists() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<GroceryList> allLists = new ArrayList<>();

        String selectAll = "SELECT * FROM " + Lists.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                GroceryList list = new GroceryList();
                list.setId(Integer.parseInt(cursor.getString(0)));
                list.setListName(cursor.getString(1));
                list.setDatabaseName(cursor.getString(2));
                list.setTotalQuantity(cursor.getString(3));
                list.setWasEdited(cursor.getString(4));
                list.setOutdated_name(cursor.getString(5));

                allLists.add(list);

            } while (cursor.moveToNext());
        }

        return allLists;
    }

    public void deleteList(GroceryList groceryList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Lists.TABLE_NAME, Lists.KEY_ID2 + "=?", new String[] {String.valueOf(groceryList.getId())});

        db.close();
    }

    public int countLists() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Lists.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        return cursor.getCount();
    }


}
