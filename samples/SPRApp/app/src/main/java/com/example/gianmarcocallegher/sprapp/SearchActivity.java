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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.datadroid.lib.parser.EntiParser;
import it.unive.dais.cevid.datadroid.lib.util.Function;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private LinearLayout mainView;
    private EntiParser<?> entiParser;
    private String searchText = "";
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
                if (x.codice_comparto.equals("PRO") || x.codice_comparto.equals("REG")) {
                    //Log.d("PROVA", x.descrizione_ente);
                    entiList.add(x);
                }
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
                Intent i = new Intent(SearchActivity.this, SearchableActivity.class);
            }
        });

        /*setSingleListener(soldiPubblicireloadedSearch, entiParser, SearchableActivity.LIST_ENTI_TERRITORIALI, Enti_getText, Enti_getCode, new Function<String, Void>() {
            @Override
            public Void eval(String x) {
                SearchActivity.this.searchText = x;
                return null;
            }
        });

        //Button combineButton = (Button) findViewById(R.id.button_data);
        /*combineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, com.example.gianmarcocallegher.sprapp.SearchableActivity.class);
                boolean r1 = processQuery(entiParser, searchText, intent, com.example.gianmarcocallegher.sprapp.SearchableActivity.LIST_COMUNI, Enti_getText, Enti_getCode);
                if (r1)
                    startActivity(intent);
                else
                    alert("Compilare il campo di ricerca");
            }
        });

        mainView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(v);
                }
            }
        });*/
    }

    /*private <T> void setSingleListener(final SearchView v, final AsyncParser<T, ?> parser, final String label,
                                       final Function<T, String> getText, final Function<T, Integer> getCode, final Function<String, Void> setText) {
        v.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                Intent intent = new Intent(SearchActivity.this, SearchableActivity.class);
                if (processQuery(parser, text, intent, label, getText, getCode))
                    startActivity(intent);
                else
                    alert("La ricerca non ha prodotto nessun risultato. Provare con altri valori.");
                v.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setText.eval(newText);
                return false;
            }
        });
    }

    private <T> boolean processQuery(AsyncParser<T, ?> parser, String text, Intent intent, String label,
                                     Function<T, String> getText, Function<T, Integer> getCode) {
        try {
            List<T> l = new ArrayList<>(parser.getAsyncTask().get());
            if (!text.isEmpty()) {
                if (text.matches("[0-9]+"))
                    DataManipulation.filterByCode(l, Integer.parseInt(text), getCode);
                else
                    DataManipulation.filterByWords(l, text.split(" "), getText, false);
                if (l.size() == 0) {
                    return false;
                } else {
                    intent.putExtra(label, (Serializable) l);
                    return true;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            alert(String.format("Errore inatteso: %s. Riprovare.", e.getMessage()));
            e.printStackTrace();
        }
        return false;
    }

    private void alert(String msg) {
        Snackbar.make(mainView, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }*/

    private static final Function<EntiParser.Data, String> Enti_getText = new Function<EntiParser.Data, String>() {
        @Override
        public String eval(EntiParser.Data x) {
            return x.codice_sottocomparto;
        }
    };

    private static final Function<EntiParser.Data, Integer> Enti_getCode = new Function<EntiParser.Data, Integer>() {
        @Override
        public Integer eval(EntiParser.Data x) {
            return Integer.parseInt(x.codice_comune);
        }
    };

}
