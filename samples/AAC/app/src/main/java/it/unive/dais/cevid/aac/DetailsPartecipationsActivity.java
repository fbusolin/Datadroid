package it.unive.dais.cevid.aac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import it.unive.dais.cevid.aac.entities.Partecipation;
import it.unive.dais.cevid.aac.util.PartecipationAdapter;

public class DetailsPartecipationsActivity extends AppCompatActivity {
    public static final String TAG = "DetailsPartecipationsActivity";
    public static final String BUNDLE_PARTECIPATIONS = "PARTS";
    private ArrayList<Partecipation> partecipations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partecipations_bids);
        Intent intent = getIntent();
        RecyclerView.LayoutManager lmanager = new LinearLayoutManager(this);
        RecyclerView view = (RecyclerView) findViewById(R.id.lista_partecipazioni);
        view.setLayoutManager(lmanager);

        partecipations =(ArrayList<Partecipation>) intent.getSerializableExtra(BUNDLE_PARTECIPATIONS);
        int n = partecipations.size();

        PartecipationAdapter adapter = new PartecipationAdapter(partecipations);
        view.setAdapter(adapter);
    }
}
