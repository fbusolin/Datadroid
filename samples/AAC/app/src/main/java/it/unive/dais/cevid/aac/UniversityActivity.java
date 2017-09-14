package it.unive.dais.cevid.aac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import it.unive.dais.cevid.aac.util.AppaltiAdapter;
import it.unive.dais.cevid.aac.util.SoldiPubbliciAdapter;
import it.unive.dais.cevid.datadroid.lib.parser.AppaltiParser;
import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;
import it.unive.dais.cevid.datadroid.lib.util.DataManipulation;
import it.unive.dais.cevid.datadroid.lib.util.Function;


public class UniversityActivity extends AppCompatActivity {

    public static final String LIST = "LIST";
    public static final String MODE = "MODE";

    enum Mode {APPALTI, SOLDIPUBBLICI }

    private int mode;
    private static final String TAG = "UniversityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);
        final Intent i = getIntent();
        final Serializable l0 = i.getSerializableExtra(LIST);
        final Mode mode = (Mode) i.getSerializableExtra(MODE);

        RecyclerView appaltiRecyclerView = (RecyclerView) findViewById(R.id.lista_appalti);
        RecyclerView soldipubbliciRecyclerView = (RecyclerView) findViewById(R.id.lista_appalti);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        appaltiRecyclerView.setLayoutManager(mLayoutManager);

        switch (mode) {
            case APPALTI: {
                final List<AppaltiParser.Data> l = (List<AppaltiParser.Data>) l0;
                AppaltiAdapter appaltiAdapter = new AppaltiAdapter(l);
                appaltiRecyclerView.setAdapter(appaltiAdapter);

                LinearLayout appaltiSum = (LinearLayout) findViewById(R.id.appalti_somma);
                appaltiSum.setVisibility(View.VISIBLE);
                TextView appaltiSumText = (TextView) findViewById(R.id.spesa_totale);
                Double sum = DataManipulation.sumBy(l, new Function<AppaltiParser.Data, Double>() {
                    @Override
                    public Double eval(AppaltiParser.Data x) {
                        Log.d(TAG, "eval: " + x.importo);
                        return Double.valueOf(x.importo);
                    }
                });
                Log.d(TAG, "onCreate: " + String.valueOf(sum));
                appaltiSumText.setText(String.valueOf(sum));
                break;
            }

            case SOLDIPUBBLICI: {
                final List<SoldipubbliciParser.Data> l = (List<SoldipubbliciParser.Data>) l0;
                Log.d(TAG, "onCreate: " + l.size());
                SoldiPubbliciAdapter soldiPubbliciAdapter = new SoldiPubbliciAdapter(l);
                soldipubbliciRecyclerView.setAdapter(soldiPubbliciAdapter);
                break;
            }

            default: {
                Log.e(TAG, String.format("unknown mode: %d", mode));
            }

        }

    }
}

