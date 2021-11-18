package org.unibl.etf.pj2.projekat.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import org.unibl.etf.pj2.projekat.gradjevine.Ambulanta;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.stanovnici.Pol;
import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class StatistikaController implements Initializable
{

    @FXML
    private PieChart polChart;

    @FXML
    private PieChart godineChart;

    private int muski;
    private int zenski;
    private int djeca;
    private int odrasli;
    private int stari;
    public static final String statistikaFile = "statistika.csv";
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        for(Stanovnik s : Grad.sviZarazeni)
        {
            if(s.getPol()== Pol.MUSKO)
                muski++;
            else
                zenski++;
            if(s.getGodine()<=18)
                djeca++;
            else if(s.getGodine()>=66)
                stari++;
            else
                odrasli++;
        }

        ObservableList<PieChart.Data> polChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Muski", muski),
                        new PieChart.Data("Zenski", zenski));


        polChart.setData(polChartData);

        ObservableList<PieChart.Data> godineChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Djeca", djeca),
                        new PieChart.Data("Odrasli", odrasli),
                        new PieChart.Data("Stari", stari));

        godineChart.setData(godineChartData);

    }

    public void preuzmiButton()
    {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(statistikaFile))))
        {
            bw.write("zarazeni,oporavljeni,muski,zenski,djeca,odrasli,stari\n");
            bw.append(String.valueOf(Ambulanta.ukupnoZarazenih)).append(",").append(String.valueOf(Ambulanta.ukupnoOzdravljelih)).append(",").append(String.valueOf(muski)).append(",").append(String.valueOf(zenski)).append(",").append(String.valueOf(djeca)).append(",").append(String.valueOf(odrasli)).append(",").append(String.valueOf(stari));
        }
        catch(Exception e)
        {
            MyLogger.log(Level.WARNING, "Greska pri cuvanju statistike", e);
        }


    }
}
