package it.unive.dais.cevid.aac;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.aac.entities.Partecipation;
import it.unive.dais.cevid.aac.entities.Supplier;
import it.unive.dais.cevid.aac.util.PartecipazioniParser;

public class SupplierInfoActivity extends AppCompatActivity {
    public static final String TAG = "SupplierInfoActivity";
    public static String BUNDLE_SUPPLY = "SUPPLY";
    Supplier supp;
    View mainView;
    PartecipazioniParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_supplier);
       // bundle restore
        if (savedInstanceState == null) {
            // crea l'activity da zero
            supp = (Supplier) getIntent().getSerializableExtra(BUNDLE_SUPPLY);
        } else {
            // ricrea l'activity deserializzando alcuni dati dal bundle
            supp = (Supplier) savedInstanceState.getSerializable(BUNDLE_SUPPLY);
        }
        //create activity


        this.mainView = findViewById(R.id.supply_info_activity);
        parser = new PartecipazioniParser(supp.getPiva());
        parser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        TextView titleView = (TextView) findViewById(R.id.supply_title);
        titleView.setText(supp.getTitle());

        TextView ivaView = (TextView) findViewById(R.id.supply_iva);
        ivaView.setText(supp.getPiva());

        TextView addressView = (TextView) findViewById(R.id.supply_address);
        addressView.setText(supp.getAddress());

        TextView typeView = (TextView) findViewById(R.id.supply_type);
        typeView.setText(supp.getType());
        Button button = (Button) findViewById(R.id.button_supply_expand);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    List<PartecipazioniParser.Data> data = parser.getAsyncTask().get();
                    int size = data.size();
                    ArrayList<Partecipation> parts = new ArrayList<>();
                    for(PartecipazioniParser.Data d: data){
                        parts.add(new Partecipation(d));
                    }
                    if (parts.size() > 0) {
                        Intent intent = new Intent(SupplierInfoActivity.this,DetailsPartecipationsActivity.class);
                        intent.putExtra(DetailsPartecipationsActivity.BUNDLE_PARTECIPATIONS,parts);
                        startActivity(intent);
                    }else {
                        alert(String.format("Trovati %d bandi attivati nel 2016 per %s",size,supp.getTitle()));
                    }
                }catch(InterruptedException | ExecutionException e){
                    alert(String.format("Errore inatteso: %s. Riprovare.", e.getMessage()));
                    Log.e(TAG, String.format("exception caught during parser %s", parser.getName()));
                    e.printStackTrace();
                }
            }
        });
    }
    private void alert(String msg) {
        Snackbar.make(mainView, msg, Snackbar.LENGTH_SHORT).show();
    }

}
