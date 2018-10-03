package com.crazy.food.app.crazyfood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MealDbHelper extends SQLiteOpenHelper {
    public static final int version=1;
    public Context c;
    public MealDbHelper(Context context) {
        super(context, MyMealContract.FavMealEntry.TABLE_NAME, null, version);
        c = context;
    }

    public Void showFavoriteMeals() {
        //ArrayList<MyUserMeal> mealdbinfo = new ArrayList<>();
        String queryselected = "SELECT * FROM " + MyMealContract.FavMealEntry.TABLE_NAME;
        SQLiteDatabase database = getReadableDatabase();
        @SuppressLint("Recycle") Cursor mealdetails = database.rawQuery(queryselected, null);
        String[] data = new String[10];
        String p = "";
        if (mealdetails.moveToFirst()) {
            do {
                data[0] = String.valueOf(mealdetails.getInt(0));
                data[1] = mealdetails.getString(1);
                data[2] = mealdetails.getString(2);
                data[3] = mealdetails.getString(3);
                data[4] = mealdetails.getString(4);
                p = p + data[1] + "\n" + data[2] + "\n" + data[3] + "\n" + data[4] + "\n" + data[5] + "\n" + data[6] + "\n";
            } while (mealdetails.moveToNext());
        }
        database.close();
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + MyMealContract.FavMealEntry.TABLE_NAME + "("
                + MyMealContract.FavMealEntry.COLUMN_MEALID + " INTEGER PRIMARY KEY" + ","
                + MyMealContract.FavMealEntry.COLUMN_TITLE + " TEXT" + ","
                + MyMealContract.FavMealEntry.COLUMN_CATEGORY + " TEXT" + ","
                + MyMealContract.FavMealEntry.COLUMN_AREA + " TEXT" + ","
                + MyMealContract.FavMealEntry.COLUMN_BACKGROUND_IMAGE + " TEXT" + ")";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyMealContract.FavMealEntry.TABLE_NAME);
        onCreate(db);
    }
}
