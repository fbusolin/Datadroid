package it.unive.dais.cevid.aac;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.aac.entities.Bid;
import it.unive.dais.cevid.aac.entities.Partecipation;
import it.unive.dais.cevid.aac.util.BandiParser;
import it.unive.dais.cevid.aac.util.PartecipationAdapter;
import it.unive.dais.cevid.aac.util.RecyclerItemClickListener;

public class DetailsPartecipationsActivity extends AppCompatActivity {
    public static final String TAG = "DetailsPartecipationsActivity";
    public static final String BUNDLE_PARTECIPATIONS = "PARTS";
    private ArrayList<Partecipation> partecipations;
    private Map<Partecipation,Bid> map;
    private ArrayList<BandiParser> parsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        map = new HashMap<>();
        parsers = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partecipations_bids);
        Intent intent = getIntent();
        RecyclerView.LayoutManager lmanager = new LinearLayoutManager(this);
        RecyclerView view = (RecyclerView) findViewById(R.id.lista_partecipazioni);
        view.setLayoutManager(lmanager);

        partecipations =(ArrayList<Partecipation>) intent.getSerializableExtra(BUNDLE_PARTECIPATIONS);
        for(Partecipation p : partecipations){
            String lotto = p.getLotto();
            BandiParser bandiParser = new BandiParser(lotto);
            parsers.add(bandiParser);
            bandiParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        PartecipationAdapter adapter = new PartecipationAdapter(partecipations);
        view.setAdapter(adapter);
        view.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), view ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        try {
                           List<BandiParser.Data> data = parsers.get(position).getAsyncTask().get();
                           if(data == null || data.size() <= 0) return;//<-- errore, non faccio nulla
                           /** una partecipazione è relativa sempre
                            * a uno e un solo Bando
                            * in caso di più partecipazioni sono doppioni**/
                           Bid bid = new Bid(data.get(0));
                           map.put(partecipations.get(position),bid);
                           Intent next = new Intent(DetailsPartecipationsActivity.this,BidActivity.class);
                           next.putExtra(BidActivity.BUNDLE_BID,bid);
                           startActivity(next);

                        } catch (InterruptedException|ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }
}
