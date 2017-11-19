package com.example.gianmarcocallegher.sprapp.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.gianmarcocallegher.sprapp.R;
import com.example.gianmarcocallegher.sprapp.SearchableActivity;

import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;

/**
 * Created by gianmarcocallegher on 15/11/17.
 */

public class Anno2013 extends Fragment {
    ArrayAdapter<Anno2013.Data> adapter;
    EditText inputSearch;
    ListView lv;
    List<String> voceSpese, spesePROCapite;
    String numero_abitanti;
    List<Anno2013.Data> datas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SearchableActivity activity = (SearchableActivity) getActivity();

        numero_abitanti = activity.getNumero_abitanti();
        List<SoldipubbliciParser.Data> spese_ente = activity.getSpese_Ente_2013();
        spesePROCapite = new ArrayList<>();
        datas = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.anno2013, container, false);

        lv = (ListView) rootView.findViewById(R.id.list_view);
        inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);

        for (SoldipubbliciParser.Data x : spese_ente) {
            datas.add(new Anno2013.Data(x.descrizione_codice, x.importo_2013));
        }

        for (SoldipubbliciParser.Data x : spese_ente)
            spesePROCapite.add(x.descrizione_codice + "\nSpesa Totale: " + (Double.parseDouble(x.importo_2013) / 100) +
                    "\nSpesa PRO-Capite: " + (Double.parseDouble(x.importo_2013) / 100) / Double.parseDouble(numero_abitanti)
            );

        adapter = new ArrayAdapter<>(getContext(), R.layout.list_spese, R.id.Spesa, datas);
        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Anno2013.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    public class Data {
        String descrizione_spesa;
        String importo;
        Double spesaPROCapite;

        public Data(String descrizione_spesa, String importo) {
            this.descrizione_spesa = descrizione_spesa;
            this.importo = importo;
            this.spesaPROCapite = Double.parseDouble(importo) / Double.parseDouble(numero_abitanti);
        }
    }
}
