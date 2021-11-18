package org.unibl.etf.pj2.projekat.simulacija;


import org.unibl.etf.pj2.projekat.gradjevine.Ambulanta;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Nadzirac implements Serializable
{
    public static ConcurrentLinkedDeque<Alarm> alarmi = new ConcurrentLinkedDeque<>();


    public static void dodaj(Alarm a)
    {
        alarmi.push(a);
    }

    public static void obradi() { // obrada alarma nakon pristizanja

        int dostupno = Grad.brojAmbVozila;
        while (!alarmi.isEmpty() && dostupno>0) {
            Alarm a = alarmi.pop();
            dostupno--;
            int alarmId = a.getIdKuce();
            Grad.sviStanovnici.forEach(e ->  // ili sve kuce
            {
                if (e.getIdKuce() == alarmId)
                {
                    int bzu = e.getBrojZarazenihUkucana();
                    e.setBrojZarazenihUkucana(++bzu);
                }
            });
            for(Ambulanta am : Grad.sveAmbulante)
                if(am.premjesti(a.getPozicija(), a.getS()))
                {
                    break;
                }
        }
    }

}
