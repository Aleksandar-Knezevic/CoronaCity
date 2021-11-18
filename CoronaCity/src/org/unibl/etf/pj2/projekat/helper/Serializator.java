package org.unibl.etf.pj2.projekat.helper;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import org.unibl.etf.pj2.projekat.gradjevine.Ambulanta;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.main.MainController;
import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.simulacija.Nadzirac;

import java.io.*;
import java.util.logging.Level;

public class Serializator
{
    private static Serializator serializator=new Serializator();
    private MainController mainController;
    public static final String serFile = "grad.ser";

    private Serializator(){}

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public static Serializator getInstance()
    {
        return serializator;
    }

    public void serialize() // zaustavljanje grada i serijalizacija
    {
        Grad.allowed=false;
        try
        {
            Thread.sleep(1000);
        }
        catch(Exception e)
        {
            MyLogger.log(Level.INFO, "Greska pri sleepu", e);
        }
        Grad.krajSimulacije=System.currentTimeMillis();
        Grad.razlika=Grad.krajSimulacije - Grad.pocetakSimulacije;

        try(ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(new File("." + File.separator + serFile ))))
        {
            SerializableGrad sg = new SerializableGrad();
            ous.writeObject(sg);

        }
        catch(Exception e)
        {
            MyLogger.log(Level.WARNING, "Greska pri serijalizaciji", e);
        }
        System.exit(0);


    }

    public void deserialize() // deserijalizacija grada i ponovno pokretanje
    {
        SerializableGrad sg=null;
        File f = new File("." + File.separator + serFile);
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f)))
        {
            sg = (SerializableGrad) ois.readObject();

        }
        catch(Exception e)
        {
            MyLogger.log(Level.WARNING, "Greska pri deserijalizaciji", e);
        }

        Grad.MAT_DIM=sg.MAT_DIM;
        Grad.grad=sg.grad;
//        Grad.guiMapa=sg.guiMapa;
        Grad.sviStanovnici=sg.sviStanovnici;
        Grad.sveAmbulante=sg.sveAmbulante;
        Grad.sveKuce=sg.sveKuce;
        Grad.sviZarazeni=sg.sviZarazeni;
        Grad.sviPunktovi=sg.sviPunktovi;
        Grad.brojAmbVozila=sg.brojAmbVozila;
        Grad.razlika=sg.razlika;
        Grad.inicijalizuj2();

        Grad.sveKuce.forEach(e ->
        {
            e.setHouseBackground(new Background(new BackgroundFill(Colors.colors.get(e.getIdKuce()), null, null)));
            Label l = Grad.guiMapa.get(e.getPozicija());
            l.setBackground(e.getHouseBackground());
            l.setText("K");
            l.setAlignment(Pos.CENTER);
            l.setStyle("-fx-font-weight: bold;");
        });


        Grad.sviStanovnici.forEach(e ->
        {
//            e.setMainController(mainController);
            e.setColor(Colors.colors.get(e.getIdKuce()));
            e.setHouseBackground(new Background(new BackgroundFill(Colors.colors.get(e.getIdKuce()),null,null)));
        });

        Grad.sviPunktovi.forEach(e ->
        {
            Grad.guiMapa.get(e.getPozicija()).setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            e.start();
        });


        Grad.sveAmbulante.forEach(e ->
        {
            Grad.guiMapa.get(e.getPozicija()).setBackground(new Background(new BackgroundFill(Color.RED, null,null)));
            e.start();

        });
        Ambulanta.ukupnoZarazenih=sg.ukupnoZarazenih;
        Ambulanta.ukupnoOzdravljelih=sg.ukupnoOzdravljelih;
        Nadzirac.alarmi=sg.alarmi;




        new Refresher().start();
        new TemperatureChanger().start();
        Grad.sviStanovnici.forEach(Thread::start);
        Grad.pocetakSimulacije=System.currentTimeMillis();
    }
}
