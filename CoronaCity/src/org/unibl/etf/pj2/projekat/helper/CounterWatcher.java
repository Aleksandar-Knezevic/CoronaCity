package org.unibl.etf.pj2.projekat.helper;

import javafx.application.Platform;
import org.unibl.etf.pj2.projekat.gradjevine.Ambulanta;
import org.unibl.etf.pj2.projekat.logger.MyLogger;
import org.unibl.etf.pj2.projekat.main.MainController;
import org.unibl.etf.pj2.projekat.simulacija.Grad;

import java.io.File;
import java.nio.file.*;
import java.util.logging.Level;

public class CounterWatcher extends Thread
{
    MainController mainController;

    public CounterWatcher(MainController mc)
    {
        mainController=mc;
    }

    @Override
    public void run() // prati promjene fajla sa brojem zarazenih i oporavljenih stanovnika
    {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path directory = Paths.get("." + File.separator + "info");
            WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            while (Grad.allowed)
            {
                for(WatchEvent<?> event : watchKey.pollEvents())
                {
                    try
                    {
                        var ref = new Object() {
                            String printString = "";
                        };
                        synchronized (Ambulanta.counterSyncObject) {
                            for (String s : Files.readAllLines(Paths.get("." + File.separator + Ambulanta.infoDirectory + File.separator + event.context().toString()))) {
                                ref.printString = ref.printString + s + '\n';
                            }
                            Platform.runLater(() ->
                            {
                                mainController.statisticLabel.setText(ref.printString);
                            });
                        }
                    }
                    catch(Exception e)
                    {
                        MyLogger.log(Level.WARNING, "Greska u azuriranju", e);
                    }
                }
                sleep(300);
            }
        } catch (Exception e) {
            MyLogger.log(Level.WARNING, "Greska u watcheru", e);
        }

    }
}
