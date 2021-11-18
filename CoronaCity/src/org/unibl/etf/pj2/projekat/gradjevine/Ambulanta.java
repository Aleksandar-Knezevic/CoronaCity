package org.unibl.etf.pj2.projekat.gradjevine;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import org.unibl.etf.pj2.projekat.helper.TemperatureChanger;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.simulacija.IntPair;
import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

public class Ambulanta extends Thread implements Serializable
{
    private int kapacitet;
    private CopyOnWriteArrayList<Stanovnik> zarazeni;
    private IntPair pozicija;

    public IntPair getPozicija() {
        return pozicija;
    }

    public void setPozicija(IntPair pozicija) {
        this.pozicija = pozicija;
    }

    private Random rand = new Random();
    public static int ukupnoZarazenih=0;
    public static int ukupnoOzdravljelih=0;
    public static final Object counterSyncObject = new Object();
    public static final String infoFileName="info.txt";
    public static final String infoDirectory = "info";

    public Ambulanta()
    {
        super();
    }

    public Ambulanta(IntPair pozicija) // kreiranje ambulante i postavljanje kapaciteta
    {
        this.pozicija=pozicija;
        zarazeni=new CopyOnWriteArrayList<>();
        kapacitet= (int) (rand.nextInt((int) (0.05*Grad.sviStanovnici.size() + 1)) + 1 + 0.1* Grad.sviStanovnici.size());
        Grad.guiMapa.get(pozicija).setBackground(new Background(new BackgroundFill(Color.RED, null,null)));
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public CopyOnWriteArrayList<Stanovnik> getZarazeni() {
        return zarazeni;
    }


    public boolean premjesti(IntPair lokacija, Stanovnik s) { // premjestanje zarazenog u ambulantu
        synchronized (counterSyncObject) {
            if (zarazeni.size() >= kapacitet)
                return false;
            ukupnoZarazenih++;
            azurirajFajl();

            Grad.grad.get(lokacija).getStanovnici().remove(s);
            zarazeni.add(s);

            return true;
        }
    }

    public void ukloni(Stanovnik st) // uklanjanje oporavljene osobe iz ambulante i vracanje kuci
    {
        synchronized (counterSyncObject) {
            zarazeni.remove(st);
            st.setPozicija(st.getPozicijaKuce());
            Grad.grad.get(st.getPozicijaKuce()).dodajStanovnika(st);
            st.setZarazen(false);

            ukupnoZarazenih--;
            ukupnoOzdravljelih++;
            azurirajFajl();

            Grad.sviStanovnici.forEach(e ->
            {
                if (e.getIdKuce() == st.getIdKuce()) {
                    int bzu = e.getBrojZarazenihUkucana();
                    e.setBrojZarazenihUkucana(--bzu);
                }
            });
        }
    }

    public void run() // ambulanta na svakih 7-10 sekundi mjeri temperature stanovnicima
    {
        while (Grad.allowed)
        {
            try {
                sleep(7000 + rand.nextInt(3000));
                synchronized (TemperatureChanger.temperatureObject)
                {
                zarazeni.forEach(e ->
                {
                    double sum = 0;
                    double as;
                    ArrayList<Double> temps = e.getTemperature();
                    if (temps.size() > 3) {
                        List<Double> l3t = temps.subList(temps.size() - 3, temps.size());
                        for (Double d : l3t)
                            sum += d;
                        as = sum / 3.0;
                        if (as < 37)
                            ukloni(e);
                    }
                });
            }

            }
            catch(Exception e)
            {
                MyLogger.log(Level.WARNING, "Greska u ambulanti", e);
            }
        }
    }

    private void azurirajFajl() // azuriranje fajla sa brojem zarazenih i oporavljenih stanovnika
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("." + File.separator + infoDirectory + File.separator + infoFileName))))
        {
            bw.write("Zarazeni: " + ukupnoZarazenih + '\n' + "Oporavljeni: " + ukupnoOzdravljelih);
        }
        catch (Exception e)
        {
            MyLogger.log(Level.WARNING, "Greska pri azuriranju", e);
        }

    }
}
