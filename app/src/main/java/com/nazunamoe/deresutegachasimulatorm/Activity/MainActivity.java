package com.nazunamoe.deresutegachasimulatorm.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuInflater;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.google.gson.Gson;
import com.nazunamoe.deresutegachasimulatorm.Card.Card;
import com.nazunamoe.deresutegachasimulatorm.Database.DatabaseHelper;
import com.nazunamoe.deresutegachasimulatorm.Fragments.GachaFragment;
import com.nazunamoe.deresutegachasimulatorm.R;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GachaFragment.OnFragmentInteractionListener {

    NavigationView navigationView;
    Toolbar toolbar;
    DatabaseHelper mDBHelper;
    SQLiteDatabase mDb;
    ArrayList<Card> card_list;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_info) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
            alert_confirm.setMessage(getResources().getString(R.string.CurrentVersion)+" : "+getResources().getString(R.string.Version));
            alert_confirm.setPositiveButton("OK", null);
            AlertDialog alert = alert_confirm.create();

            alert.setTitle(R.string.info);
            alert.show();
        }
        if (menuItem.getItemId() == R.id.exit) {
            finishAffinity();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setSubtitleTextColor(Color.WHITE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        mDBHelper = new DatabaseHelper(this);


        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        mDBHelper.openDataBase();

        card_list = mDBHelper.getAllCardList();
        SharedPreferences addSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = addSharedPrefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(card_list);
        prefsEditor.putString("CardList", json);
        prefsEditor.commit();

        SharedPreferences Shared = getSharedPreferences("Shared", 0);
        SharedPreferences.Editor editor = Shared.edit();
        editor.putFloat("SSRP",(float)3.0);
        editor.putFloat("SRP",(float)12.0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.maincontents,new GachaFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view  item clicks here.
        int id = item.getItemId();
        Intent intentgogo=null;
        Bundle bundle = new Bundle();
        if(id == R.id.limitedswitch){
            intentgogo = new Intent(this, LimitedCardActivity.class);
        }else if(id == R.id.pbutton2){
            intentgogo = new Intent(this, pActivity.class);}
        else if(id == R.id.nav_cardinfo){
            intentgogo = new Intent(this, InfoActivity.class);
        }
        startActivity(intentgogo);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
