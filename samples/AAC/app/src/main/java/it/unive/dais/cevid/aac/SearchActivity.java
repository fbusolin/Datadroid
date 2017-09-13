package it.unive.dais.cevid.aac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.aac.util.University;
import it.unive.dais.cevid.datadroid.lib.parser.AppaltiParser;
import it.unive.dais.cevid.datadroid.lib.parser.SoldiPubbliciParser;
import it.unive.dais.cevid.datadroid.lib.util.DataManipulation;
import it.unive.dais.cevid.datadroid.lib.util.Function;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    public static final String BUNDLE_UNI = "UNI";
    private static final String BUNDLE_APPALTI_LIST = "APPALTI";
    private static final String BUNDLE_SOLDIPUBBLICI_LIST = "SOLDIPUBBLICI";

    private University university;
    private SearchView soldipubbliciSearch;
    private SearchView appaltiSearch;
    private SoldiPubbliciParser<?> soldiPubbliciParser;
    private AppaltiParser<?> appaltiParser;
//    private List<AppaltiParser.Data> appaltiList;
//    private List<SoldiPubbliciParser.Data> soldipubbliciList;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(BUNDLE_UNI, university);
        savedInstanceState.putSerializable(BUNDLE_APPALTI_LIST, new ArrayList<>(appaltiList));
        savedInstanceState.putSerializable(BUNDLE_SOLDIPUBBLICI_LIST, new ArrayList<>(soldipubbliciList));
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            // crea l'activity da zero
            university = (University) getIntent().getSerializableExtra(BUNDLE_UNI);
            soldiPubbliciParser = new SoldiPubbliciParser(University.getCodiceComparto(), university.getCodiceEnte());
            appaltiParser = new AppaltiParser(university.getUrls());

            try {
                appaltiList = appaltiParser.executeAndRetrieve();
                soldipubbliciList = soldiPubbliciParser.executeAndRetrieve();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            // crea l'activity deserializzando alcuni dati dal bundle
            university = (University) savedInstanceState.getSerializable(BUNDLE_UNI);
        }
        TextView title = (TextView) findViewById(R.id.univeristy_name);
        title.setText(university.getTitle());

        appaltiSearch = (SearchView) findViewById(R.id.ricerca_appalti);
        soldipubbliciSearch = (SearchView) findViewById(R.id.ricerca_soldipubblici);
        appaltiSearch.onActionViewExpanded();
        soldipubbliciSearch.onActionViewExpanded();
        appaltiSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<AppaltiParser.Data> appaltiFilteredList = new ArrayList<>(appaltiList);

                if (!query.isEmpty()) {

                    if (query.matches("[0-9]+"))
                        filterAppaltiByCode(Integer.getInteger(query), appaltiFilteredList);
                    else
                        filterAppaltiByWord(query, appaltiFilteredList);
                    Log.d(TAG, "onQueryTextSubmit: "+ appaltiFilteredList.size());
                    Intent intent = new Intent(SearchActivity.this, UniversityActivity.class);
                    intent.putExtra(UniversityActivity.APPALTI_LIST, appaltiFilteredList);
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
                ArrayList<SoldiPubbliciParser.Data> soldipubbliciFiltredList = new ArrayList<>(soldipubbliciList);
                if (!query.isEmpty()){
                    if (query.matches("[0-9]+"))
                        filterSoldiPubbliciByCode(Integer.parseInt(query), soldipubbliciFiltredList);
                    else
                        filterSoldiPubbliciByWord(query, soldipubbliciFiltredList);

                    Log.d(TAG, "onQueryTextSubmit: " + soldipubbliciFiltredList.size());
                    Intent intent = new Intent(SearchActivity.this, UniversityActivity.class);
                    intent.putExtra(UniversityActivity.SP_LIST, soldipubbliciFiltredList);
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

    private void filterSoldiPubbliciByWord(String word, @NonNull List<SoldiPubbliciParser.Data> list){
        String[] w = word.split(" ");
        DataManipulation.filterByWords(list, w, new Function<SoldiPubbliciParser.Data, String>() {
            @Override
            public String eval(SoldiPubbliciParser.Data x) {
                return x.descrizione_codice;
            }
        },false);
    }

    private void filterSoldiPubbliciByCode(int code, @NonNull List<SoldiPubbliciParser.Data> list){
        DataManipulation.filterByCode(list, code, new Function<SoldiPubbliciParser.Data, Integer>() {
            @Override
            public Integer eval(SoldiPubbliciParser.Data x) {
                return Integer.parseInt(x.codice_siope);
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
        }, false);
    }

    private void filterAppaltiByCode(int code, List<AppaltiParser.Data> list){
        DataManipulation.filterByCode(list, code, new Function<AppaltiParser.Data, Integer>() {
            @Override
            public Integer eval(AppaltiParser.Data x) {
                return Integer.parseInt(x.cig);
            }
        });
    }

}
