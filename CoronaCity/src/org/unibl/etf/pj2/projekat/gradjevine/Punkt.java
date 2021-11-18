package org.unibl.etf.pj2.projekat.gradjevine;


import javafx.application.Platform;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.main.MainController;
import org.unibl.etf.pj2.projekat.simulacija.Alarm;
import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.simulacija.IntPair;
import org.unibl.etf.pj2.projekat.simulacija.Nadzirac;
import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

public class Punkt extends Thread implements Serializable
{
    IntPair pozicija;
    public static final Object punktSyncObject = new Object();
    private transient MainController mainController;
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public Punkt()
    {
        super();
    }

    public Punkt(IntPair pozicija) // kreiranje punkta
    {
        this.pozicija=pozicija;
        Grad.guiMapa.get(pozicija).setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

    }

    public IntPair getPozicija() {
        return pozicija;
    }


    @Override
    public void run() // punkt provjera polja, i u slucaju zarazenog stanovnika, prekida mu kretanje i kreira alarm
    {
        while(Grad.allowed)
        {
            try
            {
                for(int i=pozicija.getVrsta()-1;i<=pozicija.getVrsta()+1;i++)
                {
                    for(int j=pozicija.getKolona()-1;j<=pozicija.getKolona()+1;j++) {
                        IntPair ip = new IntPair(i, j);
                        if (Grad.grad.get(ip).getKuca() == null)
                        {
                            CopyOnWriteArrayList<Stanovnik> stanovnici = Grad.grad.get(ip).getStanovnici();
                        stanovnici.forEach(e ->
                        {
                            synchronized (punktSyncObject) {
                                if (e.getTemperatura() > 37 && !e.isZarazen()) // srediti premjestanje u ambulantu
                                {
                                    e.setZarazen(true);
                                    Grad.sviZarazeni.add(e);
                                    Nadzirac.dodaj(new Alarm(e.getPozicija(), e.getIdKuce(), e));
                                    if (!mainController.alertCircle.isVisible())
                                        Platform.runLater(() ->
                                        {
                                            mainController.alertCircle.setVisible(true);
                                        });
                                }
                            }
                        });
                    }
                    }
                }
                sleep(300);

            }
            catch(Exception e)
            {
                MyLogger.log(Level.WARNING, "Greska u punktu", e);
            }
        }
    }
}
