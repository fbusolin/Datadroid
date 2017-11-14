package com.example.gianmarcocallegher.sprapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;

public class SearchableActivity extends AppCompatActivity {

    public static final String LIST_COMUNI = "LIST_ENTI_TERRITORIALI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Intent i = getIntent();
        String numero_abitanti = i.getStringExtra("numero_abitanti");
        String descrizione_ente = i.getStringExtra("descrizione_ente");
        Serializable sl = i.getSerializableExtra("spese_ente");
        List<SoldipubbliciParser.Data> spese_ente = (List<SoldipubbliciParser.Data>) sl;

    }
}
