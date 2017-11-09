package com.example.gianmarcocallegher.sprapp;

import android.app.ListActivity;
import android.os.Bundle;

public class SearchableActivity extends ListActivity {

    public static final String LIST_COMUNI = "LIST_COMUNI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
    }
}
