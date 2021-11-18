package org.unibl.etf.pj2.projekat.stanovnici;

import javafx.application.Platform;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.main.MainController;
import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.simulacija.IntPair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;

public abstract class Stanovnik extends Thread implements Serializable
{
    protected int idStanovnika;
    protected String ime;
    protected String prezime;
    protected int godinaRodjenja;
    protected Pol pol;
    protected int idKuce;
    protected double temperatura;
    protected int godine;
    protected ArrayList<Double> temperature;


    protected Smjer smjer;
    protected IntPair pozicija;
    protected IntPair novaPozicija;


    protected IntPair pocetnaPozicija;
    protected IntPair pozicijaKuce;


    protected transient Color color;
    private static Random random = new Random();


    protected int istokDozvoljeno;
    protected int zapadDozvoljeno;
    protected int sjeverDozvoljeno;
    protected int jugDozvoljeno;


    protected volatile boolean zarazen;
    protected volatile int brojZarazenihUkucana;
    private boolean rez;

    private transient MainController mainController;



    public static transient final Background DEFAULT_BACKGROUND = new Background(new BackgroundFill(Color.LAVENDER, null, null));
    private transient Background houseBackground;


    public Stanovnik()
    {
        super();
    }

    public Stanovnik(int idStanovnika, String ime, int godine) // kreiranje stanovnika
    {
        this.idStanovnika=idStanovnika;
        this.ime=ime;
        this.godine=godine;
        temperature=new ArrayList<>();
        temperatura=36;
        temperature.add(temperatura);
        if(random.nextBoolean())
            pol=Pol.MUSKO;
        else
            pol=Pol.ZENSKO;
        brojZarazenihUkucana=0;
        godinaRodjenja= Calendar.YEAR-godine;

    }

    public int getBrojZarazenihUkucana() {
        return brojZarazenihUkucana;
    }

    public void setBrojZarazenihUkucana(int brojZarazenihUkucana) {
        this.brojZarazenihUkucana = brojZarazenihUkucana;
    }

    public Background getHouseBackground() {
        return houseBackground;
    }

    public void setHouseBackground(Background houseBackground) {
        this.houseBackground = houseBackground;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public IntPair getPozicijaKuce() {
        return pozicijaKuce;
    }

    public void setPozicijaKuce(IntPair pozicijaKuce) {
        this.pozicijaKuce = pozicijaKuce;
    }

    public IntPair getPozicija() {
        return pozicija;
    }

    public ArrayList<Double> getTemperature() {
        return temperature;
    }

    public void setPozicija(IntPair pozicija) { // dozvole se inicijalizuju nakon postavljanja pozicije
        this.pozicija = pozicija;
        pocetnaPozicija = pozicija;
        inicijalizujDozvole();
    }


    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) { // pri promjeni temperature, nova temperatura se dodaje u listu
        this.temperatura = temperatura;
        temperature.add(temperatura);
    }

    public int getIdStanovnika() {
        return idStanovnika;
    }

    public void setIdStanovnika(int idStanovnika) {
        this.idStanovnika = idStanovnika;
    }

    public String getIme() {
        return ime;
    }

    public Pol getPol() {
        return pol;
    }

    public int getIdKuce() {
        return idKuce;
    }

    public void setIdKuce(int idKuce) {
        this.idKuce = idKuce;
    }

    public boolean isZarazen() {
        return zarazen;
    }

    public void setZarazen(boolean zarazen) {
        this.zarazen = zarazen;
    }

    public int getGodine() {
        return godine;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    @Override
    public String toString()
    {
        return ime + "#" + idStanovnika + "#" + smjer + "#" + pozicija + '\n';
    }

    @Override
    public void run()
    {
//        try {
//            sleep(3000);
//        } catch (Exception e) {
//            MyLogger.log(Level.INFO, "Greska pri sleepu", e);
//        }
        while(Grad.allowed)
        {
            synchronized (Grad.syncObject)
            {
                try {
                    if (!zarazen && brojZarazenihUkucana==0) { // provjera da li ima dozvolu kretanja
                        smjer = Smjer.values()[new Random().nextInt(4)];
                        IntPair staraPozicija = pozicija;
                        novaPozicija = pomjeri(smjer);
                        if (novaPozicija.equals(pozicija))
                            continue;
//
                        rez= checkFields(novaPozicija);
                        if (rez) {
                            Grad.grad.get(staraPozicija).ukloniStanovnika(this);
                            Grad.grad.get(novaPozicija).dodajStanovnika(this);
                            pozicija = novaPozicija;
                            Platform.runLater(() ->
                            {
                                mainController.textArea.appendText(this.toString());
                            });


                        }

                    }
                } catch (Exception e) {
                    MyLogger.log(Level.INFO, "Pokusaj izlaska sa mape", e);

                }
            }

            if (brojZarazenihUkucana>0 && !zarazen) // povratak stanovnika kuci u slucaju da je ukucanin zarazen
            {
                if(pozicija.getVrsta() != pozicijaKuce.getVrsta() || pozicija.getKolona() != pozicijaKuce.getKolona()) {
                    IntPair staraPozicija = pozicija;
                    if (pozicija.getVrsta() > pozicijaKuce.getVrsta())
                        pozicija = new IntPair(pozicija.getVrsta() - 1, pozicija.getKolona());
                    else if (pozicija.getVrsta() < pozicijaKuce.getVrsta())
                        pozicija = new IntPair(pozicija.getVrsta() + 1, pozicija.getKolona());
                    else if (pozicija.getKolona() > pozicijaKuce.getKolona())
                        pozicija = new IntPair(pozicija.getVrsta(), pozicija.getKolona() - 1);
                    else if (pozicija.getKolona() < pozicijaKuce.getKolona())
                        pozicija = new IntPair(pozicija.getVrsta(), pozicija.getKolona() + 1);
                    Grad.grad.get(staraPozicija).ukloniStanovnika(this);
                    Grad.grad.get(pozicija).dodajStanovnika(this);
                    synchronized (Grad.syncObject)
                    {
                    Platform.runLater(() ->
                    {
                        mainController.textArea.appendText(this.toString());
                    });
                }
            try {
                sleep(random.nextInt(500) + 300);
                continue;
            }
            catch(Exception e)
            {
                MyLogger.log(Level.INFO, "Greska pri sleepu", e);
            }
                }
            }

            try {
                if(rez) {
                    sleep(random.nextInt(500) + 1000);
                }
                else
                    sleep(100);
            } catch (Exception e) {
                MyLogger.log(Level.INFO, "Greska pri sleepu", e);
            }
        }
    }




    private IntPair pomjeri(Smjer smjer) // odabir sljedece pozicije
    {
        if(smjer==Smjer.ISTOK && pozicija.getKolona()+1 <= pocetnaPozicija.getKolona()+istokDozvoljeno)
            return new IntPair(pozicija.getVrsta(), pozicija.getKolona()+1);
        else if(smjer==Smjer.JUG && pozicija.getVrsta()+1 <=pocetnaPozicija.getVrsta()+jugDozvoljeno)
            return new IntPair(pozicija.getVrsta()+1, pozicija.getKolona());
        else if(smjer==Smjer.SJEVER && pozicija.getVrsta()-1 >= pocetnaPozicija.getVrsta()-sjeverDozvoljeno)
            return new IntPair(pozicija.getVrsta()-1, pozicija.getKolona());
        else if(smjer==Smjer.ZAPAD && pozicija.getKolona()-1 >= pocetnaPozicija.getKolona()-zapadDozvoljeno)
            return new IntPair(pozicija.getVrsta(), pozicija.getKolona()-1);
        else
            return pozicija;

    }

    public abstract boolean checkFields(IntPair pozicija);



    public abstract void inicijalizujDozvole();



}
