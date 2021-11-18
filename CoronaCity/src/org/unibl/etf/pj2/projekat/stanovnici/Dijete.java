package org.unibl.etf.pj2.projekat.stanovnici;


import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.simulacija.IntPair;

import java.util.concurrent.CopyOnWriteArrayList;

public class Dijete extends Stanovnik
{
    public Dijete()
    {
        super();
    }

    public Dijete(int idStanovnika, String ime, int godine)
    {
        super(idStanovnika, ime, godine);
    }

    @Override
    public boolean checkFields(IntPair pozicija) // provjera da li dijete smije preci na drugo polje
    {

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
                        if(s.godine>65 && s.idKuce!=idKuce)
                            return false;
                    }
                }

            }
        }
        return true;
    }


    @Override
    public void inicijalizujDozvole() // dijete ima neograniceno kretanje
    {
        sjeverDozvoljeno= pozicija.getVrsta();
        jugDozvoljeno = Grad.MAT_DIM - 1 - pozicija.getVrsta();
        istokDozvoljeno = Grad.MAT_DIM - 1 - pozicija.getKolona();
        zapadDozvoljeno = pozicija.getKolona();
    }

}
