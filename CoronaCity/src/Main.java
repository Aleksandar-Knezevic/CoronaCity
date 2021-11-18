
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.unibl.etf.pj2.projekat.gradjevine.Kuca;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.simulacija.Grad;

import java.io.File;
import java.util.logging.Level;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(("org" + File.separator + "unibl" + File.separator + "etf" + File.separator + "pj2" + File.separator + "projekat" + File.separator + "main" + File.separator + "Start.fxml")));

        primaryStage.setTitle("Corona City");
        primaryStage.setScene(new Scene(root, 680, 470));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        try {
            MyLogger.setup();
            launch(args);
        }
        catch(Exception e)
        {
            MyLogger.log(Level.WARNING, "Logging greska", e);
        }

    }


}
