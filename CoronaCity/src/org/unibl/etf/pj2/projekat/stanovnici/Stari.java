package org.unibl.etf.pj2.projekat.stanovnici;

import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.simulacija.IntPair;

import java.util.concurrent.CopyOnWriteArrayList;

public class Stari extends Stanovnik
{
    public static final int STARI_DOZVOLA=3;
    public Stari()
    {
        super();
    }

    public Stari(int idStanovnika, String ime, int godine)
    {
        super(idStanovnika, ime, godine);
    }

    @Override
    public boolean checkFields(IntPair pozicija) // provjera polja pri pomjeranju
    {
//

        for(int i = pozicija.getVrsta()-2; i<=pozicija.getVrsta()+2; i++)
        {
            for (int j = pozicija.getKolona() - 2; j <= pozicija.getKolona() + 2; j++)
            {
                if(Grad.grad.get(new IntPair(i,j)) == null)
                    return false;
                CopyOnWriteArrayList<Stanovnik> st = Grad.grad.get(new IntPair(i,j)).getStanovnici();
                if(!st.isEmpty())
                {
                    for(Stanovnik s : st)
                    {
                        if(Grad.grad.get(new IntPair(i, j)).getKuca()!=null)
                            continue;
                        if(s.idKuce!=idKuce)
                            return false;
                    }
                }

            }
        }
        return true;
    }

    @Override
    public void inicijalizujDozvole() // izracunavanje dozvola za stariju osobu
    {
        int dozvola = STARI_DOZVOLA;
        sjeverDozvoljeno= pozicija.getVrsta() - dozvola;
        jugDozvoljeno = pozicija.getVrsta() + dozvola;
        istokDozvoljeno = pozicija.getKolona() + dozvola;
        zapadDozvoljeno = pozicija.getKolona() - dozvola;

        if(sjeverDozvoljeno<0)
        {
            jugDozvoljeno = dozvola + Math.abs(sjeverDozvoljeno);
            sjeverDozvoljeno=dozvola - Math.abs(sjeverDozvoljeno);
            if(zapadDozvoljeno<0)
            {
                istokDozvoljeno = dozvola + Math.abs(zapadDozvoljeno);
                zapadDozvoljeno= dozvola - Math.abs(zapadDozvoljeno);
                return;
            }
            zapadDozvoljeno=dozvola;
            if(istokDozvoljeno>Grad.MAT_DIM-1)
            {
                istokDozvoljeno= dozvola  - (istokDozvoljeno-Grad.MAT_DIM + 1);
                zapadDozvoljeno = dozvola + (dozvola - istokDozvoljeno);
                return;
            }
            istokDozvoljeno=dozvola;
            return;
        }
        else
        {
            sjeverDozvoljeno=dozvola;
        }

        if(jugDozvoljeno>Grad.MAT_DIM-1)
        {
            jugDozvoljeno= dozvola - (jugDozvoljeno - Grad.MAT_DIM+1);
            sjeverDozvoljeno = dozvola + (dozvola - jugDozvoljeno);
            if(zapadDozvoljeno<0)
            {
                istokDozvoljeno = dozvola + Math.abs(zapadDozvoljeno);
                zapadDozvoljeno= dozvola - Math.abs(zapadDozvoljeno);
                return;
            }
            zapadDozvoljeno=dozvola;
            if(istokDozvoljeno>Grad.MAT_DIM-1)
            {
                istokDozvoljeno= dozvola  - (istokDozvoljeno-Grad.MAT_DIM + 1);
                zapadDozvoljeno = dozvola + (dozvola - istokDozvoljeno);
                return;
            }
            istokDozvoljeno=dozvola;
            return;
        }
        else
        {
            jugDozvoljeno=dozvola;
        }


        if(zapadDozvoljeno<0)
        {
            istokDozvoljeno = dozvola + Math.abs(zapadDozvoljeno);
            zapadDozvoljeno= dozvola - Math.abs(zapadDozvoljeno);
            return;
        }
        else
        {
            zapadDozvoljeno=dozvola;
        }

        if(istokDozvoljeno>Grad.MAT_DIM-1)
        {
            istokDozvoljeno= dozvola  - (istokDozvoljeno-Grad.MAT_DIM + 1);
            zapadDozvoljeno = dozvola + (dozvola - istokDozvoljeno);
            return;

        }
        else
        {
            istokDozvoljeno=dozvola;
        }

    }
}
