package it.unive.dais.cevid.aac.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
;import it.unive.dais.cevid.aac.fragment.Anno2013Fragment;
import it.unive.dais.cevid.aac.fragment.Anno2014Fragment;
import it.unive.dais.cevid.aac.fragment.Anno2015Fragment;
import it.unive.dais.cevid.aac.fragment.Anno2016Fragment;
import it.unive.dais.cevid.aac.fragment.Anno2017Fragment;

/**
 * Created by gianmarcocallegher on 15/11/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new Anno2017Fragment();
            case 1:
                return new Anno2016Fragment();
            case 2:
                return new Anno2015Fragment();
            case 3:
                return new Anno2014Fragment();
            case 4:
                return new Anno2013Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}