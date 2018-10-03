package com.crazy.food.app.crazyfood;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by DELL on 25-06-2018.
 */

public class MyMealContract {
    public static final String AUTHORITY = "com.crazy.food.app.crazyfood";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "mealsTable";

    public static final class FavMealEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();
        public static final String TABLE_NAME = "mealsTable";
        public static final String COLUMN_MEALID = "MealId";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_CATEGORY = "Category";
        public static final String COLUMN_AREA = "Area";
        public static final String COLUMN_BACKGROUND_IMAGE = "BackgroundImage";
    }
}
