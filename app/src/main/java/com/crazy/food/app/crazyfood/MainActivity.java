package com.crazy.food.app.crazyfood;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    String BASEURLMENU;
    @BindView(R.id.grid_main_recycler) RecyclerView rvmFoodList;
    @BindView(R.id.main_grid_layout)GridLayout gridLayout;
    String bundleKey = "MyBKey";
    GridLayoutManager gridLayoutManager;
    ArrayList<MyUserMeal> jsonFoodImages;
    ArrayList<MyUserLookup> jsonFoodSpecific;
    String bundleValue = "Starter";
    String s="";
    private Cursor cursorData;
    private int mIdCol, mTitleCol, mCategoryCol, mAreaCol, mBackgroundCol;
    int scrollpos=0;
    final int LOADERID=47;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BASEURLMENU = getString(R.string.mealurl);
        ButterKnife.bind(this);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getString(R.string.no_internet_display))
                    .setPositiveButton(getString(R.string.accepted), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

        } else {
            Snackbar.make(gridLayout, getString(R.string.loading), Snackbar.LENGTH_SHORT).show();
            if (savedInstanceState != null) {
                if (savedInstanceState.getString(bundleKey) != getString(R.string.favomenu)) {
                    bundleValue = savedInstanceState.getString(bundleKey);
                    new FoodData().execute(bundleValue);
                } else if (savedInstanceState.getString(bundleKey) == getString(R.string.favomenu)) {
                    bundleValue = savedInstanceState.getString(bundleKey);
                    getSupportLoaderManager().restartLoader(LOADERID,null,this);
                }
            } else {
                new FoodData().execute(bundleValue);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        scrollpos=gridLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(getString(R.string.scrollposition),scrollpos);
        outState.putString(bundleKey, bundleValue);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        scrollpos=savedInstanceState.getInt(getString(R.string.scrollposition));
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (bundleValue) {
            case "Starter":
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Snackbar.make(gridLayout, R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData = new FoodData();
                    foodData.execute(bundleValue);
                }
                break;
            case "Vegetarian":
                ConnectivityManager conMgr1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo1 = conMgr1.getActiveNetworkInfo();
                if (netInfo1 == null) {
                    Snackbar.make(gridLayout, R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData1 = new FoodData();
                    foodData1.execute(bundleValue);
                }
                break;
            case "Desert":
                ConnectivityManager conMgr2 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo2 = conMgr2.getActiveNetworkInfo();
                if (netInfo2 == null) {
                    Snackbar.make(gridLayout,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData2 = new FoodData();
                    foodData2.execute(bundleValue);
                }
                break;
            case "favourites":
                getSupportLoaderManager().restartLoader(LOADERID,null,this);
                break;
            case "Chicken":
                ConnectivityManager conMgr3 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo3 = conMgr3.getActiveNetworkInfo();
                if (netInfo3 == null) {
                    Snackbar.make(gridLayout,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData3 = new FoodData();
                    foodData3.execute(bundleValue);
                }
                break;
            case "SeaFood":
                ConnectivityManager conMgr4 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo4 = conMgr4.getActiveNetworkInfo();
                if (netInfo4 == null) {
                    Snackbar.make(gridLayout,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData4 = new FoodData();
                    foodData4.execute(bundleValue);
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            public Cursor loadInBackground() {
                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(MyMealContract.FavMealEntry.CONTENT_URI,
                        null, null, null, null);
                return cursor;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<MyUserMeal> cursorMealData = new ArrayList<MyUserMeal>();
        cursorData = data;
        mIdCol = cursorData.getColumnIndex(MyMealContract.FavMealEntry.COLUMN_MEALID);
        mTitleCol = cursorData.getColumnIndex(MyMealContract.FavMealEntry.COLUMN_TITLE);
        mCategoryCol = cursorData.getColumnIndex(MyMealContract.FavMealEntry.COLUMN_CATEGORY);
        mAreaCol = cursorData.getColumnIndex(MyMealContract.FavMealEntry.COLUMN_AREA);
        mBackgroundCol = cursorData.getColumnIndex(MyMealContract.FavMealEntry.COLUMN_BACKGROUND_IMAGE);
        while (cursorData.moveToNext()) {
            String mId = cursorData.getString(mIdCol);
            String mTitle = cursorData.getString(mTitleCol);
            String mCategory = cursorData.getString(mCategoryCol);
            String mArea = cursorData.getString(mAreaCol);
            String mBackground = cursorData.getString(mBackgroundCol);
            cursorMealData.add(new MyUserMeal(mTitle,mBackground,mId));
        }
        cursorData.close();
        if (cursorMealData.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.nofavdisplay);
            builder.setPositiveButton(R.string.accepted, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        } else {
            gridLayoutManager=new GridLayoutManager(MainActivity.this, 2);
            rvmFoodList.setLayoutManager(gridLayoutManager);
            rvmFoodList.setAdapter(new MyFoodList(MainActivity.this, cursorMealData));
            rvmFoodList.scrollToPosition(scrollpos);
            Toast.makeText(MainActivity.this, getString(R.string.no_of_favourites) + cursorData.getCount(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



    public class FoodData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            jsonFoodImages = new ArrayList<MyUserMeal>();
            HttpUrlResponse responseConnection = new HttpUrlResponse();
            URL url = responseConnection.buildURL(BASEURLMENU, strings[0]);
            String response = null;
            try {
                response = responseConnection.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.mealsarray));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject foodDetail = jsonArray.getJSONObject(i);
                    String strMeal = foodDetail.getString(getString(R.string.strMeal));
                    String strMealThumb = foodDetail.getString(getString(R.string.strMealThumb));
                    String idMeal = foodDetail.getString(getString(R.string.idMeal));
                    jsonFoodImages.add(new MyUserMeal(strMeal, strMealThumb, idMeal));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            gridLayoutManager=new GridLayoutManager(MainActivity.this,2);
            rvmFoodList.setLayoutManager(gridLayoutManager);
            rvmFoodList.setAdapter(new MyFoodList(MainActivity.this,jsonFoodImages));
            rvmFoodList.scrollToPosition(scrollpos);
        }
    }

    public void loading(final String s,final String action){
        Snackbar.make(gridLayout, s, Snackbar.LENGTH_SHORT).setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s= getString(R.string.d);
                final String action = "";
                //loading(s,action);
                new FoodData().execute(getString(R.string.sry));

            }
        }).show();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemThatClicked = menuItem.getItemId();
        switch (itemThatClicked) {
            case R.id.menu_starters:
                bundleValue=getString(R.string.startermenu);
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Snackbar.make(gridLayout,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData = new FoodData();
                    foodData.execute(bundleValue);
                }
                break;
            case R.id.menu_vegetarian:
                bundleValue=getString(R.string.vegeimenu);
                ConnectivityManager conMgr1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo1 = conMgr1.getActiveNetworkInfo();
                if (netInfo1 == null) {
                    Snackbar.make(gridLayout,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData1 = new FoodData();
                    foodData1.execute(bundleValue);
                }
                break;
            case R.id.menu_desert:
                bundleValue=getString(R.string.desertmenu);
                ConnectivityManager conMgr2 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo2 = conMgr2.getActiveNetworkInfo();
                if (netInfo2 == null) {
                    Snackbar.make(gridLayout,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData2 = new FoodData();
                    foodData2.execute(bundleValue);
                }
                break;
            case R.id.menu_favourites:
                bundleValue=getString(R.string.favomenu);
                getSupportLoaderManager().restartLoader(LOADERID,null,this);
                break;
            case R.id.menu_chicken:
                bundleValue=getString(R.string.chickenmenu);
                ConnectivityManager conMgr3 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo3 = conMgr3.getActiveNetworkInfo();
                if (netInfo3 == null) {
                    Snackbar.make(gridLayout,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData3 = new FoodData();
                    foodData3.execute(bundleValue);
                }
                break;
            case R.id.menu_seafood:
                bundleValue=getString(R.string.seafoodmenu);
                ConnectivityManager conMgr4 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo4 = conMgr4.getActiveNetworkInfo();
                if (netInfo4 == null) {
                    Snackbar.make(gridLayout,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
                }else {
                    FoodData foodData4 = new FoodData();
                    foodData4.execute(bundleValue);
                }
                break;
            case R.id.menu_nonveg:
                Toast.makeText(this, R.string.choose_a_non_veg, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
