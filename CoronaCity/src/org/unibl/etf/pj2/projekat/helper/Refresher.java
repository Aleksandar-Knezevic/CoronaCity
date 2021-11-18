package org.unibl.etf.pj2.projekat.helper;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.simulacija.IntPair;
import org.unibl.etf.pj2.projekat.simulacija.Polje;
import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;

import java.io.ObjectInputStream;
import java.util.logging.Level;


public class Refresher extends Thread
{
    public static final Object refreshObject = new Object();
    public Refresher()
    {
        super();
    }

    @Override
    public void run() // apdejtuje GUI dok se stanovnici pomjeraju
    {
        while(Grad.allowed)
        {
            try {
//
                Grad.grad.values().parallelStream().forEach(e ->
                {
//                    synchronized (refreshObject)
//                    {
                    if (e.getKuca() == null && e.getPunkt() == null && e.getAmbulanta() == null) {
                        IntPair ip = keyFromValue(e);
                        Label l = Grad.guiMapa.get(ip);
                        if (e.getStanovnici().isEmpty()) {

                            Platform.runLater(() ->
                            {
                                l.setBackground(Stanovnik.DEFAULT_BACKGROUND);
                                l.setText("");
                            });

                        } else {
                            Stanovnik s = Grad.grad.get(ip).getStanovnici().get(0);

                            Platform.runLater(() ->
                            {
                                l.setBackground(s.getHouseBackground());
                                l.setText(s.getIme());
                            });
                        }
                    }
                    else if(e.getAmbulanta()!=null)
                    {
                        IntPair ip = keyFromValue(e);
                        Label l = Grad.guiMapa.get(ip);
                        Platform.runLater(() ->
                        {
                            l.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
                        });
                    }
//                }
                });
                sleep(100);
            }
            catch(Exception e)
            {
                MyLogger.log(Level.WARNING, "Greska pri osvjezavanju mape", e);
            }
        }
    }

    public IntPair keyFromValue(Polje polje) // metoda koja dohvata kljuc na osnovu vrijednost iz HashMap-e
    {

        for(IntPair ip : Grad.grad.keySet())
        {
            if(Grad.grad.get(ip).equals(polje))
                return ip;
        }

        return new IntPair(0, 0);
    }
}
