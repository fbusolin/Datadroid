package it.unive.dais.cevid.aac;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.aac.entities.Municipality;
import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;

public class ComuniInfoActivity extends AppCompatActivity {
    public static final String COMUNE = "COMUNE" ;
    SoldipubbliciParser soldipubbliciParser;
    public static String CODENTE = "ENTE",CODCOMPARTO = "COMPARTO";
    Municipality comune;
    String ente;
    String comparto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comuni_info);
        ente = getIntent().getStringExtra(CODENTE);
        comparto = getIntent().getStringExtra(CODCOMPARTO);
        comune = (Municipality) getIntent().getSerializableExtra(COMUNE);
        soldipubbliciParser = new SoldipubbliciParser(comparto,ente);
        soldipubbliciParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Button btn = (Button)  findViewById(R.id.button_comuni);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }


    protected void click(){
        String numero_abitanti, codice_comparto, codice_ente, descrizione_ente = comune.getDescription();
        List spese_ente_2017 = new ArrayList<SoldipubbliciParser.Data>();
        List spese_ente_2016 = new ArrayList<SoldipubbliciParser.Data>();
        List spese_ente_2015 = new ArrayList<SoldipubbliciParser.Data>();
        List spese_ente_2014 = new ArrayList<SoldipubbliciParser.Data>();
        List spese_ente_2013 = new ArrayList<SoldipubbliciParser.Data>();

        /** codice_comparto = findCodiceCompartoByDescrizioneEnte(descrizione_ente);
         codice_ente = findCodiceEnteByDescrizioneEnte(descrizione_ente);
         numero_abitanti = findNumeroAbitantiByDescrizioneEnte(descrizione_ente);*/
        numero_abitanti = "100000"; //TODO:  fix it

        try {
            List<SoldipubbliciParser.Data> l = new ArrayList<>(soldipubbliciParser.getAsyncTask().get());
            for (SoldipubbliciParser.Data x : l) {
                if (!(x.importo_2017).equals("0")) {
                    spese_ente_2017.add(x);
                }
                if (!(x.importo_2016).equals("0")) {
                    spese_ente_2016.add(x);
                }
                if (!(x.importo_2015).equals("0")) {
                    spese_ente_2015.add(x);
                }
                if (!(x.importo_2014).equals("0")) {
                    spese_ente_2014.add(x);
                }
                if (!(x.importo_2013).equals("0")) {
                    spese_ente_2013.add(x);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(ComuniInfoActivity.this, SearchableActivity.class);

        //crop size, quick fix for crash
        spese_ente_2013.subList(100,spese_ente_2013.size()).clear();
        spese_ente_2014.subList(100,spese_ente_2014.size()).clear();
        spese_ente_2015.subList(100,spese_ente_2015.size()).clear();
        spese_ente_2016.subList(100,spese_ente_2016.size()).clear();
        spese_ente_2017.subList(100,spese_ente_2017.size()).clear();

        intent.putExtra("numero_abitanti", numero_abitanti);
        intent.putExtra("descrizione_ente", descrizione_ente);
        intent.putExtra("spese_ente_2017", (Serializable) spese_ente_2017);
        intent.putExtra("spese_ente_2016", (Serializable) spese_ente_2016);
        intent.putExtra("spese_ente_2015", (Serializable) spese_ente_2015);
        intent.putExtra("spese_ente_2014", (Serializable) spese_ente_2014);
        intent.putExtra("spese_ente_2013", (Serializable) spese_ente_2013);

        startActivity(intent);
    }
}
