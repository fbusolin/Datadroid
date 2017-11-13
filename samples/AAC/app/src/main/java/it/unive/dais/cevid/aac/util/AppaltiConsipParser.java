package it.unive.dais.cevid.aac.util;

/**
 * Created by admin on 10/11/17.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class AppaltiConsipParser {
    String bandi2017 = "377784b5-bb11-4a3e-a3a7-e1e48d122892";
    String bandi2016 = "5e12248d-07be-4e94-8be7-05b49787427f";
    String bandi2015 = "072fac7d-beda-4146-b574-1108e3bc030f";
    String base = "http://dati.consip.it/api/action/";
    String search ="datastore_search?resource_id=";

    public List parse(int n) throws IOException{
        Request request = new Request.Builder()
                .url(base+search+bandi2016)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept", "Application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();
        try{
            return parseJSON(new OkHttpClient().newCall(request).execute().body().string());
        }catch(JSONException ex){
            throw new IOException(ex);
        }
    }

    public List<Data> parseJSON(String data) throws JSONException{
        List<Data> r = new ArrayList();
        JSONObject jo = new JSONObject(data);
        JSONObject result = jo.getJSONObject("result");
        int n = result.getInt("total");
        JSONArray array = result.getJSONArray("records");
        for(int i = 0; i < array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            Data d = new Data();
            d.unita_misura = obj.optString("Unità_Misura");
            d.per_erosione = obj.optString("Percentuale_Erosione");
            d.crit_aggiudicazione = obj.optString("Criterio_Aggiudicazione");
            d.cat_merceologica = obj.optString("Categoria_Merceologica");
            d.tipo_strumento = obj.optString("Tipo_Strumento");
            d.flag_fee = obj.optString("Flag_Commissione_FEE");
            d.aggiudicazione_esclusiva = obj.optString("Aggiudicazione_Esclusiva");
            d.importo_massimale = obj.optString("Importo_Massimale");
            d.data_fine = obj.optString("Data_Termine");
            d.mod_svolgimento = obj.optString("Modalità_Svolgimento");
            d.data_pubb = obj.optString("Data_Pubblicazione");
            d.qnt_massimale = obj.optString("Quantità_Massimale");
            d.verde = obj.optString("Verde");
            d.den_lotto = obj.optString("Denominazione_Lotto");
            d.tipo_procedura = obj.optString("Tipo_Procedura");
            d.base_asta = obj.optString("Base_Asta");
            d.id_lotto = obj.optString("Identificativo_Lotto");
            d.n_operatori_part = obj.optString("Numero_Operatori_Economici_Partecipanti");
            d.tipo_lotto = obj.optString("Tipologia_Lotto");
            d.denominazione = obj.optString("#Denominazione_Bando");
            d.data_auth_75 = obj.optString("Data_Autorizzazione_7_5");
            d.data_attivazione = obj.optString("Data_Attivazione");
            d.data_auth_65 = obj.optString("Data_Autorizzazione_6_5");
            d.n_operatori_agg = obj.optString("Numero_Operatori_Economici_Aggiudicatari/Abilitati");
            d.data_agg = obj.optString("Data_Aggiudicazione");
            d.id_download = obj.optString("_id");
            r.add(d);
        }
        return r;
    }

    public static class Data {
        String unita_misura,
                per_erosione,
                crit_aggiudicazione,
                cat_merceologica,
                tipo_strumento,
                flag_fee,
                aggiudicazione_esclusiva,
                importo_massimale,
                data_fine,
                mod_svolgimento,
                data_pubb,
                qnt_massimale,
                verde,
                den_lotto,
                tipo_procedura,
                base_asta,
                id_lotto,
                n_operatori_part,
                tipo_lotto,
                denominazione,
                data_auth_75,
                data_attivazione,
                data_auth_65,
                n_operatori_agg,
                data_agg,
                id_download;
        public Data() {
        }
    }


}
