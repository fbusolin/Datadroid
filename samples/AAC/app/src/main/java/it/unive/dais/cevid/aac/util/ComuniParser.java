package it.unive.dais.cevid.aac.util;

import android.support.annotation.NonNull;
import android.webkit.JsResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.unive.dais.cevid.datadroid.lib.parser.AbstractAsyncParser;
import it.unive.dais.cevid.datadroid.lib.util.ProgressStepper;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by fbusolin on 23/11/17.
 */

public class ComuniParser extends AbstractAsyncParser<ComuniParser.Data, ProgressStepper> {
    private String nome;
    private static String single = "%27";
    private static String pair = "%22";
    private static String space = "%20";
    private String base = "http://ckan.ancitel.it/api/action/datastore_search_sql?sql=" +
            "SELECT%20*%20" +
            "FROM%20%22c381efe6-f73f-4e20-a825-547241eeb457%22%20" +
            "WHERE";
    private String query;
    public ComuniParser(String nome){
        this.nome = nome;
        this.query = base + space +
                pair + "Comune" + pair + space +
                "LIKE" + space + single + this.nome + single;

        /*SELECT *
        * FROM ...
        * WHERE "Comune" LIKE '[nome]'*/
    }
    @NonNull
    @Override
    public List<Data> parse() throws IOException {
        OkHttpClient client = new OkHttpClient();
        List returnList = new ArrayList();
        Request request = new Request.Builder()
                .url(this.query)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept", "Application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();
        try{
            returnList.addAll(parseJSON(client.newCall(request).execute().body().string()));
        }catch(JSONException ex){
            ex.printStackTrace();;
        }finally {
            return returnList;
        }
    }

    private List<Data> parseJSON(String data) throws JSONException {
        List<Data> list = new ArrayList<>();
        JSONObject jo = new JSONObject(data);
        JSONObject result = jo.getJSONObject("result");
        JSONArray array = result.getJSONArray("records");
        ProgressStepper prog = new ProgressStepper(array.length());
        for(int i = 0; i < array.length();i++){
            JSONObject obj = array.getJSONObject(i);
            Data d = new Data();
            d._id = obj.optString("_id");
            d.montano = obj.optString("IndiceMontanita");
            d.provincia = obj.optString("Provincia");
            d.regione = obj.optString("Regione");
            d.comune = obj.optString("Comune");
            d.classe = obj.optString("ClasseComune");
            d.tipo = obj.optString("TipoComune");
            d.superficie = obj.optString("SuperficieKmq");
            d.urbanizzazione = obj.optString("GradoUrbaniz");
            d.minAltezza = obj.optString("AltezzzaMinima");
            d.zonaAltimetrica = obj.optString("ZonaAltimetrica");
            d.altezzaCentro = obj.optString("AltezzaCentro");
            d.ISTAT = obj.optString("ISTAT");
            d.lat = obj.optString("Latitudine");
            d.lng = obj.optString("Longitudine");
            d.areaGeo = obj.optString("AreaGeo");
            d.zonaClima = obj.optString("ZonaClimatica");
            d.zonaSismica = obj.optString("ZonaSismica");
            d.popStraniera = obj.optString("PopStraniera");
            d.popResidente = obj.optString("PopResidente");
            d.densita = obj.optString("DensitaDemografica");
            d.altezzaMassima = obj.optString("AltezzaMassima");
            d.siglaProv = obj.optString("SiglaProv");
            list.add(d);
            prog.step();
            publishProgress(prog);
        }

        return list;
    }

    public class Data implements Serializable{
        private String montano,
        provincia,
        regione,
        comune,
        classe,
        tipo,
        superficie,
        urbanizzazione,
        minAltezza,
        zonaAltimetrica,
        altezzaCentro,
        ISTAT,
        lat,
        lng,
        areaGeo,
        zonaSismica,
        popStraniera,
        popResidente,
        densita,
        altezzaMassima,
        siglaProv,
        zonaClima,
        _id;

        public String getMontano() {
            return montano;
        }

        public String getProvincia() {
            return provincia;
        }

        public String getRegione() {
            return regione;
        }

        public String getComune() {
            return comune;
        }

        public String getClasse() {
            return classe;
        }

        public String getTipo() {
            return tipo;
        }

        public String getSuperficie() {
            return superficie;
        }

        public String getUrbanizzazione() {
            return urbanizzazione;
        }

        public String getMinAltezza() {
            return minAltezza;
        }

        public String getZonaAltimetrica() {
            return zonaAltimetrica;
        }

        public String getAltezzaCentro() {
            return altezzaCentro;
        }

        public String getISTAT() {
            return ISTAT;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }

        public String getAreaGeo() {
            return areaGeo;
        }

        public String getZonaSismica() {
            return zonaSismica;
        }

        public String getPopStraniera() {
            return popStraniera;
        }

        public String getPopResidente() {
            return popResidente;
        }

        public String getDensita() {
            return densita;
        }

        public String getAltezzaMassima() {
            return altezzaMassima;
        }

        public String getSiglaProv() {
            return siglaProv;
        }

        public String getZonaClima() {
            return zonaClima;
        }

        public String get_id() {
            return _id;
        }
    }
}
