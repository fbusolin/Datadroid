package it.unive.dais.cevid.datadroid.parser;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Fonto on 29/08/17.
 */

public class SoldiPubbliciParser {

    private OkHttpClient client;
    private String codiceComparto;
    private String codiceEnte;
    private String responseString;
    private List<Data> datalist;
    private static final String TAG = "SoldiPubbliciParser";
    private boolean ready = false;
    private Object lock= new Object();

    public SoldiPubbliciParser(String codiceComparto, String codiceEnte) {
        this.codiceComparto = codiceComparto;
        this.codiceEnte = codiceEnte;
        download();
    }


    private void download() {
        client = new OkHttpClient();
        try {
            post(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    synchronized (lock){

                        responseString = response.body().string();
                        parse(responseString);
                        ready = true;
                        lock.notifyAll();
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private okhttp3.Call post(Callback callback) throws IOException {
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

        okhttp3.Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public List<Data> parse(String data){
        try {
            JSONObject jo = new JSONObject(data);
            JSONArray ja = jo.getJSONArray("data");
            datalist = new ArrayList<>();
            Log.d(TAG, "parse: "+ja.getJSONObject(1).toString() );
            for (int i =0; i< ja.length(); i++){
                JSONObject j = ja.getJSONObject(i);
                Data d = new Data();

                d.setAnno(j.get("anno").toString());
                d.setCod_ente(j.get("cod_ente").toString());
                d.setCodice_gestionale(j.get("codice_gestionale").toString());
                d.setCodice_siope(j.get("codice_siope").toString());
                d.setData_di_fine_validita(j.get("data_di_fine_validita").toString());
                d.setDescrizione_codice(j.get("descrizione_codice").toString());
                d.setDescrizione_ente(j.get("descrizione_ente").toString());
                d.setIdtable(j.get("idtable").toString());
                d.setImp_uscite_att(j.get("imp_uscite_att").toString());
                d.setImporto_2013(j.get("importo_2013").toString());
                d.setImporto_2014(j.get("importo_2014").toString());
                d.setImporto_2015(j.get("importo_2015").toString());
                d.setImporto_2016(j.get("importo_2016").toString());
                d.setImporto_2017(j.get("importo_2017").toString());
                d.setRicerca(j.get("ricerca").toString());
                d.setPeriodo(j.get("periodo").toString());


                datalist.add(d);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "parse: "+ datalist.get(0).getAnno());
        return datalist;
    }

    public List<Data> getDatalist() {
        synchronized (lock){
            while (ready==false)
                try {
                    lock.wait((long) 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            Log.d(TAG, "parse: "+ datalist.get(0).getAnno());
            return datalist;
        }
    }

    public List<Data> filterData(List<CharSequence> ss){
        synchronized (lock) {
            while (ready == false)
                try {
                    lock.wait((long) 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }

        List<Data> dd = new ArrayList<>();
        for (SoldiPubbliciParser.Data d: datalist) {
            for (CharSequence s:ss) {
                if (d.getDescrizione_codice().contains(s))
                    if (!dd.contains(d))
                        dd.add(d);
            }
        }
        return dd;
    }


    public class Data {
        private String descrizione_codice;
        private String codice_siope;
        private String descrizione_ente;
        private String ricerca;
        private String idtable;
        private String cod_ente;
        private String anno;
        private String periodo;
        private String codice_gestionale;
        private String imp_uscite_att;
        private String data_di_fine_validita;
        private String importo_2013;
        private String importo_2014;
        private String importo_2015;
        private String importo_2016;
        private String importo_2017;

        public String getDescrizione_codice() {
            return descrizione_codice;
        }

        public void setDescrizione_codice(String descrizione_codice) {
            this.descrizione_codice = descrizione_codice;
        }

        public String getCodice_siope() {
            return codice_siope;
        }

        public void setCodice_siope(String codice_siope) {
            this.codice_siope = codice_siope;
        }

        public String getDescrizione_ente() {
            return descrizione_ente;
        }

        public void setDescrizione_ente(String descrizione_ente) {
            this.descrizione_ente = descrizione_ente;
        }

        public String getRicerca() {
            return ricerca;
        }

        public void setRicerca(String ricerca) {
            this.ricerca = ricerca;
        }

        public String getIdtable() {
            return idtable;
        }

        public void setIdtable(String idtable) {
            this.idtable = idtable;
        }

        public String getCod_ente() {
            return cod_ente;
        }

        public void setCod_ente(String cod_ente) {
            this.cod_ente = cod_ente;
        }

        public String getAnno() {
            return anno;
        }

        public void setAnno(String anno) {
            this.anno = anno;
        }

        public String getPeriodo() {
            return periodo;
        }

        public void setPeriodo(String periodo) {
            this.periodo = periodo;
        }

        public String getCodice_gestionale() {
            return codice_gestionale;
        }

        public void setCodice_gestionale(String codice_gestionale) {
            this.codice_gestionale = codice_gestionale;
        }

        public String getImp_uscite_att() {
            return imp_uscite_att;
        }

        public void setImp_uscite_att(String imp_uscite_att) {
            this.imp_uscite_att = imp_uscite_att;
        }

        public String getData_di_fine_validita() {
            return data_di_fine_validita;
        }

        public void setData_di_fine_validita(String data_di_fine_validita) {
            this.data_di_fine_validita = data_di_fine_validita;
        }

        public String getImporto_2013() {
            return importo_2013;
        }

        public void setImporto_2013(String importo_2013) {
            this.importo_2013 = importo_2013;
        }

        public String getImporto_2014() {
            return importo_2014;
        }

        public void setImporto_2014(String importo_2014) {
            this.importo_2014 = importo_2014;
        }

        public String getImporto_2015() {
            return importo_2015;
        }

        public void setImporto_2015(String importo_2015) {
            this.importo_2015 = importo_2015;
        }

        public String getImporto_2016() {
            return importo_2016;
        }

        public void setImporto_2016(String importo_2016) {
            this.importo_2016 = importo_2016;
        }

        public String getImporto_2017() {
            return importo_2017;
        }

        public void setImporto_2017(String importo_2017) {
            this.importo_2017 = importo_2017;
        }
    }

}
