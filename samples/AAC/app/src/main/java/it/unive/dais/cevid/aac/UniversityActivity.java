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

import it.unive.dais.cevid.aac.util.AppaltiAdapter;
import it.unive.dais.cevid.aac.util.SoldiPubbliciAdapter;
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
    private LinearLayout appaltiSum;
    private TextView appaltiSumText;
    private RecyclerView appaltiRecyclerView, spRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppaltiAdapter appaltiAdapter;
    private SoldiPubbliciAdapter soldiPubbliciAdapter;
    private static final String TAG = "UniversityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);
        mode = (int) getIntent().getSerializableExtra(MODE);



        appaltiRecyclerView = (RecyclerView) findViewById(R.id.lista_appalti);
        spRecyclerView = (RecyclerView) findViewById(R.id.lista_appalti);

        mLayoutManager = new LinearLayoutManager(this);
        appaltiRecyclerView.setLayoutManager(mLayoutManager);

        if (mode == APPALTI_MODE) {

            List<AppaltiParser.Data> appaltiList = (ArrayList<AppaltiParser.Data>) getIntent().getSerializableExtra(APPALTI_LIST);
            appaltiAdapter = new AppaltiAdapter(appaltiList);
            appaltiRecyclerView.setAdapter(appaltiAdapter);

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
            Log.d(TAG, "onCreate: "+ String.valueOf(sum));
            appaltiSumText.setText(String.valueOf(sum));

        }

        if (mode == SOLDIPUBBLICI_MODE){
            List<SoldiPubbliciParser.Data> spList = (ArrayList<SoldiPubbliciParser.Data> ) getIntent().getSerializableExtra(SP_LIST);
            Log.d(TAG, "onCreate: "+ spList.size());
            soldiPubbliciAdapter = new SoldiPubbliciAdapter(spList);
            spRecyclerView.setAdapter(soldiPubbliciAdapter);

        }

    }
}

