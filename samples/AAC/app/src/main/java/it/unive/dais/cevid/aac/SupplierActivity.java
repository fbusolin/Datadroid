package it.unive.dais.cevid.aac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import it.unive.dais.cevid.aac.entities.Supplier;

public class SupplierActivity extends AppCompatActivity {
    public static String BUNDLE_SUPPLY = "SUPPLY";
    Supplier supp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        if (savedInstanceState == null) {
            // crea l'activity da zero
            supp = (Supplier) getIntent().getSerializableExtra(BUNDLE_SUPPLY);
        } else {
            // ricrea l'activity deserializzando alcuni dati dal bundle
            supp = (Supplier) savedInstanceState.getSerializable(BUNDLE_SUPPLY);
        }

        TextView title = (TextView) findViewById(R.id.supply_title);
        title.setText(supp.getTitle() + " PIVA:  "+supp.getPiva());
    }
}
