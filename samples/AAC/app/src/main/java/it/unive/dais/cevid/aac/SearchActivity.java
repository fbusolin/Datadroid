package it.unive.dais.cevid.aac;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.aac.util.University;
import it.unive.dais.cevid.datadroid.lib.parser.AppaltiParser;
import it.unive.dais.cevid.datadroid.lib.parser.AsyncParser;
import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;
import it.unive.dais.cevid.datadroid.lib.util.DataManipulation;
import it.unive.dais.cevid.datadroid.lib.util.Function;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    public static final String BUNDLE_UNI = "UNI";
    private static final String BUNDLE_LIST = "LIST";

    private University university;
    private SoldipubbliciParser<?> soldiPubbliciParser; // TODO: agguingere una progress bar al layout
    private AppaltiParser<?> appaltiParser;
    private LinearLayout mainView;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(BUNDLE_UNI, university);
//        saveParserState(savedInstanceState, appaltiParser);
//        saveParserState(savedInstanceState, soldiPubbliciParser);
        super.onSaveInstanceState(savedInstanceState);
    }

    private <T> void saveParserState(Bundle savedInstanceState, AsyncParser<T, ?> parser) {
        try {
            savedInstanceState.putSerializable(BUNDLE_LIST, new ArrayList<T>(parser.getAsyncTask().get()));
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, String.format("parser %s failed", parser.getClass().getSimpleName()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mainView = (LinearLayout) findViewById(R.id.search_activity);

        if (savedInstanceState == null) {
            // crea l'activity da zero
            university = (University) getIntent().getSerializableExtra(BUNDLE_UNI);
        } else {
            // ricrea l'activity deserializzando alcuni dati dal bundle
            university = (University) savedInstanceState.getSerializable(BUNDLE_UNI);
        }
        TextView title = (TextView) findViewById(R.id.univeristy_name);
        title.setText(university.getTitle());

        // TODO: salvare lo stato dei parser con un proxy serializzabile
        soldiPubbliciParser = new SoldipubbliciParser(University.getCodiceComparto(), university.getCodiceEnte());
        appaltiParser = new AppaltiParser(university.getUrls());
        soldiPubbliciParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        appaltiParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        SearchView appaltiSearch = (SearchView) findViewById(R.id.ricerca_appalti);
        SearchView soldipubbliciSearch = (SearchView) findViewById(R.id.ricerca_soldipubblici);
        appaltiSearch.onActionViewExpanded();
        soldipubbliciSearch.onActionViewExpanded();

        setOnQueryTextListener(appaltiSearch, appaltiParser, UniversityActivity.Mode.APPALTI,
                new Function<AppaltiParser.Data, String>() {
                    @Override
                    public String eval(AppaltiParser.Data x) {
                        return x.oggetto;
                    }
                },
                new Function<AppaltiParser.Data, Integer>() {
                    @Override
                    public Integer eval(AppaltiParser.Data x) {
                        return Integer.parseInt(x.cig);
                    }
                });

        setOnQueryTextListener(soldipubbliciSearch, soldiPubbliciParser, UniversityActivity.Mode.SOLDI_PUBBLICI,
                new Function<SoldipubbliciParser.Data, String>() {
                    @Override
                    public String eval(SoldipubbliciParser.Data x) {
                        return x.descrizione_codice;
                    }
                },
                new Function<SoldipubbliciParser.Data, Integer>() {
                    @Override
                    public Integer eval(SoldipubbliciParser.Data x) {
                        return Integer.parseInt(x.codice_siope);
                    }
                });
    }


    private <T> void setOnQueryTextListener(SearchView search, final AsyncParser<T, ?> parser, final UniversityActivity.Mode mode,
                                            final Function<T, String> getText, final Function<T, Integer> getCode) {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if (!query.isEmpty()) {
                        final List<T> l = new ArrayList<T>(parser.getAsyncTask().get());
                        if (query.matches("[0-9]+"))
                            DataManipulation.filterByCode(l, Integer.parseInt(query), getCode);
                        else
                            DataManipulation.filterByWords(l, query.split(" "), getText, false);
                        if (l.size() == 0) {
                            Snackbar.make(mainView, "La ricerca non ha dato nessun risultato.", Snackbar.LENGTH_SHORT).show();
                            return false;
                        }
                        Log.d(TAG, "onQueryTextSubmit: " + l.size());
                        Intent intent = new Intent(SearchActivity.this, UniversityActivity.class);
                        intent.putExtra(UniversityActivity.LIST, (Serializable) l);
                        intent.putExtra(UniversityActivity.MODE, mode);
                        startActivity(intent);
                        return true;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Log.e(TAG, String.format("exception caught during parser %s", parser.getName()));
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }



//    private void filterSoldiPubbliciByWord(String word, @NonNull List<SoldipubbliciParser.Data> list) {
//        String[] w = word.split(" ");
//        DataManipulation.filterByWords(list, w, new Function<SoldipubbliciParser.Data, String>() {
//            @Override
//            public String eval(SoldipubbliciParser.Data x) {
//                return x.descrizione_codice;
//            }
//        }, false);
//    }
//
//    private void filterSoldiPubbliciByCode(int code, @NonNull List<SoldipubbliciParser.Data> list) {
//        filterByCode(list, code, new Function<SoldipubbliciParser.Data, Integer>() {
//            @Override
//            public Integer eval(SoldipubbliciParser.Data x) {
//                return Integer.parseInt(x.codice_siope);
//            }
//        });
//    }
//
//    private void filterAppaltiByWord(String word, List<AppaltiParser.Data> list) {
//        String[] w = word.split(" ");
//        DataManipulation.filterByWords(list, w, new Function<AppaltiParser.Data, String>() {
//            @Override
//            public String eval(AppaltiParser.Data x) {
//                return x.oggetto;
//            }
//        }, false);
//    }
//
//    private void filterAppaltiByCode(int code, List<AppaltiParser.Data> list) {
//        filterByCode(list, code, new Function<AppaltiParser.Data, Integer>() {
//            @Override
//            public Integer eval(AppaltiParser.Data x) {
//                return Integer.parseInt(x.cig);
//            }
//        });
//    }

}
