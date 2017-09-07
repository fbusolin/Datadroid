package it.unive.dais.cevid.aac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import it.unive.dais.cevid.aac.util.University;
import it.unive.dais.cevid.datadroid.template.R;

public class SearchActivity extends AppCompatActivity {
    public static final String EXTRA_UNI = "UNI";
    private University university;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        university = (University) getIntent().getSerializableExtra(EXTRA_UNI);
        title = (TextView) findViewById(R.id.univeristy_name);
        title.setText(university.getTitle());

    }
}
