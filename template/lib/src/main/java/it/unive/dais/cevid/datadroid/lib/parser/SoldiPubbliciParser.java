package it.unive.dais.cevid.datadroid.lib.parser;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Sottoclasse di {@code AbstractCsvParser} che implementa un donwloader e parser per il sito soldipubblici.gov.it.
 * Questa classe è usabile direttamente e non necessita di essere ereditata.
 * Non richiede il generic FiltrableData perché utilizza una classe innestata apposita per rappresentare il risultato della richiesta in maniera untyped ma generale tramite un dizionario.
 * Un esempio d'uso con un file CSV con header e virgole come separatore:
 * <blockquote><pre>
 * {@code
 * SoldiPubbliciParser parser = new SoldiPubbliciParser(1, 2);
 * List<SoldiPubbliciParser.FiltrableData> rows = parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
 * for (CsvRowParser.Row row : rows) {
 *     String id = row.get("ID"), nome = row.get("NAME");
 *     // fai qualcosa con id e nome
 * }
 * }
 * </pre></blockquote>
 *
 * @author Alvise Spanò, Università Ca' Foscari
 * @param <Progress>
 */
public class SoldiPubbliciParser<Progress> extends AbstractDataParser<SoldiPubbliciParser.Data, Progress> {

    private static final String TAG = "SoldiPubbliciParser";

    protected String codiceComparto;
    protected String codiceEnte;

    public SoldiPubbliciParser(String codiceComparto, String codiceEnte) {
        this.codiceComparto = codiceComparto;
        this.codiceEnte = codiceEnte;
    }

    @NonNull
    @Override
    protected List<Data> parse() throws IOException {
        RequestBody fromRequest = new FormBody.Builder()
                .add("codicecomparto", codiceComparto)
                .add("codiceente", codiceEnte)
                .build();

        Request request = new Request.Builder()
                .url("http://soldipubblici.gov.it/it/ricerca")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept", "Application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .post(fromRequest)
                .build();

        try {
            return parseJSON(new OkHttpClient().newCall(request).execute().body().string());
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    protected List<Data> parseJSON(String data) throws JSONException {
        List<Data> r = new ArrayList<>();
        JSONObject jo = new JSONObject(data);
        JSONArray ja = jo.getJSONArray("data");
        Log.d(TAG, "parse: "+ja.getJSONObject(1).toString() );
        for (int i =0; i< ja.length(); i++){
            JSONObject j = ja.getJSONObject(i);
            Data d = new Data();
            d.anno = j.getString("anno");
            d.cod_ente = j.getString("cod_ente");
            d.codice_gestionale = j.getString("codice_gestionale");
            d.codice_siope = j.getString("codice_siope");
            d.data_di_fine_validita = j.getString("data_di_fine_validita");
            d.descrizione_codice = j.getString("descrizione_codice");
            d.descrizione_ente = j.getString("descrizione_ente");
            d.idtable = j.getString("idtable");
            d.imp_uscite_att = j.getString("imp_uscite_att");
            d.importo_2013 = j.getString("importo_2013");
            d.importo_2014 = j.getString("importo_2014");
            d.importo_2015 = j.getString("importo_2015");
            d.importo_2016 = j.getString("importo_2016");
            d.importo_2017 = j.getString("importo_2017");
            d.ricerca = j.getString("ricerca");
            d.periodo = j.getString("periodo");
            r.add(d);
        }
        Log.d(TAG, String.format("parsed %d items", r.size()));
        return r;
    }


    public static class Data implements Serializable {
        public String descrizione_codice;
        public String codice_siope;
        public String descrizione_ente;
        public String ricerca;
        public String idtable;
        public String cod_ente;
        public String anno;
        public String periodo;
        public String codice_gestionale;
        public String imp_uscite_att;
        public String data_di_fine_validita;
        public String importo_2013;
        public String importo_2014;
        public String importo_2015;
        public String importo_2016;
        public String importo_2017;

    }

}
