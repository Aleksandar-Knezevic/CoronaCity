package org.unibl.etf.pj2.projekat.simulacija;

import org.unibl.etf.pj2.projekat.gradjevine.Ambulanta;
import org.unibl.etf.pj2.projekat.gradjevine.Kuca;
import org.unibl.etf.pj2.projekat.gradjevine.Punkt;
import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class Polje implements Serializable
{
    Kuca kuca;
    CopyOnWriteArrayList<Stanovnik> stanovnici; //
    Punkt punkt;
    Ambulanta ambulanta;
    public Polje()
    {
        stanovnici=new CopyOnWriteArrayList<>();
    }

    public CopyOnWriteArrayList<Stanovnik> getStanovnici() {
        return stanovnici;
    }

    public Ambulanta getAmbulanta() {
        return ambulanta;
    }

    public void setAmbulanta(Ambulanta ambulanta) {
        this.ambulanta = ambulanta;
    }

    public void ukloniStanovnika(Stanovnik st)
    {
        stanovnici.remove(st);
    }

    public void dodajStanovnika(Stanovnik st)
    {
        stanovnici.add(st);
    }

    public Kuca getKuca() {
        return kuca;
    }

    public void setKuca(Kuca kuca) {
        this.kuca = kuca;
    }

    public Punkt getPunkt() {
        return punkt;
    }

    public void setPunkt(Punkt punkt) {
        this.punkt = punkt;
    }
}
