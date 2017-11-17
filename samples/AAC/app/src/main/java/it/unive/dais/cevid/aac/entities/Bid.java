package it.unive.dais.cevid.aac.entities;

import java.io.Serializable;

import it.unive.dais.cevid.aac.util.BandiParser;

/**
 * Created by fbusolin on 15/11/17.
 */

public class Bid implements Serializable{
    private String idLotto;
    private String percent_usato;
    private String massimale;
    private String descrizione;
    private String title;
    public Bid(BandiParser.Data data){
        this.idLotto = data.getId_lotto();
        this.percent_usato = data.getPer_erosione();
        this.massimale = data.getImporto_massimale();
        this.descrizione = data.getCat_merceologica();
        this.title = data.getDenominazione();
    }

    public String getIdLotto() {
        return idLotto;
    }

    public String getPercent_usato() {
        return percent_usato;
    }

    public String getMassimale() {
        return massimale;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getTitle() {
        return title;
    }
}
