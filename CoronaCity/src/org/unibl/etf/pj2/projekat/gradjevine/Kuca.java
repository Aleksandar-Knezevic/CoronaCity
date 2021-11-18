package org.unibl.etf.pj2.projekat.gradjevine;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.simulacija.IntPair;
import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

public class Kuca implements Serializable
{
    private int idKuce;
    private CopyOnWriteArrayList<Stanovnik> ukucani;
    private IntPair pozicija;

    public IntPair getPozicija() {
        return pozicija;
    }

    public Background getHouseBackground() {
        return houseBackground;
    }

    public void setHouseBackground(Background houseBackground) {
        this.houseBackground = houseBackground;
    }

    private transient Background houseBackground;


    public Kuca(int idKuce, Color houseColor, IntPair pozicija) // kreiranje kuce
    {
        this.idKuce=idKuce;
        this.pozicija=pozicija;
        //Grad.grad.get(pozicija)
        houseBackground=new Background(new BackgroundFill(houseColor, null, null));
        Label l = Grad.guiMapa.get(pozicija);
        l.setBackground(houseBackground);
        l.setText("K");
        l.setAlignment(Pos.CENTER);
        l.setStyle("-fx-font-weight: bold;");
        ukucani=new CopyOnWriteArrayList<>();
    }

    public void pokreniUkucane()
    {
        try {
            ukucani.forEach(Thread::start);
        }
        catch (Exception e)
        {
            MyLogger.log(Level.WARNING, "Ukucani se vec krecu", e);
        }
    }

    public void inicijalizujUkucane() // pokretanje ukucana
    {
        ukucani.forEach(e ->
        {
            e.setIdKuce(idKuce);
            e.setHouseBackground(houseBackground);
            e.setPozicija(pozicija);
            e.setPozicijaKuce(pozicija);
            e.setPrezime("Prezime" + idKuce);
        });
    }

    public void dodajUkucana(Stanovnik st)
    {
        ukucani.add(st);
    }

    public int getIdKuce() {
        return idKuce;
    }
}
