package it.unive.dais.cevid.aac.util;

/**
 * Created by admin on 15/11/17.
 */

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.datadroid.lib.parser.AbstractAsyncParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.unive.dais.cevid.datadroid.lib.util.ProgressStepper;

/**
 *
 * @author admin
 */
public class PartecipazioniParser extends AbstractAsyncParser<PartecipazioniParser.Data,ProgressStepper> {
    private static String query = "http://dati.consip.it/api/action/datastore_search_sql?sql="
            + "SELECT%20*%20"
            + "FROM%20%22996e869f-d3a3-4938-bd87-38d8d688860a%22%20"
            + "WHERE%20%22Partita_Iva%22%20LIKE%20";
    private static String del = "%27";
    private final String url;

    public PartecipazioniParser(String iva) throws IOException{
        this.url = query + del + iva + del;

    }
    public List<Data> parse() throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept", "Application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();
        try {
            return parseJSON(new OkHttpClient().newCall(request).execute().body().string());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<Data> parseJSON(String string) throws JSONException {
        List r = new ArrayList();
        JSONObject jo = new JSONObject(string);
        JSONObject result = jo.getJSONObject("result");
        JSONArray array = result.getJSONArray("records");
        for(int i = 0; i < array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            Data d = new Data();
            d.esito = obj.optString("Esito_Partecipazione");
            d.forma_partecipazione = obj.optString("Forma_Partecipazione");
            d.data_aggiudicazione = obj.optString("Data_Aggiudicazione/Abilitazione");
            d.id_lotto = obj.optString("Identificativo_Lotto");
            d.full_text = obj.optString("_full_text");
            d.nome_iniziativa = obj.optString("#Denominazione_Iniziativa");
            d.tipo_strumento = obj.optString("Tipo_Strumento");
            d.rag_sociale = obj.optString("Ragione_Sociale");
            d.prog_partecipante = obj.optString("Progressivo_Partecipante");
            d.nome_partecipante = obj.optString("Denominazione_Partecipazione");
            d.nome_lotto = obj.optString("Denominazione_Lotto");
            d.piva = obj.optString("Partita_Iva");
            d.capogruppo = obj.optString("Flag_Capogruppo");
            d.id_download = obj.optString("_id");

            r.add(d);
        }
        return r;
    }

    public class Data implements Serializable{
        String esito,
                forma_partecipazione,
                data_aggiudicazione,
                id_lotto,
                full_text,
                nome_iniziativa,
                tipo_strumento,
                rag_sociale,
                prog_partecipante,
                nome_partecipante,
                nome_lotto,
                piva,
                capogruppo,
                id_download;

        public String getEsito() {
            return esito;
        }

        public String getForma_partecipazione() {
            return forma_partecipazione;
        }

        public String getData_aggiudicazione() {
            return data_aggiudicazione;
        }

        public String getId_lotto() {
            return id_lotto;
        }

        public String getFull_text() {
            return full_text;
        }

        public String getNome_iniziativa() {
            return nome_iniziativa;
        }

        public String getTipo_strumento() {
            return tipo_strumento;
        }

        public String getRag_sociale() {
            return rag_sociale;
        }

        public String getProg_partecipante() {
            return prog_partecipante;
        }

        public String getNome_partecipante() {
            return nome_partecipante;
        }

        public String getNome_lotto() {
            return nome_lotto;
        }

        public String getPiva() {
            return piva;
        }

        public String getCapogruppo() {
            return capogruppo;
        }

        public String getId_download() {
            return id_download;
        }
    }

}
