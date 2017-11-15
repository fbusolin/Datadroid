package com.example.gianmarcocallegher.sprapp.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gianmarcocallegher.sprapp.R;
import com.example.gianmarcocallegher.sprapp.SearchableActivity;

import java.util.List;

import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;

/**
 * Created by gianmarcocallegher on 15/11/17.
 */

public class Anno2013 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SearchableActivity activity = (SearchableActivity) getActivity();

        String numero_abitanti = activity.getNumero_abitanti();
        List<SoldipubbliciParser.Data> spese_ente_2013 = activity.getSpese_Ente_2013();

        return inflater.inflate(R.layout.anno2013, container, false);
    }
}
