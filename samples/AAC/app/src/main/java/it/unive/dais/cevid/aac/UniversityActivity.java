package it.unive.dais.cevid.aac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.aac.util.AppaltiAdapter;
import it.unive.dais.cevid.aac.util.University;
import it.unive.dais.cevid.datadroid.template.R;
import it.unive.dais.cevid.datadroid.lib.parser.AppaltiParser;
import it.unive.dais.cevid.datadroid.lib.parser.SoldiPubbliciParser;


public class UniversityActivity extends AppCompatActivity {

    public static final String EXTRA_UNI = "UNI";
    private University university;
    private SoldiPubbliciParser soldiPubbliciParser;
    private AppaltiParser appaltiParser;
    private TextView descrizione;
    private TextView spesa2016;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppaltiAdapter appaltiAdapter;
    private static final String TAG = "UniversityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);
        university = (University) getIntent().getSerializableExtra(EXTRA_UNI);

        soldiPubbliciParser = new SoldiPubbliciParser(university.getCodiceComparto(),university.getCodiceEnte());
        appaltiParser = new AppaltiParser(university.getUrls());

        CharSequence ls = "Hardware";
        List<CharSequence> ss = new ArrayList<>();
        ss.add(ls);

        List<SoldiPubbliciParser.Data> splList = null;
        try {
            splList = soldiPubbliciParser.executeAndGet();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        descrizione = (TextView) findViewById(R.id.descrizione_spesa_pubblica);
        spesa2016 = (TextView) findViewById(R.id.spesa_pubblica_2016);

        descrizione.setText(splList.get(0).descrizione_codice);
        spesa2016.setText(splList.get(0).importo_2016+ " €");

        mRecyclerView= (RecyclerView) findViewById(R.id.lista_appalti);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        try {
            List<AppaltiParser.Data> appaltiList = appaltiParser.executeAndGet();
            appaltiAdapter = new AppaltiAdapter(appaltiList);
            mRecyclerView.setAdapter(appaltiAdapter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}

