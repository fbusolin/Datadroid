package it.unive.dais.cevid.aac.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;



import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.aac.R;
import it.unive.dais.cevid.aac.component.MunicipalityResultActivity;
import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;

/**
 * Created by gianmarcocallegher on 15/11/17.
 */

public class Anno2017Fragment extends Fragment {

    ArrayAdapter<String> adapter;
    EditText inputSearch;
    ListView lv;
    List<String> spesePROCapite;
    String numero_abitanti;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MunicipalityResultActivity activity = (MunicipalityResultActivity) getActivity();

        numero_abitanti = activity.getNumero_abitanti();
        List<SoldipubbliciParser.Data> spese_ente = activity.getSpese_Ente_2017();
        spesePROCapite = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.anno2017, container, false);

        lv = (ListView) rootView.findViewById(R.id.list_view);
        inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);

        for (SoldipubbliciParser.Data x : spese_ente){
            double spesa = 0;
            try{ spesa = Double.parseDouble(x.importo_2017);}
            catch(NumberFormatException ex){
                spesa = 0;
            }
            spesePROCapite.add(x.descrizione_codice + "\nSpesa Totale: " + (spesa / 100) +
                    "\nSpesa PRO-Capite: " + (spesa / 100) / Double.parseDouble(numero_abitanti)
            );}


        adapter = new ArrayAdapter<>(getContext(), R.layout.list_spese, R.id.Spesa, spesePROCapite);
        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Anno2017Fragment.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }
}
