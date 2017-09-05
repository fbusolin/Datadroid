package it.unive.dais.cevid.datadroid.parser;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Fonto on 29/08/17.
 */

public class AppaltiParser extends AsyncTask<Void, Void, List<AppaltiParser.Data>> {
    private List<URL> urls;
    private NodeList nodes;
    private ArrayList<Data> datalist;
    private static final String TAG = "AppaltiParser";

    public AppaltiParser(List<URL> url) {
        this.urls = url;
    }

    private List<Data> downloadXMLFile() {
        for (URL u: urls
             ) {
            URL url = u;
            URLConnection conn = null;
            try {
                conn = url.openConnection();
                Log.d(TAG, "downloadXMLFile: "+ url.toString());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(conn.getInputStream());
                nodes = doc.getElementsByTagName("lotto");

                Log.d(TAG, "downloadXMLFile: "+nodes.item(0).getTextContent());

                parser();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return datalist;
    }

    private List<Data> parser(){
        if (datalist == null)
            datalist = new ArrayList<>();
        for (int i = 0; i< nodes.getLength(); i++){
            Node nodo = nodes.item(i);
            Data data = new Data();

            data.setCig(nodo.getFirstChild().getTextContent());

            if( nodo.getChildNodes().item(5).hasChildNodes()){
                data.setAggiudicatario(nodo.getChildNodes().item(5).getFirstChild().getLastChild().getTextContent());
                data.setCodiceFiscaleAgg(nodo.getChildNodes().item(5).getFirstChild().getFirstChild().getTextContent());
            }
            else {
                data.setAggiudicatario("Dati assenti o malformattati");
                data.setCodiceFiscaleAgg("0");
            }
            Log.d(TAG, "parser: "+data.getAggiudicatario() +" "+ data.getCig());
            data.setPorponente(nodo.getChildNodes().item(1).getLastChild().getTextContent());//ok
            data.setCodiceFiscaleProp(nodo.getChildNodes().item(1).getFirstChild().getTextContent());//ok

            data.setOggetto(nodo.getChildNodes().item(2).getTextContent());//ok
            data.setSceltac(nodo.getChildNodes().item(3).getTextContent());//ok

            data.setImporto(nodo.getChildNodes().item(6).getTextContent());
            data.setImportoSommeLiquidate(nodo.getLastChild().getTextContent());

            data.setDataFine(nodo.getChildNodes().item(7).getLastChild().getTextContent());
            data.setDataInizio(nodo.getChildNodes().item(7).getFirstChild().getTextContent());

            datalist.add(data);
        }
        return datalist;
    }

    public Double sumImporto(){
        Double i = 0.0;
        for (Data d:
             datalist) {
            Double a = new Double(d.getImportoSommeLiquidate());
            i += a;
        }
        return i;
    }


    public List<Data> filterData(List<CharSequence> ss){
        List<Data> dd = new ArrayList<>();
        for (Data d: datalist) {
            for (CharSequence s:ss) {
                if (d.getOggetto().contains(s))
                    if (!dd.contains(d))
                        dd.add(d);
            }
        }
        return dd;
    }


    @Override
    protected List<Data> doInBackground(Void... voids) {
        return downloadXMLFile();
    }



    public class Data {
        private String cig;
        private String porponente;
        private String codiceFiscaleProp;
        private String oggetto;
        private String sceltac;
        private String aggiudicatario;
        private String codiceFiscaleAgg;
        private String importo;
        private String importoSommeLiquidate;
        private String dataInizio;
        private String dataFine;

        public String getCig() {
            return cig;
        }

        public void setCig(String cig) {
            this.cig = cig;
        }

        public String getPorponente() {
            return porponente;
        }

        public void setPorponente(String porponente) {
            this.porponente = porponente;
        }

        public String getCodiceFiscaleProp() {
            return codiceFiscaleProp;
        }

        public void setCodiceFiscaleProp(String codiceFiscaleProp) {
            this.codiceFiscaleProp = codiceFiscaleProp;
        }

        public String getOggetto() {
            return oggetto;
        }

        public void setOggetto(String oggetto) {
            this.oggetto = oggetto;
        }

        public String getSceltac() {
            return sceltac;
        }

        public void setSceltac(String sceltac) {
            this.sceltac = sceltac;
        }

        public String getAggiudicatario() {
            return aggiudicatario;
        }

        public void setAggiudicatario(String aggiudicatario) {
            this.aggiudicatario = aggiudicatario;
        }

        public String getCodiceFiscaleAgg() {
            return codiceFiscaleAgg;
        }

        public void setCodiceFiscaleAgg(String codiceFiscaleAgg) {
            this.codiceFiscaleAgg = codiceFiscaleAgg;
        }

        public String getImporto() {
            return importo;
        }

        public void setImporto(String importo) {
            this.importo = importo;
        }

        public String getImportoSommeLiquidate() {
            return importoSommeLiquidate;
        }

        public void setImportoSommeLiquidate(String importoSommeLiquidate) {
            this.importoSommeLiquidate = importoSommeLiquidate;
        }

        public String getDataInizio() {
            return dataInizio;
        }

        public void setDataInizio(String dataInizio) {
            this.dataInizio = dataInizio;
        }

        public String getDataFine() {
            return dataFine;
        }

        public void setDataFine(String dataFine) {
            this.dataFine = dataFine;
        }
    }
}
