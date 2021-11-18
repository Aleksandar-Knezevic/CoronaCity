package org.unibl.etf.pj2.projekat.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.unibl.etf.pj2.projekat.helper.Serializator;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.simulacija.Grad;

import java.io.File;
import java.util.logging.Level;

public class StartController
{

    @FXML
    private TextField djecaField;
    @FXML
    private TextField odrasliField;
    @FXML
    private TextField stariField;
    @FXML
    private TextField kuceField;
    @FXML
    private TextField punktoviField;
    @FXML
    private TextField ambVozilaField;

    @FXML
    public void pokreniButtonControl()
    {
        try
        {
        Grad.pocetakSimulacije=System.currentTimeMillis();
        Grad.inicijalizuj();
        Grad.inicijalizujGrad(Integer.parseInt(djecaField.getText()),
                              Integer.parseInt(odrasliField.getText()),
                              Integer.parseInt(stariField.getText()),
                              Integer.parseInt(kuceField.getText()),
                              Integer.parseInt(punktoviField.getText()),
                              Integer.parseInt(ambVozilaField.getText()));


            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1200, 700));
            stage.setTitle("Corona City");
            stage.show();

            djecaField.getScene().getWindow().hide();
        }
        catch(Exception e)
        {
            MyLogger.log(Level.WARNING, "Greska pri otvaranju prozora", e);
            System.exit(0);
        }
    }

    public void pokreniPonovoButton() {
        if (new File("." + File.separator + Serializator.serFile).exists())
        {
            Serializator.getInstance().deserialize();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1200, 700));
            stage.setTitle("Corona City");
            stage.show();
            djecaField.getScene().getWindow().hide();

        } catch (Exception e) {
            MyLogger.log(Level.WARNING, "Greska pri ponovnom pokretanju", e);
        }
    }
    }
}
