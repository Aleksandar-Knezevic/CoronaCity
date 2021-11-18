package org.unibl.etf.pj2.projekat.helper;


import org.unibl.etf.pj2.projekat.gradjevine.Ambulanta;
import org.unibl.etf.pj2.projekat.gradjevine.Kuca;
import org.unibl.etf.pj2.projekat.gradjevine.Punkt;
import org.unibl.etf.pj2.projekat.simulacija.*;
import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class SerializableGrad implements Serializable // klasa koja sluzi za serijalizaciju grada
{
    public int MAT_DIM;
    public ConcurrentHashMap<IntPair, Polje> grad;
    public CopyOnWriteArrayList<Stanovnik> sviStanovnici;
    public CopyOnWriteArrayList<Ambulanta> sveAmbulante;
    public CopyOnWriteArrayList<Stanovnik> sviZarazeni;
    public CopyOnWriteArrayList<Punkt> sviPunktovi;
    public List<Kuca> sveKuce;
    public int brojAmbVozila;
    public int ukupnoZarazenih;
    public int ukupnoOzdravljelih;
    public ConcurrentLinkedDeque<Alarm> alarmi;
    public long razlika;



    public SerializableGrad()
    {
        MAT_DIM= Grad.MAT_DIM;
        grad=Grad.grad;
        sviStanovnici=Grad.sviStanovnici;
        sveAmbulante=Grad.sveAmbulante;
        sviZarazeni=Grad.sviZarazeni;
        sviPunktovi=Grad.sviPunktovi;
        sveKuce=Grad.sveKuce;
        brojAmbVozila=Grad.brojAmbVozila;
        ukupnoZarazenih=Ambulanta.ukupnoZarazenih;
        ukupnoOzdravljelih=Ambulanta.ukupnoOzdravljelih;
        alarmi = Nadzirac.alarmi;
        razlika = Grad.razlika;
    }

}
