package it.unive.dais.cevid.aac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.unive.dais.cevid.aac.util.University;
import it.unive.dais.cevid.datadroid.template.R;

public class SelectionActivity extends AppCompatActivity {
    public static final String EXTRA_UNI = "UNI";
    private University university;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        university = (University) getIntent().getSerializableExtra(EXTRA_UNI);
    }
}
