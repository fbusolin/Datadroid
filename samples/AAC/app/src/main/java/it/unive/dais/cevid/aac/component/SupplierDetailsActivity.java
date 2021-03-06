package it.unive.dais.cevid.aac.component;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import it.unive.dais.cevid.aac.R;
import it.unive.dais.cevid.aac.parser.TenderParser;

public class SupplierDetailsActivity extends AppCompatActivity {
    public static final String TAG = "SupplierDetailsActivity";
    protected static final String BUNDLE_BID = "BID";
    private TenderParser.Data tender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

        this.tender = (TenderParser.Data) getIntent().getSerializableExtra(BUNDLE_BID);
        TextView title = (TextView) findViewById(R.id.bid_title);
        TextView desc = (TextView) findViewById(R.id.bid_desc);
        TextView lotto = (TextView) findViewById(R.id.bid_lotto);
        TextView mass = (TextView) findViewById(R.id.bid_massimale);

        title.setText(tender.denominazione);
        desc.setText(tender.denominazione_lotto);
        lotto.setText(tender.id_lotto);
        mass.setText(tender.getMassimale() + getString(R.string.euro_symbol));
    }
}
