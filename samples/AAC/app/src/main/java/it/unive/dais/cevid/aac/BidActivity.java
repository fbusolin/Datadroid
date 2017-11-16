package it.unive.dais.cevid.aac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import it.unive.dais.cevid.aac.entities.Bid;

public class BidActivity extends AppCompatActivity {
    public static final String TAG = "BidActivity";
    public static final String BUNDLE_BID = "BID";
    private Bid bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

        this.bid = (Bid) getIntent().getSerializableExtra(BUNDLE_BID);
        TextView title = (TextView) findViewById(R.id.bid_title);
        TextView desc = (TextView) findViewById(R.id.bid_desc);
        TextView lotto = (TextView) findViewById(R.id.bid_lotto);
        TextView mass = (TextView) findViewById(R.id.bid_massimale);

        title.setText(bid.getTitle());
        desc.setText(bid.getDescrizione());
        lotto.setText(bid.getIdLotto());
        mass.setText(bid.getMassimale() + "¢");
    }
}
