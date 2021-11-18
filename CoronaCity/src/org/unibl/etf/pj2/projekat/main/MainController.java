package org.unibl.etf.pj2.projekat.main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.unibl.etf.pj2.projekat.gradjevine.Ambulanta;
import org.unibl.etf.pj2.projekat.helper.CounterWatcher;
import org.unibl.etf.pj2.projekat.helper.Refresher;
import org.unibl.etf.pj2.projekat.helper.Serializator;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.simulacija.Grad;
import org.unibl.etf.pj2.projekat.simulacija.IntPair;
import org.unibl.etf.pj2.projekat.simulacija.Nadzirac;
import org.unibl.etf.pj2.projekat.stanovnici.Pol;
import org.unibl.etf.pj2.projekat.stanovnici.Stanovnik;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class MainController implements Initializable, Serializable
{

    @FXML
    public Pane pane;

    @FXML
    public Circle alertCircle;

    @FXML
    public TextArea textArea;

    @FXML
    public Label statisticLabel;

    public static final String simFileName = "SIM-JavaKov-20-";
    public static final String extension = ".txt";



    public void omoguciKretanjeButton()
    {
        Grad.sveKuce.forEach(e ->
        {
            e.inicijalizujUkucane();
            e.pokreniUkucane();
        });

    }

    public void posaljiVoziloButton() {
        synchronized (Ambulanta.counterSyncObject) {
            Nadzirac.obradi();
            Platform.runLater(() ->
            {
                if (alertCircle.isVisible() && Nadzirac.alarmi.isEmpty())
                    alertCircle.setVisible(false);
            });
        }
    }

    public void pregledajStanjeButton()
    {
        VBox stanjeWindow = new VBox(10);
        for(int i=0;i<Grad.sveAmbulante.size();i++)
        {
            Label l = new Label("Ambulanta " + i + " Kapacitet:" + Grad.sveAmbulante.get(i).getKapacitet() + " Zarazeni:" + Grad.sveAmbulante.get(i).getZarazeni().size());
            l.setWrapText(true);
            l.setStyle("-fx-font-weight: bold;");
            stanjeWindow.getChildren().add(l);
        }
        Button button = new Button("Kreiraj novu ambulantu");
        button.setOnAction(e ->
        {
                for (int i = 0; i < Grad.MAT_DIM - 1; i++) {

                    IntPair ip = new IntPair(i, 0);
                    if (Grad.grad.get(ip).getAmbulanta() == null) {
                            Ambulanta a = new Ambulanta(ip);
                            Grad.grad.get(ip).setAmbulanta(a);
                            Grad.sveAmbulante.add(a);
                            a.start();
                        break;
                    }
                    ip = new IntPair(i, Grad.MAT_DIM-1);
                    if (Grad.grad.get(ip).getAmbulanta() == null) {
                        Ambulanta a = new Ambulanta(ip);
                        Grad.grad.get(ip).setAmbulanta(a);
                        Grad.sveAmbulante.add(a);
                        a.start();
                        break;
                    }

                    ip = new IntPair(0, i);
                    if (Grad.grad.get(ip).getAmbulanta() == null) {
                        Ambulanta a = new Ambulanta(ip);
                        Grad.grad.get(ip).setAmbulanta(a);
                        Grad.sveAmbulante.add(a);
                        a.start();
                        break;
                    }

                    ip = new IntPair(Grad.MAT_DIM-1, i);
                    if (Grad.grad.get(ip).getAmbulanta() == null) {
                        Ambulanta a = new Ambulanta(ip);
                        Grad.grad.get(ip).setAmbulanta(a);
                        Grad.sveAmbulante.add(a);
                        a.start();
                        break;
                    }

                }
        });
        stanjeWindow.getChildren().add(button);
        Platform.runLater(() ->
        {
            Stage pregled = new Stage();
            pregled.setTitle("Stanje");
            pregled.setScene(new Scene(stanjeWindow, 600, 200));
            pregled.show();
        });

    }

    public void prikaziStatistiku() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Statistika.fxml").openStream());
        Scene statistikaScene = new Scene(root);
        Stage newWindow = new Stage();
        newWindow.setScene(statistikaScene);
        newWindow.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        HBox[] rows = new HBox[Grad.MAT_DIM];
        VBox matrix = new VBox(3);
        for(int i=0;i<Grad.MAT_DIM;i++)
        {
            rows[i] = new HBox(3);
            for(int j=0;j<Grad.MAT_DIM;j++)
            {
                rows[i].getChildren().addAll(Grad.guiMapa.get(new IntPair(i, j)));
            }
            matrix.getChildren().addAll(rows[i]);
        }
        statisticLabel.setText("Zarazeni: " + Ambulanta.ukupnoZarazenih + '\n' + "Oporavljeni: " + Ambulanta.ukupnoOzdravljelih);
        pane.getChildren().removeIf(x -> true);

        pane.getChildren().add(matrix);

        Grad.sviStanovnici.forEach(e ->
        {
            e.setMainController(this);
        });

        Grad.sviPunktovi.forEach(e ->
        {
            e.setMainController(this);
        });

        new CounterWatcher(this).start();
        Serializator.getInstance().setMainController(this);
        if(!Nadzirac.alarmi.isEmpty())
            alertCircle.setVisible(true);

    }

    public void zaustaviSimulacijuButton()
    {
        Serializator.getInstance().serialize();
    }

    public void zavrsiSimulacijuButton()
    {
        Grad.krajSimulacije=System.currentTimeMillis();
        long ukupnoTrajanje = Grad.krajSimulacije - Grad.pocetakSimulacije + Grad.razlika;
        Calendar c = Calendar.getInstance();
        File file = new File(simFileName + c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR) + "." + new SimpleDateFormat("kk_mm_ss").format(new Date(System.currentTimeMillis())) + extension);

        int muski=0;
        int zenski=0;
        int djeca=0;
        int stari=0;
        int odrasli=0;
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


        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file));)
        {
            bw.append(new SimpleDateFormat("mm:ss:SSS").format(new Date(ukupnoTrajanje)) + '\n');
            bw.append("Stanovnici: " + Grad.sviStanovnici.size() + '\n');
            bw.append("Ambulante: " + Grad.sveAmbulante.size() + '\n');
            bw.append("Kuce: " + Grad.sveKuce.size() + '\n');
            bw.append("Punktovi:" + Grad.sviPunktovi.size() + '\n');
            bw.append("Muski: " + muski + '\n');
            bw.append("Zenski: " + zenski + '\n');
            bw.append("Djeca: " + djeca + '\n');
            bw.append("Odrasli: " + odrasli + '\n');
            bw.append("Stari: " + stari + '\n');
        }
        catch(Exception e)
        {
            MyLogger.log(Level.WARNING, "Greska pri upisu.", e);
        }
        System.exit(0);
    }

}
