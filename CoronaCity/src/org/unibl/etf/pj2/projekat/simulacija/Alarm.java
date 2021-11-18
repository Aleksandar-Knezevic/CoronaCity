package org.unibl.etf.pj2.projekat.simulacija;

import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;

import java.io.Serializable;

public class Alarm implements Serializable
{
    private IntPair pozicija;
    private int idKuce;
    private Stanovnik s;

    public IntPair getPozicija() {
        return pozicija;
    }

    public int getIdKuce() {
        return idKuce;
    }

    public Stanovnik getS() {
        return s;
    }

    public void setS(Stanovnik s) {
        this.s = s;
    }

    public Alarm()
    {
        super();
    }

    public Alarm(IntPair pozicija, int idKuce, Stanovnik s) // kreiranje alarm
    {
        this.pozicija=pozicija;
        this.idKuce=idKuce;
        this.s=s;
    }
}
