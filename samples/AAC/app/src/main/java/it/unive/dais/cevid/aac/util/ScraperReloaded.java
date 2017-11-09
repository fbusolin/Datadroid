package it.unive.dais.cevid.aac.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by fbusolin on 09/11/17.
 */

public class ScraperReloaded {
    String url;
    public ScraperReloaded(String url){
        this.url = url;
    }
    public List<Data> scrape(){
        List<Data> data = new ArrayList<>();
        Connection connection = Jsoup.connect(url);
        Document document = null;
        try{
            document = connection.get();
        }catch(IOException ex){
            Logger.getLogger(ScraperReloaded.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(document == null)return data;
        Elements contentTable = document.select(".table-hover");
        Elements rows = contentTable.select("tr");
        for(Element elm : rows.subList(1,rows.size())){
            String texts = "";
            Elements tokens = elm.select("td");
            for(Element piece: tokens){
                texts +=piece.text() + ";";
            }
            switch(texts.charAt(0)){
                case 'C':
                    data.add(produceComuni(texts));
                    break;
                case 'S':
                    data.add(produceSiopi(texts));
                    break;
            }
        }
        return data;

    }
    private Siope produceSiopi(String str){
        String[] tokens = str.split(";");
        Siope result = new Siope();
        result.codice = tokens[0];
        result.descrizione = tokens[1];
        result.pagamento = tokens[2];
        result.procapite = tokens[3];
        result.percentuale = tokens[4];
        return result;
    }
    private Comune produceComuni(String str){
        String[] tokens = str.split(";");
        // System.out.println(""+Arrays.toString(tokens));
        Comune result = new Comune();
        result.codice = tokens[0];
        result.nome = tokens[1];
        result.spesaTotale = tokens[2];
        result.spesaProcapite = tokens[3];
        return result;
    }

    public class Data{String codice;}
    public class Comune extends Data{
        String nome,
                spesaTotale,
                spesaProcapite;
    }
    public class Siope extends Data{
        String descrizione,
                pagamento,
                procapite,
                percentuale;
    }
}
