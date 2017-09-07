package it.unive.dais.cevid.lib.parser;

import android.support.annotation.NonNull;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
    private static final String DATI_ASSENTI_O_MAL_FORMATTATI = "Dati assenti o mal formattati";

    private List<URL> urls;

    public AppaltiParser(List<URL> url) {
        this.urls = url;
    }

    @NonNull
    @Override
    protected List<Data> parse() throws IOException {
        NodeList nodes;
        List<Data> datalist = new ArrayList<>();
        for (URL url : urls) {
            try {
                Log.d(TAG, "downloading " + url);
                URLConnection conn = url.openConnection();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(conn.getInputStream());
                nodes = doc.getElementsByTagName("lotto");

                Log.d(TAG, "downloaded " + nodes.getLength() + " nodes");

                datalist.addAll(parseNodes(nodes));

            } catch (ParserConfigurationException | SAXException e) {
                throw new IOException(e);
            }
        }
        return datalist;
    }

    protected String getTextByTag(Element e, String tagName, String defaultString) {
        Node n = getElementByTag(e, tagName);
        return n == null ? defaultString : n.getTextContent();
    }

    protected String getTextByTag(Element e, String tagName) {
        return getTextByTag(e, tagName, DATI_ASSENTI_O_MAL_FORMATTATI);
    }

    protected Element getElementByTag(Element e, String tagName) {
        return (Element) e.getElementsByTagName(tagName).item(0);
    }

    protected List<Data> parseNodes(NodeList nodes) {
        List<Data> r = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element parent = (Element) nodes.item(i);
            Data d = new Data();

            //controlli aggiudicatario
            {
                Element e = getElementByTag(parent, "aggiudicatario");
                if (e != null) {
                    d.aggiudicatario = getTextByTag(e, "ragioneSociale");
                    d.codiceFiscaleAgg = getTextByTag(e, "codiceFiscale", "0");
                } else {
                    d.aggiudicatario = DATI_ASSENTI_O_MAL_FORMATTATI;
                    d.codiceFiscaleAgg = "0";
                }

            }

            {
                d.oggetto = getTextByTag(parent, "oggetto");
            }



            // TODO: riscrivere il seguente codice con l'API nuova

            if (parent.getElementsByTagName("aggiudicatario").item(0) != null) {
                Element e = (Element) parent.getElementsByTagName("aggiudicatario").item(0);
                if (e.getElementsByTagName("ragioneSociale").item(0) != null)
                    d.aggiudicatario = e.getElementsByTagName("ragioneSociale").item(0).getTextContent();
                else
                    d.aggiudicatario = "Dati assenti o malformattati";

                if (e.getElementsByTagName("codiceFiscale").item(0) != null)
                    d.codiceFiscaleAgg = e.getElementsByTagName("codiceFiscale").item(0).getTextContent();
                else
                    d.codiceFiscaleAgg = "0";
            } else {
                d.aggiudicatario = "Dati assenti o malformattati";
                d.codiceFiscaleAgg = "0";
            }


            //controllo porponente
            if (parent.getElementsByTagName("strutturaProponente").item(0) != null) {

                Element s = (Element) parent.getElementsByTagName("strutturaProponente").item(0);
                if (s.getElementsByTagName("ragioneSociale").item(0) != null)
                    d.proponente = s.getElementsByTagName("ragioneSociale").item(0).getTextContent();
                else
                    d.proponente = "Dati assenti o malformattati";

                if (s.getElementsByTagName("codiceFiscale").item(0) != null)
                    d.codiceFiscaleProp = s.getElementsByTagName("codiceFiscale").item(0).getTextContent();
                else
                    d.codiceFiscaleProp = "0";

            } else {
                d.aggiudicatario = "Dati assenti o malformattati";
                d.codiceFiscaleAgg = "0";
            }

            //controllo oggetto
            if (parent.getElementsByTagName("oggetto").item(0) != null) {
                d.oggetto = parent.getElementsByTagName("oggetto").item(0).getTextContent();
            } else {
                d.oggetto = "Dati assenti o malformattati";
            }

            //controllo scelta contraente
            if (parent.getElementsByTagName("sceltaContraente").item(0) != null) {
                d.sceltac = parent.getElementsByTagName("sceltaContraente").item(0).getTextContent();
            } else {
                d.sceltac = "Dati assenti o malformattati";
            }

            //controllo importo
            if (parent.getElementsByTagName("importo").item(0) != null) {
                d.importo = parent.getElementsByTagName("importo").item(0).getTextContent();
            } else {
                d.importo = "0";
            }

            //controllo importo somme liquidate
            if (parent.getElementsByTagName("importoSommeLiquidate").item(0) != null) {
                d.importoSommeLiquidate = parent.getElementsByTagName("importoSommeLiquidate").item(0).getTextContent();
            } else {
                d.importoSommeLiquidate = "0";
            }

            //controllo tempi di completamento
            if (parent.getElementsByTagName("tempiCompletamento").item(0) != null) {
                Element s = (Element) parent.getElementsByTagName("tempiCompletamento").item(0);
                if (s.getElementsByTagName("dataUltimazione").item(0) != null)
                    d.dataFine = s.getElementsByTagName("dataUltimazione").item(0).getTextContent();
                else
                    d.dataFine = "Dati assenti o malformattati";

                if (s.getElementsByTagName("dataInizio").item(0) != null)
                    d.dataInizio = s.getElementsByTagName("dataInizio").item(0).getTextContent();
                else
                    d.dataInizio = "Dati assenti o malformattati";
            } else {
                d.dataFine = "Dati assenti o malformattati";
                d.dataInizio = "Dati assenti o malformattati";
            }

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
