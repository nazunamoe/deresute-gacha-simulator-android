package com.nazunamoe.deresutegachasimulatorm.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nazunamoe.deresutegachasimulatorm.Card.Card;
import com.nazunamoe.deresutegachasimulatorm.Card.CustomListAdapter;
import com.nazunamoe.deresutegachasimulatorm.Database.DatabaseHelper;
import com.nazunamoe.deresutegachasimulatorm.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    CustomListAdapter adapter;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    ArrayList<Card> wholelist;
    ArrayList<Card> usinglist;

    CheckBox cuteonlycheck;
    CheckBox coolonlycheck;
    CheckBox passiononlycheck;

    CheckBox ssronlycheck;
    CheckBox sronlycheck;
    CheckBox ronlycheck;
    CheckBox nonlycheck;

    CheckBox usualonlycheck;
    CheckBox limitedonlycheck;
    CheckBox fesonlycheck;
    CheckBox eventonlycheck;

    boolean cuteonly=false;
    boolean coolonly=false;
    boolean passiononly=false;

    boolean ssronly=false;
    boolean sronly=false;
    boolean ronly=false;
    boolean nonly=false;

    boolean usualonly=false;
    boolean limitedonly=false;
    boolean fesonly=false;
    boolean eventonly=false;

    Toolbar toolbar;
    LinearLayout settings;
    ListView listView;
    CardView listViewCard;
    int s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        adapter = new CustomListAdapter();
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        listView = (ListView)findViewById(R.id.CardList);
        listViewCard = (CardView)findViewById(R.id.cardlistcard);
        s = 0;
        settings = (LinearLayout)findViewById(R.id.settings);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.info));
                    settings.setVisibility(LinearLayout.INVISIBLE);
                    s = 0;
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    s = 1;
                    settings.setVisibility(LinearLayout.VISIBLE);//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
                listViewCard.setPadding(16,16,16,verticalOffset);
            }
        });

        listView.setAdapter(adapter);

        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        mDBHelper = new DatabaseHelper(this);

        cuteonlycheck = (CheckBox)findViewById(R.id.CuteOnly);
        coolonlycheck = (CheckBox)findViewById(R.id.CoolOnly);
        passiononlycheck = (CheckBox)findViewById(R.id.PassionOnly);

        ssronlycheck = (CheckBox)findViewById(R.id.SSROnly);
        sronlycheck = (CheckBox)findViewById(R.id.SROnly);
        ronlycheck = (CheckBox)findViewById(R.id.ROnly);
        nonlycheck = (CheckBox)findViewById(R.id.NormalOnly);

        usualonlycheck = (CheckBox)findViewById(R.id.UsualOnly);
        limitedonlycheck = (CheckBox)findViewById(R.id.LimitedOnly);
        fesonlycheck = (CheckBox)findViewById(R.id.FesOnly);
        eventonlycheck = (CheckBox)findViewById(R.id.EventOnly);

        cuteonlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cuteonly=true;
                }else if(!isChecked){
                    cuteonly=false;
                }
                updateListbyType();
            }
        });

        coolonlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    coolonly=true;
                }else if(!isChecked){
                    coolonly=false;
                }
                updateListbyType();
            }
        });

        passiononlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    passiononly=true;
                }else if(!isChecked){
                    passiononly=false;
                }
                updateListbyType();
            }
        });

        ssronlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ssronly=true;
                }else if(!isChecked){
                    ssronly=false;
                }
                updateListbyType();
            }
        });

        sronlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sronly=true;
                }else if(!isChecked){
                    sronly=false;
                }
                updateListbyType();
            }
        });

        ronlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ronly=true;
                }else if(!isChecked){
                    ronly=false;
                }
                updateListbyType();
            }
        });

        nonlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    nonly=true;
                }else if(!isChecked){
                    nonly=false;
                }
                updateListbyType();
            }
        });

        usualonlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    usualonly=true;
                }else if(!isChecked){
                    usualonly=false;
                }
                updateListbyType();
            }
        });

        limitedonlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    limitedonly=true;
                }else if(!isChecked){
                    limitedonly=false;
                }
                updateListbyType();
            }
        });

        fesonlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    fesonly=true;
                }else if(!isChecked){
                    fesonly=false;
                }
                updateListbyType();
            }
        });

        eventonlycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    eventonly=true;
                }else if(!isChecked){
                    eventonly=false;
                }
                updateListbyType();
            }
        });

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("CardList","");
        Type type = new TypeToken<List<Card>>(){}.getType();
        wholelist = gson.fromJson(json, type);
        usinglist = gson.fromJson(json, type);
        for(int i=0; i<wholelist.size(); i++){
            adapter.addItem(wholelist.get(i));
        }
    }

    private void updateListbyType(){
        adapter.clearItem();
        usinglist.clear();
        for(int i=0; i<wholelist.size(); i++){
            if(cuteonly && wholelist.get(i).Type.equals("CUTE")){
                if(updateListbyRarity(wholelist.get(i))){
                    adapter.addItem(wholelist.get(i));
                    usinglist.add(wholelist.get(i));
                }
            }
            if(coolonly && wholelist.get(i).Type.equals("COOL")){
                if(updateListbyRarity(wholelist.get(i))){
                    adapter.addItem(wholelist.get(i));
                    usinglist.add(wholelist.get(i));
                }
            }
            if(passiononly && wholelist.get(i).Type.equals("PASSION")){
                if(updateListbyRarity(wholelist.get(i))){
                    adapter.addItem(wholelist.get(i));
                    usinglist.add(wholelist.get(i));
                }
            }
            if(!cuteonly && !coolonly && !passiononly){
                if(updateListbyRarity(wholelist.get(i))){
                    adapter.addItem(wholelist.get(i));
                    usinglist.add(wholelist.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private boolean updateListbyRarity(Card input){
        if(ssronly && (input.Rarity.equals("SS RARE") || input.Rarity.equals("SS RARE+"))){
            return updateListbyLimited(input);
        }
        if(sronly && (input.Rarity.equals("S RARE") || input.Rarity.equals("S RARE+"))){
            return updateListbyLimited(input);
        }
        if(ronly && (input.Rarity.equals("RARE") || input.Rarity.equals("RARE+"))){
            return updateListbyLimited(input);
        }
        if(nonly && (input.Rarity.equals("NORMAL") || input.Rarity.equals("NORMAL+"))){
            return updateListbyLimited(input);
        }
        if(!ssronly && !sronly && !ronly && !nonly){
            return updateListbyLimited(input);
        }
        return false;
    }

    private boolean updateListbyLimited(Card input){
        if(usualonly && (!input.Limited && !input.Fes && !input.EventCard)){
            return true;
        }
        if(eventonly && (input.EventCard && (!(input.Rarity.equals("RARE"))||!(input.Rarity.equals("RARE+"))))){
            return true;
        }
        if(fesonly && (input.Limited && input.Fes)){
            return true;
        }
        if(limitedonly && (input.Limited && !input.Fes)){
            return true;
        }
        if(!usualonly && !limitedonly && !fesonly && !eventonly){
            return true;
        }
        return false;
    }

}