package com.javaislove.mycoronaguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StatsActivity extends AppCompatActivity {
    String filename = "file.mt";
    String line;
    public static String value;
    public String language = HomeActivity.language ;
    TextView textViewCases, textViewRecovered, textViewDeaths, textViewDate, textViewDeathsTitle,
            textViewRecoveredTitle, textViewActive, textViewActiveTitle, textViewNewDeaths,
            textViewNewCases, textViewNewDeathsTitle, textViewNewCasesTitle ;
    EditText textSearchBox;
    Handler handler;
    String url = "https://www.worldometers.info/coronavirus/";
    String tmpCountry, tmpCases, tmpRecovered, tmpDeaths, tmpPercentage, tmpNewCases, tmpNewDeaths;
    Document doc;
    Element countriesTable, row;
    Elements countriesRows, cols ;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Calendar myCalender;
    SimpleDateFormat myFormat;
    double tmpNumber;
    DecimalFormat generalDecimalFormat;
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    ListView listViewCountries;
    ListCountriesAdapter listCountriesAdapter;
    ArrayList<CountryLine> allCountriesResults, FilteredArrList;
    int colNumCountry, colNumCases, colNumRecovered, colNumDeaths, colNumActive, colNumNewCases, colNumNewDeaths;
    SwipeRefreshLayout mySwipeRefreshLayout;
    InputMethodManager inputMethodManager;
    Iterator<Element> rowIterator;
    ProgressBar countryProgressBar;
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            FileInputStream in = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                if ((line = bufferedReader.readLine()) != null) {
                    value = line;
                } else {
                    value = "light";
                }
            } catch (IOException e) {
                value = "light";
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            value = "light";
            e.printStackTrace();
        }
        if (value.equals("light"))
            setTheme(android.R.style.Theme_Material_Light_NoActionBar);
        else if (value.equals("dark"))
            setTheme(android.R.style.Theme_Material_NoActionBar);
        setContentView(R.layout.updated_activity_stats);
        ImageView entete = (ImageView) findViewById(R.id.entete);
        Button homebtn = (Button) findViewById(R.id.homebtn);
        textSearchBox = (EditText)findViewById(R.id.textSearchBox);
        Button changetheme = (Button) findViewById(R.id.themebtn);
        textViewCases = (TextView)findViewById(R.id.textViewCases);
        textViewRecovered = (TextView)findViewById(R.id.textViewRecovered);
        textViewDeaths = (TextView)findViewById(R.id.textViewDeaths);
        textViewDate = (TextView)findViewById(R.id.textViewDate);
        textViewRecoveredTitle = (TextView)findViewById(R.id.textViewRecoveredTitle);
        textViewDeathsTitle = (TextView)findViewById(R.id.textViewDeathsTitle);
        textViewActiveTitle = (TextView)findViewById(R.id.textViewActiveTitle);
        textViewActive = (TextView)findViewById(R.id.textViewActive);
        textViewNewDeaths = (TextView)findViewById(R.id.textViewNewDeaths);
        textViewNewCases = (TextView)findViewById(R.id.textViewNewCases);
        textViewNewCasesTitle = (TextView)findViewById(R.id.textViewNewCasesTitle);
        textViewNewDeathsTitle = (TextView)findViewById(R.id.textViewNewDeathsTitle);
        listViewCountries = (ListView)findViewById(R.id.listViewCountries);
        textSearchBox = (EditText)findViewById(R.id.textSearchBox);
        countryProgressBar = (ProgressBar) findViewById(R.id.countryProgressBar);
        colNumCountry = 0; colNumCases = 1; colNumRecovered = 0; colNumDeaths = 0; colNumNewCases = 0; colNumNewDeaths = 0;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        myFormat = new SimpleDateFormat("MMMM dd, yyyy, hh:mm:ss aaa", Locale.US);
        myCalender = Calendar.getInstance();
        handler = new Handler() ;
        generalDecimalFormat = new DecimalFormat("0.00", symbols);
        allCountriesResults = new ArrayList<CountryLine>();
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.coronaMainSwipeRefresh);
        if (value.equals("light")) {
            entete.setImageResource(R.drawable.entete);
            changetheme.setBackground(getResources().getDrawable(R.drawable.darkmodebtn));
            mySwipeRefreshLayout.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            textSearchBox.setTextColor(Color.BLACK);
        }
        else if (value.equals("dark")) {
            entete.setImageResource(R.drawable.entetedk);
            changetheme.setBackground(getResources().getDrawable(R.drawable.lightmodebtn));
            mySwipeRefreshLayout.setBackgroundColor(Color.rgb(39,39,39));
            textSearchBox.setTextColor(Color.WHITE);
        }

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        changetheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value.equals("light")) {
                    try {
                        value = "dark";
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(value.getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                    }
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else if(value.equals("dark")){
                    try {
                        value = "light";
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(value.getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                    }
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

        // Implement Swipe to Refresh
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.coronaMainSwipeRefresh);
        mySwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshData();
                    }
                }
        );

        // fix interference between scrolling in listView & parent SwipeRefreshLayout
        listViewCountries.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        if (!listIsAtTop()) mySwipeRefreshLayout.setEnabled(false);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mySwipeRefreshLayout.setEnabled(true);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
            private boolean listIsAtTop()   {
                if(listViewCountries.getChildCount() == 0) return true;
                return listViewCountries.getChildAt(0).getTop() == 0;
            }
        });

      // fetch previously saved data in SharedPreferences, if any
        if(preferences.getString("textViewCases", null) != null ){
            textViewCases.setText(preferences.getString("textViewCases", null));
            textViewRecovered.setText(preferences.getString("textViewRecovered", null));
            textViewDeaths.setText(preferences.getString("textViewDeaths", null));
            textViewDate.setText(preferences.getString("textViewDate", null));
            textViewActive.setText(preferences.getString("textViewActive", null));
            //calculate_percentages();
        }

        // Add Text Change Listener to textSearchBox to filter by Country
        textSearchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence searchSequence, int start, int before, int count) {
                FilteredArrList = new ArrayList<CountryLine>();
                if (searchSequence == null || searchSequence.length() == 0) {
                    // back to original
                    setListViewCountries(allCountriesResults);
                } else {
                    searchSequence = searchSequence.toString().toLowerCase();
                    for (int i = 0; i < allCountriesResults.size(); i++) {
                        String data = allCountriesResults.get(i).countryName;
                        if (data.toLowerCase().startsWith(searchSequence.toString())) {
                            FilteredArrList.add(new CountryLine(
                                    allCountriesResults.get(i).countryName,
                                    allCountriesResults.get(i).cases,
                                    allCountriesResults.get(i).newCases,
                                    allCountriesResults.get(i).recovered,
                                    allCountriesResults.get(i).deaths,
                                    allCountriesResults.get(i).newDeaths));
                        }
                    }
                    // set the Filtered result to return
                    setListViewCountries(FilteredArrList);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Hide keyboard after hitting done button
        textSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do something, e.g. set your TextView here via .setText()
                    inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    textSearchBox.clearFocus();
                    return true;
                }
                return false;
            }
        });

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String filtered = "";
                for (int i = start; i < end; i++) {
                    char character = source.charAt(i);
                    if (!Character.isWhitespace(character)) {
                        filtered += character;
                    }
                }

                return filtered;
            }

        };

        textSearchBox.setFilters(new InputFilter[] { filter });
        textSearchBox.clearFocus();
        // Call refreshData once the app is opened only one time, then user can request updates
        refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        textSearchBox.clearFocus();
    }

    void setListViewCountries(ArrayList<CountryLine> allCountriesResults) {
        Collections.sort(allCountriesResults);
        listCountriesAdapter = new ListCountriesAdapter(this, allCountriesResults);
        listViewCountries.setAdapter(listCountriesAdapter);
    }


    void calculate_percentages () {
        tmpNumber = Double.parseDouble(textViewRecovered.getText().toString().replaceAll(",", ""))
                / Double.parseDouble(textViewCases.getText().toString().replaceAll(",", ""))
                * 100;
        textViewRecoveredTitle.setText("Guéris   " + generalDecimalFormat.format(tmpNumber) + "%");

        tmpNumber = Double.parseDouble(textViewDeaths.getText().toString().replaceAll(",", ""))
                / Double.parseDouble(textViewCases.getText().toString().replaceAll(",", ""))
                * 100 ;
        textViewDeathsTitle.setText("Décès   " + generalDecimalFormat.format(tmpNumber) + "%");

        tmpNumber = Double.parseDouble(textViewActive.getText().toString().replaceAll(",", ""))
                / Double.parseDouble(textViewCases.getText().toString().replaceAll(",", ""))
                * 100 ;
        textViewActiveTitle.setText("Active   " + generalDecimalFormat.format(tmpNumber) + "%");
    }

    void refreshData() {
        mySwipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    doc = null; // Fetches the HTML document
                    doc = Jsoup.connect(url).timeout(10000).get();
                    // table id main_table_countries
                    countriesTable = doc.select("table").get(0);
                    countriesRows = countriesTable.select("tr");
                    //Log.e("TITLE", elementCases.text());
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // get countries
                            rowIterator = countriesRows.iterator();
                            allCountriesResults = new ArrayList<CountryLine>();

                            // read table header and find correct column number for each category
                            row = rowIterator.next();
                            cols = row.select("th");
                            //Log.e("COLS: ", cols.text());
                            if (cols.get(0).text().contains("Country")) {
                                for(int i=1; i < cols.size(); i++){
                                    if (cols.get(i).text().contains("Total") && cols.get(i).text().contains("Cases"))
                                    {colNumCases = i; Log.e("Cases: ", cols.get(i).text());}
                                    else if (cols.get(i).text().contains("Total") && cols.get(i).text().contains("Recovered"))
                                    {colNumRecovered = i; Log.e("Recovered: ", cols.get(i).text());}
                                    else if (cols.get(i).text().contains("Total") && cols.get(i).text().contains("Deaths"))
                                    {colNumDeaths = i; Log.e("Deaths: ", cols.get(i).text());}
                                    else if (cols.get(i).text().contains("Active") && cols.get(i).text().contains("Cases"))
                                    {colNumActive = i; Log.e("Active: ", cols.get(i).text());}
                                    else if (cols.get(i).text().contains("New") && cols.get(i).text().contains("Cases"))
                                    {colNumNewCases = i; Log.e("NewCases: ", cols.get(i).text());}
                                    else if (cols.get(i).text().contains("New") && cols.get(i).text().contains("Deaths"))
                                    {colNumNewDeaths = i; Log.e("NewDeaths: ", cols.get(i).text());}
                                }
                            }

                            while (rowIterator.hasNext()) {
                                row = rowIterator.next();
                                cols = row.select("td");
                                if (cols.get(0).text().contains("World")) {
                                    textViewCases.setText(cols.get(colNumCases).text());
                                    textViewRecovered.setText(cols.get(colNumRecovered).text());
                                    textViewDeaths.setText(cols.get(colNumDeaths).text());

                                    if (cols.get(colNumActive).hasText()) {textViewActive.setText(cols.get(colNumActive).text());}
                                    else {textViewActive.setText("0");}
                                    if (cols.get(colNumNewCases).hasText()) {textViewNewCases.setText(cols.get(colNumNewCases).text());}
                                    else {textViewNewCases.setText("0");}
                                    if (cols.get(colNumNewDeaths).hasText()) {textViewNewDeaths.setText(cols.get(colNumNewDeaths).text());}
                                    else {textViewNewDeaths.setText("0");}
                                    continue;
                                } else if (
                                        cols.get(0).text().contains("Total") ||
                                                cols.get(0).text().contains("Europe") ||
                                                cols.get(0).text().contains("North America") ||
                                                cols.get(0).text().contains("Asia") ||
                                                cols.get(0).text().contains("South America") ||
                                                cols.get(0).text().contains("Africa") ||
                                                cols.get(0).text().contains("Oceania")
                                ) {
                                    continue;
                                }

                                if (cols.get(colNumCountry).hasText()) {tmpCountry = cols.get(0).text();}
                                else {tmpCountry = "NA";}

                                if (cols.get(colNumCases).hasText()) {tmpCases = cols.get(colNumCases).text();}
                                else {tmpCases = "0";}

                                if (cols.get(colNumRecovered).hasText()){
                                    if(!cols.get(colNumRecovered).text().contains("N/A")) {
                                        tmpRecovered = cols.get(colNumRecovered).text();
                                        tmpPercentage = (generalDecimalFormat.format(Double.parseDouble(tmpRecovered.replaceAll(",", ""))
                                                / Double.parseDouble(tmpCases.replaceAll(",", ""))
                                                * 100)) + "%";
                                        tmpRecovered = tmpRecovered + "\n" + tmpPercentage;
                                    }
                                    else {tmpRecovered = "NA";}
                                }
                                else {tmpRecovered = "0";}

                                if(cols.get(colNumDeaths).hasText()) {
                                    if(!cols.get(colNumDeaths).text().contains("N/A")) {
                                        tmpDeaths = cols.get(colNumDeaths).text();
                                        tmpPercentage = (generalDecimalFormat.format(Double.parseDouble(tmpDeaths.replaceAll(",", ""))
                                                / Double.parseDouble(tmpCases.replaceAll(",", ""))
                                                * 100)) + "%";
                                        tmpDeaths = tmpDeaths + "\n" + tmpPercentage;
                                    }
                                    else {tmpDeaths = "NA";}
                                }
                                else {tmpDeaths = "0";}

                                if (cols.get(colNumNewCases).hasText()) {tmpNewCases = cols.get(colNumNewCases).text();}
                                else {tmpNewCases = "0";}

                                if (cols.get(colNumNewDeaths).hasText()) {tmpNewDeaths = cols.get(colNumNewDeaths).text();}
                                else {tmpNewDeaths = "0";}

                                allCountriesResults.add(new CountryLine(tmpCountry, tmpCases, tmpNewCases, tmpRecovered, tmpDeaths, tmpNewDeaths));
                            }

                            setListViewCountries(allCountriesResults);
                            textSearchBox.setText(null);
                            textSearchBox.clearFocus();

                            // save results
                            editor.putString("textViewCases", textViewCases.getText().toString());
                            editor.putString("textViewRecovered", textViewRecovered.getText().toString());
                            editor.putString("textViewActive", textViewActive.getText().toString());
                            editor.putString("textViewDeaths", textViewDeaths.getText().toString());
                            editor.putString("textViewDate", textViewDate.getText().toString());
                            editor.apply();

                            calculate_percentages();

                            myCalender = Calendar.getInstance();
                            textViewDate.setText("Last updated: " + myFormat.format(myCalender.getTime()));
                        }
                    });
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(StatsActivity.this, "Network Connection Error!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                finally {
                    doc = null;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mySwipeRefreshLayout.setRefreshing(false);
                    }});
            }
        }).start();
    }
}

