package com.crazy.food.app.crazyfood;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondFoodDetails extends AppCompatActivity {
    ContentValues mealcontentValues;
    String BASEURLMENU;
    ArrayList<String> foodDetails;
    String sid;
    ScrollView scrollView;
    @BindView(R.id.second_backgraph)
    ImageView background_image;
    @BindView(R.id.id_second_title)
    TextView title;
    @BindView(R.id.id_second_tv_playvideo)
    TextView playvideo_tv;
    @BindView(R.id.id_second_category)
    TextView category;
    @BindView(R.id.id_second_area)
    TextView area;
    @BindView(R.id.id_second_instructions)
    TextView tv_instructions;
    @BindView(R.id.second_fav_img)
    ImageView favImage;
    boolean check = false;
    ArrayList<MyUserLookup> jsonUserLookup;
    MyUserLookup myUserLookup;
    String widInstructions, widTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_food_details);
        BASEURLMENU = getString(R.string.mealdburl);
        ButterKnife.bind(this);
        scrollView = findViewById(R.id.id_second_layout_scrollview);
        foodDetails = getIntent().getStringArrayListExtra(getString(R.string.jTitlekey));
        sid = foodDetails.get(2);
        Picasso.with(this).load(foodDetails.get(0)).placeholder(R.mipmap.ic_launcher_round).into(background_image);
        title.setText(foodDetails.get(1));
        Uri urisel = Uri.parse(MyMealContract.FavMealEntry.CONTENT_URI + "/*");
        @SuppressLint("Recycle") Cursor selcur = getContentResolver().query(urisel, null, sid, null, null);
        if (selcur.getCount() > 0) {
            check = true;
            favImage.setImageResource(R.mipmap.coloredfavimage);
        } else {
            check = false;
            favImage.setImageResource(R.mipmap.favimage);
        }
        Snackbar.make(scrollView, R.string.cilick_to_add_widget, Snackbar.LENGTH_LONG).show();
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            new FoodData(this).execute("");
        }
    }

    public void addFavListImg(View view) {
        if (!check) {
            check = true;
            mealcontentValues = new ContentValues();
            mealcontentValues.put(MyMealContract.FavMealEntry.COLUMN_MEALID, foodDetails.get(2));
            mealcontentValues.put(MyMealContract.FavMealEntry.COLUMN_BACKGROUND_IMAGE, foodDetails.get(0));
            mealcontentValues.put(MyMealContract.FavMealEntry.COLUMN_TITLE, foodDetails.get(1));
            mealcontentValues.put(MyMealContract.FavMealEntry.COLUMN_CATEGORY, myUserLookup.getStrCategory());
            mealcontentValues.put(MyMealContract.FavMealEntry.COLUMN_AREA, myUserLookup.getStrArea());
            Uri uri = getContentResolver().insert(Uri.parse(MyMealContract.FavMealEntry.CONTENT_URI + ""), mealcontentValues);
            if (uri != null) {
                favImage.setImageResource(R.mipmap.coloredfavimage);
                new MealDbHelper(this).showFavoriteMeals();
                Toast.makeText(this, R.string.favadding, Toast.LENGTH_SHORT).show();
            }
        } else {
            check = false;
            getContentResolver().delete(MyMealContract.FavMealEntry.CONTENT_URI, MyMealContract.FavMealEntry.COLUMN_MEALID + " =? ", new String[]{foodDetails.get(2)});
            favImage.setImageResource(R.mipmap.favimage);
            Toast.makeText(this, R.string.favremoving, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class FoodData extends AsyncTask<String, Void, String> {
        Context context;
        public FoodData(Context c) {
            this.context = c;
        }
        @Override
        protected String doInBackground(String... strings) {
            jsonUserLookup = new ArrayList<MyUserLookup>();
            HttpUrlResponse responseConnection = new HttpUrlResponse();
            URL url = responseConnection.buildLookupUrl(BASEURLMENU, sid);
            String response = null;
            try {
                response = responseConnection.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.optJSONArray(getString(R.string.mealjsonarray));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject foodSpecific = jsonArray.optJSONObject(i);
                    String idMeal = foodSpecific.optString(getString(R.string.idmealjsonn));
                    String strMeal = foodSpecific.optString(getString(R.string.strmealjson));
                    String strCategory = foodSpecific.optString(getString(R.string.categoryjson));
                    String strArea = foodSpecific.optString(getString(R.string.areajson));
                    String strInstructions = foodSpecific.optString(getString(R.string.instructionsjson));
                    String strMealThumb = foodSpecific.optString(getString(R.string.mealthumdjson));
                    String strYoutube = foodSpecific.optString(getString(R.string.youtubejson));
                    myUserLookup = new MyUserLookup(idMeal, strMeal, strCategory, strArea, strInstructions, strMealThumb, strYoutube);
                    jsonUserLookup.add(myUserLookup);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            for (int i = 0; i < jsonUserLookup.size(); i++) {
                if (jsonUserLookup.get(i).getStrCategory().isEmpty()) {
                    category.setText(R.string.nocategory);
                }else {
                    category.setText(jsonUserLookup.get(i).getStrCategory());
                }if (jsonUserLookup.get(i).getStrArea().isEmpty()) {
                    area.setText(R.string.noarea);
                }else {
                    area.setText(jsonUserLookup.get(i).getStrArea());
                } if (jsonUserLookup.get(i).getStrInstructions().isEmpty()) {
                    tv_instructions.setText(R.string.insempty);
                }else {
                    tv_instructions.setText(jsonUserLookup.get(i).getStrInstructions());
                }
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    playvideo_tv.setText("");
                } else {
                    playvideo_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(myUserLookup.getStrYoutube());
                            String s = uri.toString();
                            String[] parts = s.split("=");
                            Intent i = new Intent(context, YoutubeActivity.class);
                            i.putExtra(getString(R.string.youtubeparsekey), parts[1]);
                            startActivity(i);
                        }
                    });
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemThatClicked = menuItem.getItemId();
        switch (itemThatClicked) {
            case R.id.id_seccond_menu_widget:
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                   Snackbar.make(scrollView, R.string.nonetcantaddtowidget,Snackbar.LENGTH_SHORT).show();
                }else {
                    widTitle = foodDetails.get(1);
                    widInstructions = myUserLookup.getStrInstructions();
                    MealWidget mealWidget = new MealWidget();
                    menuItem.setVisible(false);
                    mealWidget.mealWidtitle = getString(R.string.title) + "\t" + widTitle + "\n" + getString(R.string.instructions) + widInstructions;
                    Toast.makeText(this, R.string.widgetadding, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(menuItem);

    }
}
