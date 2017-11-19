package com.example.gianmarcocallegher.sprapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.gianmarcocallegher.sprapp.util.Anno2017;
import com.example.gianmarcocallegher.sprapp.util.PagerAdapter;

import java.io.Serializable;
import java.util.List;

import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;

public class SearchableActivity extends AppCompatActivity {


    //private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;
    private List<SoldipubbliciParser.Data> spese_ente_2017, spese_ente_2016, spese_ente_2015, spese_ente_2014, spese_ente_2013;
    private String numero_abitanti, descrizione_ente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Intent i = getIntent();

        numero_abitanti = i.getStringExtra("numero_abitanti");
        descrizione_ente = i.getStringExtra("descrizione_ente");
        Serializable sl17 = i.getSerializableExtra("spese_ente_2017");
        Serializable sl16 = i.getSerializableExtra("spese_ente_2016");
        Serializable sl15 = i.getSerializableExtra("spese_ente_2015");
        Serializable sl14 = i.getSerializableExtra("spese_ente_2014");
        Serializable sl13 = i.getSerializableExtra("spese_ente_2013");
        spese_ente_2017 = (List<SoldipubbliciParser.Data>) sl17;
        spese_ente_2016 = (List<SoldipubbliciParser.Data>) sl16;
        spese_ente_2015 = (List<SoldipubbliciParser.Data>) sl15;
        spese_ente_2014 = (List<SoldipubbliciParser.Data>) sl14;
        spese_ente_2013 = (List<SoldipubbliciParser.Data>) sl13;

        Log.d("ABITANTI", numero_abitanti);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("2017"));
        tabLayout.addTab(tabLayout.newTab().setText("2016"));
        tabLayout.addTab(tabLayout.newTab().setText("2015"));
        tabLayout.addTab(tabLayout.newTab().setText("2014"));
        tabLayout.addTab(tabLayout.newTab().setText("2013"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public String getNumero_abitanti() {
        return numero_abitanti;
    }

    public List<SoldipubbliciParser.Data> getSpese_Ente_2017 () {
        return spese_ente_2017;
    }

    public List<SoldipubbliciParser.Data> getSpese_Ente_2016 () {
        return spese_ente_2016;
    }

    public List<SoldipubbliciParser.Data> getSpese_Ente_2015 () {
        return spese_ente_2015;
    }

    public List<SoldipubbliciParser.Data> getSpese_Ente_2014 () {
        return spese_ente_2014;
    }

    public List<SoldipubbliciParser.Data> getSpese_Ente_2013 () {
        return spese_ente_2013;
    }
}
