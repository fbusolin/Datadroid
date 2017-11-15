package it.unive.dais.cevid.aac.util;

/**
 * Created by admin on 15/11/17.
 */

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.datadroid.lib.parser.AbstractAsyncParser;
import it.unive.dais.cevid.datadroid.lib.util.ProgressStepper;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class BandiParser extends AbstractAsyncParser<BandiParser.Data, ProgressStepper> {
    public static final String TAG = "BandiParser";
    private static String query = "http://dati.consip.it/api/action/datastore_search_sql?"
            + "sql=SELECT%20*%20"
            + "FROM%20%225e12248d-07be-4e94-8be7-05b49787427f%22%20"
            + "WHERE%20%22Identificativo_Lotto%22LIKE%20";
    private static String del = "%27";
    private final String url;
    public BandiParser(String lotto){
        this.url = query + del + lotto + del;
    }

    public List parse() throws IOException {
        Request request = new Request.Builder()
                .url(this.url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept", "Application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();
        try {
            return parseJSON(new OkHttpClient().newCall(request).execute().body().string());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    public List<Data> parseJSON(String data) throws JSONException{
        List r = new ArrayList();
        JSONObject jo = new JSONObject(data);
        JSONObject result = jo.getJSONObject("result");
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

    public static class Data implements Serializable{
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

        public String getUnita_misura() {
            return unita_misura;
        }

        public String getPer_erosione() {
            return per_erosione;
        }

        public String getCrit_aggiudicazione() {
            return crit_aggiudicazione;
        }

        public String getCat_merceologica() {
            return cat_merceologica;
        }

        public String getTipo_strumento() {
            return tipo_strumento;
        }

        public String getFlag_fee() {
            return flag_fee;
        }

        public String getAggiudicazione_esclusiva() {
            return aggiudicazione_esclusiva;
        }

        public String getImporto_massimale() {
            return importo_massimale;
        }

        public String getData_fine() {
            return data_fine;
        }

        public String getMod_svolgimento() {
            return mod_svolgimento;
        }

        public String getData_pubb() {
            return data_pubb;
        }

        public String getQnt_massimale() {
            return qnt_massimale;
        }

        public String getVerde() {
            return verde;
        }

        public String getDen_lotto() {
            return den_lotto;
        }

        public String getTipo_procedura() {
            return tipo_procedura;
        }

        public String getBase_asta() {
            return base_asta;
        }

        public String getId_lotto() {
            return id_lotto;
        }

        public String getN_operatori_part() {
            return n_operatori_part;
        }

        public String getTipo_lotto() {
            return tipo_lotto;
        }

        public String getDenominazione() {
            return denominazione;
        }

        public String getData_auth_75() {
            return data_auth_75;
        }

        public String getData_attivazione() {
            return data_attivazione;
        }

        public String getData_auth_65() {
            return data_auth_65;
        }

        public String getN_operatori_agg() {
            return n_operatori_agg;
        }

        public String getData_agg() {
            return data_agg;
        }

        public String getId_download() {
            return id_download;
        }
    }


}