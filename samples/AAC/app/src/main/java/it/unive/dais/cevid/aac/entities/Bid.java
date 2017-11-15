package it.unive.dais.cevid.aac.entities;

import it.unive.dais.cevid.aac.util.BandiParser;

/**
 * Created by admin on 15/11/17.
 */

public class Bid {
    String idLotto;
    String percent_usato;
    String massimale;
    public Bid(BandiParser.Data data){
        this.idLotto = data.getId_lotto();
        this.percent_usato = data.getPer_erosione();
        this.massimale = data.getQnt_massimale();
    }
}
