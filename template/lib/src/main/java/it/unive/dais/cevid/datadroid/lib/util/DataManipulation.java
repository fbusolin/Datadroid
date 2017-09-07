package it.unive.dais.cevid.datadroid.lib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DataManipulation {

    public static <T> void filter(List<T> l, Function<T, Boolean> f) {
        Collection<T> c = new ArrayList<>();
        for (T x : l) {
            if (f.eval(x)) c.add(x);
        }
        l.retainAll(c);
    }

    public static <T> double sumBy(List<T> l, Function<T, Double> f) {
        double r = 0.;
        for (T x : l) {
            r += f.eval(x);
        }
        return r;
    }

    public static <T> void filterByCode(List<T> l, final int code, final Function<T, Integer> getCode) {
        filter(l, new Function<T, Boolean>() {
            @Override
            public Boolean eval(T x) {
                return getCode.eval(x) == code;
            }
        });
    }


    public static <T> void filterByWords(List<T> l, final Collection<CharSequence> ss, final Function<T, String> getText) {
        filter(l, new Function<T, Boolean>() {
            @Override
            public Boolean eval(T x) {
                final String s0 = getText.eval(x);
                for (CharSequence s : ss) {
                    if (s0.contains(s)) return true;
                }
                return false;
            }
        });
    }

    public static <T> void filterByWords(List<T> l, CharSequence[] ss, Function<T, String> getText) {
        filterByWords(l, Arrays.asList(ss), getText);
    }

//    public static void prova() {
//        List<SoldiPubbliciParser.Data> l = new ArrayList<>();
//
//        filterByWords(l, new String[]{"pizza", "fichi"}, new Function<SoldiPubbliciParser.Data, String>() {
//            @Override
//            public String eval(SoldiPubbliciParser.Data x) {
//                return x.descrizione_ente;
//            }
//        });
//
//        filterByWords(l, new String[]{"pizza", "fichi"}, new Function<SoldiPubbliciParser.Data, String>() {
//            @Override
//            public String eval(SoldiPubbliciParser.Data x) {
//                return x.descrizione_codice;
//            }
//        });
//    }
}
