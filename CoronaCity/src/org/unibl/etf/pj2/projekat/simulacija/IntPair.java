package org.unibl.etf.pj2.projekat.simulacija;

import java.io.Serializable;

public class IntPair implements Serializable // klasa za odrzavanje koordinata
{
    private int vrsta;
    private int kolona;

    public IntPair()
    {
        super();
    }

    public IntPair(int vrsta, int kolona)
    {
        this.vrsta = vrsta;
        this.kolona = kolona;
    }

    public int getVrsta() {
        return vrsta;
    }

    public int getKolona() {
        return kolona;
    }

    @Override
    public String toString()
    {
        return "[" + vrsta + "][" + kolona + "]";
    }

    @Override
    public boolean equals(Object o)
    {
        if(o==null)
            return false;
        if(getClass()!=o.getClass())
            return false;
        IntPair ip = (IntPair) o;
        if(vrsta ==ip.vrsta && kolona ==ip.kolona)
            return true;
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31 * hash + vrsta;
        hash = 31 * hash + kolona;
        return hash;
    }
}
