package org.unibl.etf.pj2.projekat.helper;

import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.simulacija.Grad;

import java.util.Random;
import java.util.logging.Level;

public class TemperatureChanger extends Thread
{

    public static final Object temperatureObject = new Object();
    private Random chance = new Random();
    private Random temperatura = new Random();
    public TemperatureChanger()
    {
        super();
    }

    @Override
    public void run() // mijenja temperature svim stanovnicima
    {
        while(Grad.allowed)
        {
            try {

                sleep(30000);
                synchronized (temperatureObject)
                {
                Grad.sviStanovnici.parallelStream().forEach(e ->
                {
                    if (chance.nextInt(100) > 30) // vjerovatnoca 70% da je temperature izmedju 36 i 37
                        e.setTemperatura((temperatura.nextInt(10) + 360) / 10.0);
                    else
                        e.setTemperatura((temperatura.nextInt(40) + 370) / 10.0); // vjerovatnoca 30% da je
                });                                                                     // temperatura izmedju 37 i 40
            }
            }
            catch(Exception e)
            {
                MyLogger.log(Level.WARNING, "Greska pri promjeni temperature", e);
            }
        }
    }


}
