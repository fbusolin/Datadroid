package it.unive.dais.cevid.aac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.aac.util.University;
import it.unive.dais.cevid.datadroid.lib.parser.AppaltiParser;
import it.unive.dais.cevid.datadroid.lib.parser.SoldiPubbliciParser;
import it.unive.dais.cevid.datadroid.lib.util.DataManipulation;
import it.unive.dais.cevid.datadroid.lib.util.Function;
import it.unive.dais.cevid.datadroid.template.R;

public class SearchActivity extends AppCompatActivity {
    public static final String EXTRA_UNI = "UNI";
    private static final String TAG = "SearchActivity";
    private University university;
    private TextView title;
    private SearchView soldipubbliciSearch;
    private SearchView appaltiSearch;
    private SoldiPubbliciParser soldiPubbliciParser;
    private AppaltiParser appaltiParser;
    private List<AppaltiParser.Data> appaltiList;
    private List<SoldiPubbliciParser.Data> spList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        university = (University) getIntent().getSerializableExtra(EXTRA_UNI);
        title = (TextView) findViewById(R.id.univeristy_name);
        title.setText(university.getTitle());
        soldiPubbliciParser = new SoldiPubbliciParser(university.getCodiceComparto(), university.getCodiceEnte());
        appaltiParser = new AppaltiParser(university.getUrls());

        try {
            appaltiList = appaltiParser.executeAndGet();
            spList = soldiPubbliciParser.executeAndGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        appaltiSearch =(SearchView) findViewById(R.id.ricerca_appalti);
        soldipubbliciSearch = (SearchView) findViewById(R.id.ricerca_soldipubblici);

        appaltiSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onClick: va?");
                ArrayList<AppaltiParser.Data> list = new ArrayList<>(appaltiList);

                if (!query.isEmpty()) {

                    if (query.matches("[0-9]+"))
                        filterAppaltiByCode(Integer.getInteger(query), list);
                    else
                        filterAppaltiByWord(query, list);
                    Log.d(TAG, "onQueryTextSubmit: "+ list.size());
                    Intent intent = new Intent(SearchActivity.this, UniversityActivity.class);
                    intent.putExtra(UniversityActivity.APPALTI_LIST, list);
                    intent.putExtra(UniversityActivity.MODE, UniversityActivity.APPALTI_MODE);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        soldipubbliciSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<SoldiPubbliciParser.Data> list = new ArrayList<>(spList);
                if (!query.isEmpty()){
                    if (query.matches("[0-9]+"))
                        filterSoldiPubbliciByCode(Integer.getInteger(query), list);
                    else
                        filterSoldiPubbliciByWord(query, list);

                    Intent intent = new Intent(SearchActivity.this, UniversityActivity.class);
                    intent.putExtra(UniversityActivity.SP_LIST, list);
                    intent.putExtra(UniversityActivity.MODE, UniversityActivity.SOLDIPUBBLICI_MODE);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void filterSoldiPubbliciByWord(String word, List<SoldiPubbliciParser.Data> list){
        String[] w = word.split(" ");
        DataManipulation.filterByWords(list, w, new Function<SoldiPubbliciParser.Data, String>() {
            @Override
            public String eval(SoldiPubbliciParser.Data x) {
                return x.descrizione_ente;
            }
        });
    }

    private void filterSoldiPubbliciByCode(Integer code, List<SoldiPubbliciParser.Data> list){
        DataManipulation.filterByCode(list, code, new Function<SoldiPubbliciParser.Data, Integer>() {
            @Override
            public Integer eval(SoldiPubbliciParser.Data x) {
                return Integer.getInteger(x.codice_siope);
            }
        });
    }

    private void filterAppaltiByWord(String word, List<AppaltiParser.Data> list){
        String[] w = word.split(" ");
        DataManipulation.filterByWords(list, w, new Function<AppaltiParser.Data, String>() {
            @Override
            public String eval(AppaltiParser.Data x) {
                return x.oggetto;
            }
        });
    }

    private void filterAppaltiByCode(Integer code, List<AppaltiParser.Data> list){
        DataManipulation.filterByCode(list, code, new Function<AppaltiParser.Data, Integer>() {
            @Override
            public Integer eval(AppaltiParser.Data x) {
                return Integer.getInteger(x.cig);
            }
        });
    }

}
