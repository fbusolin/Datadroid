package it.unive.dais.cevid.aac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import it.unive.dais.cevid.aac.entities.Supplier;

public class SupplierInfoActivity extends AppCompatActivity {
    public static final String TAG = "SupplierInfoActivity";
    public static String BUNDLE_SUPPLY = "SUPPLY";
    Supplier supp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_supplier);

        if (savedInstanceState == null) {
            // crea l'activity da zero
            supp = (Supplier) getIntent().getSerializableExtra(BUNDLE_SUPPLY);
        } else {
            // ricrea l'activity deserializzando alcuni dati dal bundle
            supp = (Supplier) savedInstanceState.getSerializable(BUNDLE_SUPPLY);
        }

        TextView titleView = (TextView) findViewById(R.id.supply_title);
        titleView.setText(supp.getTitle());

        TextView ivaView = (TextView) findViewById(R.id.supply_iva);
        ivaView.setText(supp.getPiva());

        TextView addressView = (TextView) findViewById(R.id.supply_address);
        addressView.setText(supp.getAddress());

        TextView typeView = (TextView) findViewById(R.id.supply_type);
        typeView.setText(supp.getType());
    }
}
