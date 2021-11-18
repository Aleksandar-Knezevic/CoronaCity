package org.unibl.etf.pj2.projekat.simulacija;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import org.unibl.etf.pj2.projekat.gradjevine.Ambulanta;
import org.unibl.etf.pj2.projekat.gradjevine.Kuca;
import org.unibl.etf.pj2.projekat.gradjevine.Punkt;
import org.unibl.etf.pj2.projekat.helper.*;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.stanovnici.Dijete;
import org.unibl.etf.pj2.projekat.stanovnici.Odrasli;
import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;
import org.unibl.etf.pj2.projekat.stanovnici.Stari;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Grad
{
    private static Random rand = new Random();
    public static int MAT_DIM=rand.nextInt(16)+15;
    public static ConcurrentHashMap<IntPair, Polje> grad = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<IntPair, Label> guiMapa = new ConcurrentHashMap<>();
    public static CopyOnWriteArrayList<Stanovnik> sviStanovnici = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Ambulanta> sveAmbulante = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Stanovnik> sviZarazeni = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Punkt> sviPunktovi = new CopyOnWriteArrayList<>();
    public static List<Kuca> sveKuce = new ArrayList<>();
    public static int brojAmbVozila;
    public static final Object syncObject=new Object();
    public static volatile boolean allowed=true;
    public static long pocetakSimulacije;
    public static long krajSimulacije;
    public static long razlika;


    public static void inicijalizuj() // inicijalizacija HashMape za grad i GUI
    {
        int labelSize = 600/MAT_DIM;
        for (int i = -1; i < Grad.MAT_DIM+1; i++) {
            for (int j = -1; j < Grad.MAT_DIM + 1; j++) {
                grad.put(new IntPair(i, j), new Polje());
                Label label = new Label();
                label.setPrefWidth(labelSize);
                label.setPrefHeight(labelSize);
                label.setMaxHeight(labelSize);
                label.setMaxWidth(labelSize);
                label.setBackground(Stanovnik.DEFAULT_BACKGROUND);
                label.setAlignment(Pos.CENTER);
                Grad.guiMapa.put(new IntPair(i, j), label);
            }
        }
    }

    public static void inicijalizuj2() // ponovna inicijalizacija, samo za GUI, nakon deserijalizacije
    {
        int labelSize = 600/MAT_DIM;
        for (int i = -1; i < Grad.MAT_DIM+1; i++) {
            for (int j = -1; j < Grad.MAT_DIM + 1; j++) {
                Label label = new Label();
                label.setPrefWidth(labelSize);
                label.setPrefHeight(labelSize);
                label.setMaxHeight(labelSize);
                label.setMaxWidth(labelSize);
                label.setBackground(Stanovnik.DEFAULT_BACKGROUND);
                label.setAlignment(Pos.CENTER);
                Grad.guiMapa.put(new IntPair(i, j), label);
            }
        }

    }



    public Grad() { super(); }

    public static void inicijalizujGrad(int djeca, int odrasli, int stari, int kuce, int punktovi, int ambVozila) // kreiranje svih elementara na pocetku simulacije
    {
        if(kuce > Colors.colors.size())
        {
            MyLogger.log(Level.SEVERE, "Nema dovoljno boja", new Exception());
            System.exit(0);
        }
        if((kuce + punktovi) > Grad.MAT_DIM*Grad.MAT_DIM)
        {
            MyLogger.log(Level.SEVERE, "Nema dovoljno mjesta", new Exception());
            System.exit(0);
        }
        brojAmbVozila=ambVozila;
        Random rand = new Random();
        int i=0;

            while (i < kuce) {
                int vrsta = rand.nextInt(MAT_DIM - 2) + 1;
                int kolona = rand.nextInt(MAT_DIM - 2) + 1;
                IntPair ip = new IntPair(vrsta, kolona);
                Polje p = grad.get(ip);
                if (p.getKuca() == null) {
                    Kuca kuca = new Kuca(kuce, Colors.colors.get(kuce), ip);
                    p.setKuca(kuca);
                    sveKuce.add(kuca);
                    kuce--;
                }

            }


        while(i<punktovi)
        {
            int vrsta = rand.nextInt(MAT_DIM-2)+1;
            int kolona = rand.nextInt(MAT_DIM-2)+1;
            IntPair ip = new IntPair(vrsta, kolona);
            Polje p = grad.get(ip);
            if(p.getKuca()==null && p.getPunkt()==null)
            {
                Punkt punkt = new Punkt(ip);
                p.setPunkt(punkt);
                punktovi--;
                sviPunktovi.add(punkt);
                punkt.start();
            }
        }


        while(i<odrasli)
        {
            Odrasli od = new Odrasli(i, "O" + i, rand.nextInt(47)+19);
            sviStanovnici.add(od);
            i++;
        }

        i=0;
        while(i<stari)
        {
            Stari st = new Stari(odrasli + i, "S" + odrasli + i, rand.nextInt(34)+66);
            sviStanovnici.add(st);
            i++;
        }


        i=0;
        while(i<djeca)
        {
            Dijete d = new Dijete(odrasli + stari + i, "D" + odrasli + stari + i, rand.nextInt(18)+1);
            sviStanovnici.add(d);
            i++;
        }

        int size;
        if(sveKuce.size()>(odrasli+stari))
            size = odrasli+stari;
        else
            size = sveKuce.size();

        for(int j=0;j<sviStanovnici.size();j++)
            sveKuce.get(j%size).dodajUkucana(sviStanovnici.get(j));


        Ambulanta a1 = new Ambulanta(new IntPair(0,0));
        Ambulanta a2 = new Ambulanta(new IntPair(0, MAT_DIM-1));
        Ambulanta a3 = new Ambulanta(new IntPair(MAT_DIM-1, 0));
        Ambulanta a4 = new Ambulanta(new IntPair(MAT_DIM-1, MAT_DIM-1));
        sveAmbulante.add(a1);
        sveAmbulante.add(a2);
        sveAmbulante.add(a3);
        sveAmbulante.add(a4);
        Grad.grad.get(new IntPair(0, 0)).setAmbulanta(a1);
        Grad.grad.get(new IntPair(0, MAT_DIM-1)).setAmbulanta(a2);
        Grad.grad.get(new IntPair(MAT_DIM-1, 0)).setAmbulanta(a3);
        Grad.grad.get(new IntPair(MAT_DIM-1, MAT_DIM-1)).setAmbulanta(a4);
        a1.start();
        a2.start();
        a3.start();
        a4.start();


        Refresher c = new Refresher();
        c.start();


        TemperatureChanger tc = new TemperatureChanger();
        tc.start();
    }
}
