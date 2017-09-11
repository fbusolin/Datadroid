package it.unive.dais.cevid.aac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.aac.util.AppaltiAdapter;
import it.unive.dais.cevid.aac.util.University;
import it.unive.dais.cevid.datadroid.lib.util.DataManipulation;
import it.unive.dais.cevid.datadroid.lib.util.Function;
import it.unive.dais.cevid.datadroid.template.R;
import it.unive.dais.cevid.datadroid.lib.parser.AppaltiParser;
import it.unive.dais.cevid.datadroid.lib.parser.SoldiPubbliciParser;


public class UniversityActivity extends AppCompatActivity {

    public static final String APPALTI_LIST = "APPALTI_LIST";
    public static final String SP_LIST = "SP_LIST";
    public static final String MODE = "MODE";

    public static final int APPALTI_MODE = 1;
    public static final int SOLDIPUBBLICI_MODE = 2;

    private int mode;
    private TextView descrizione;
    private TextView spesa2016;
    private LinearLayout appaltiSum;
    private TextView appaltiSumText;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppaltiAdapter appaltiAdapter;
    private static final String TAG = "UniversityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);
        mode = (int) getIntent().getSerializableExtra(MODE);



        mRecyclerView= (RecyclerView) findViewById(R.id.lista_appalti);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (mode == APPALTI_MODE) {

            try {
                List<AppaltiParser.Data> appaltiList = (ArrayList<AppaltiParser.Data>) getIntent().getSerializableExtra(APPALTI_LIST);
                appaltiAdapter = new AppaltiAdapter(appaltiList);
                mRecyclerView.setAdapter(appaltiAdapter);
                appaltiSum = (LinearLayout) findViewById(R.id.appalti_somma);
                appaltiSum.setVisibility(View.VISIBLE);

                appaltiSumText = (TextView) findViewById(R.id.spesa_totale);
                Double sum = DataManipulation.sumBy(appaltiList, new Function<AppaltiParser.Data, Double>() {
                    @Override
                    public Double eval(AppaltiParser.Data x) {
                        Log.d(TAG, "eval: "+ x.importo);
                        return Double.valueOf(x.importo);
                    }
                });
                Log.d(TAG, "onCreate: "+ sum);
                appaltiSumText.setText(sum.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

