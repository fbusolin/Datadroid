package it.unive.dais.cevid.lib.parser;

import android.support.annotation.NonNull;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class AppaltiParser<Progress> extends AbstractDataParser<AppaltiParser.Data, Progress> {
    private static final String TAG = "AppaltiParser";
    private URL url;

    public AppaltiParser(URL url) {
        this.url = url;
    }

    @NonNull
    @Override
    protected List<Data> parse() throws IOException {
        try {
            Log.d(TAG, "downloading " + url);
            URLConnection conn = url.openConnection();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());
            NodeList nodes = doc.getElementsByTagName("lotto");
            Log.d(TAG, "downloaded " + nodes.getLength() + " nodes");
            return parseNodes(nodes);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }

    protected List<Data> parseNodes(NodeList nodes) {
        List<Data> r = new ArrayList<>();
        for (int i = 0; i< nodes.getLength(); i++){
            Node nodo = nodes.item(i);
            Data d = new Data();

            d.cig = nodo.getFirstChild().getTextContent();

            if( nodo.getChildNodes().item(5).hasChildNodes()){
            d.aggiudicatario = nodo.getChildNodes().item(5).getFirstChild().getLastChild().getTextContent();
            d.codiceFiscaleAgg = nodo.getChildNodes().item(5).getFirstChild().getFirstChild().getTextContent();
            } else{
                d.aggiudicatario="Dati assenti o malformattati";
                d.codiceFiscaleAgg="0";
            }
            d.proponente = nodo.getChildNodes().item(1).getLastChild().getTextContent();
            d.codiceFiscaleProp = nodo.getChildNodes().item(1).getFirstChild().getTextContent();
            d.oggetto = nodo.getChildNodes().item(2).getTextContent();
            d.sceltac = nodo.getChildNodes().item(3).getTextContent();
            d.importo = nodo.getChildNodes().item(6).getTextContent();
            d.importoSommeLiquidate = nodo.getLastChild().getTextContent();
            d.dataFine = nodo.getChildNodes().item(7).getLastChild().getTextContent();
            d.dataInizio = nodo.getChildNodes().item(7).getFirstChild().getTextContent();

            r.add(d);
        }
        return r;
    }


    public static class Data {
        public String cig;
        public String proponente;
        public String codiceFiscaleProp;
        public String oggetto;
        public String sceltac;
        public String aggiudicatario;
        public String codiceFiscaleAgg;
        public String importo;
        public String importoSommeLiquidate;
        public String dataInizio;
        public String dataFine;
    }
}
