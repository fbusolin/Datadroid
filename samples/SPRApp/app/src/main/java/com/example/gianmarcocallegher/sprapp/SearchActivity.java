package com.example.gianmarcocallegher.sprapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.datadroid.lib.parser.EntiParser;
import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;
import it.unive.dais.cevid.datadroid.lib.util.DataManipulation;
import it.unive.dais.cevid.datadroid.lib.util.Function;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private EntiParser<?> entiParser;
    private List entiList;
    private ListView lv;
    ArrayAdapter<String> adapter;
    EditText inputSearch;
    List<String> nomeEnti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        entiList = new ArrayList<EntiParser.Data>();
        entiParser = new EntiParser();

        entiParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        nomeEnti = new ArrayList<>();


        try {
            List<EntiParser.Data> l = new ArrayList<>(entiParser.getAsyncTask().get());
            for (EntiParser.Data x : l) {
                if (x.codice_comparto.equals("PRO") || x.codice_comparto.equals("REG"))
                    entiList.add(x);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        for (Object x : entiList)
            nomeEnti.add(((EntiParser.Data) x).descrizione_ente);

        adapter = new ArrayAdapter<String>(this, R.layout.list_enti, R.id.DescrizioneEnte, nomeEnti);

        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String numero_abitanti, codice_comparto, codice_ente, descrizione_ente = adapter.getItem(position);
                List speseEnte = new ArrayList<SoldipubbliciParser.Data>();

                codice_comparto = findCodiceCompartoByDescrizioneEnte(descrizione_ente);
                codice_ente = findCodiceEnteByDescrizioneEnte(descrizione_ente);
                numero_abitanti = findNumeroAbitantiByDescrizioneEnte(descrizione_ente);

                SoldipubbliciParser soldipubbliciParser = new SoldipubbliciParser(codice_comparto, codice_ente);

                soldipubbliciParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                try {
                    speseEnte = new ArrayList<>(soldipubbliciParser.getAsyncTask().get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(SearchActivity.this, SearchableActivity.class);

                intent.putExtra("numero_abiatanti", numero_abitanti);
                intent.putExtra("descrizione_ente", descrizione_ente);
                intent.putExtra("spese_ente", (Serializable) speseEnte);

                startActivity(intent);
            }
        });
    }

    public String findCodiceCompartoByDescrizioneEnte(String descrizione_ente) {
        String codice_comparto = new String();
        for (Object x : entiList) {
            if (((EntiParser.Data) x).descrizione_ente.equals(descrizione_ente)) {
                codice_comparto = new String(((EntiParser.Data) x).codice_comparto);
            }
        }
        return codice_comparto;
    }

    public String findCodiceEnteByDescrizioneEnte(String descrizione_ente) {
        String codice_ente = new String();
        for (Object x : entiList) {
            if (((EntiParser.Data) x).descrizione_ente.equals(descrizione_ente)) {
                codice_ente = new String(((EntiParser.Data) x).codice_ente);
            }
        }
        return codice_ente;
    }

    public String findNumeroAbitantiByDescrizioneEnte(String descrizione_ente) {
        String numero_abitanti = new String();
        for (Object x : entiList) {
            if (((EntiParser.Data) x).descrizione_ente.equals(descrizione_ente)) {
                numero_abitanti = new String(((EntiParser.Data) x).numero_abitanti);
            }
        }
        return numero_abitanti;
    }
}
