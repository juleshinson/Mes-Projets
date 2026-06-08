package labyrinth;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import labyrinth.service.Services;
import labyrinth.service.dialog.DefaultDialogService;
import labyrinth.service.dialog.DialogService;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.serialization.JsonSerializationService;
import labyrinth.service.serialization.SerializationService;
import labyrinth.view.DialogHandler;
import labyrinth.view.GameView;
import labyrinth.viewmodel.BaseGameViewModel;


public class Labyrinth extends Application {
   public void start(Stage stage){
       SerializationService serializationService = new JsonSerializationService();
       DialogService dialogService = new DefaultDialogService();
       // initialisation BDD, si ca plante on continue sans persistance
       SqlPersistenceService persistenceService = null ;
       try{
           persistenceService = new SqlPersistenceService(serializationService) ;
       } catch(Exception e){
           System.out.println("attention BDD pas disponible : " + e.getMessage()) ;
       }
       Services services = new Services(serializationService, dialogService, persistenceService) ;

       GameBootstrapper bootstrapper = new GameBootstrapper(services);

       DialogHandler dialogHandler = new DialogHandler(dialogService, stage);

       BaseGameViewModel model = new BaseGameViewModel(bootstrapper.getGame(), services);

       model.setupDialogHandler(dialogHandler);

       GameView root = new GameView(model);
       Scene scene = new Scene(root, 900, 700);
       stage.titleProperty().bind(model.titleProperty());
       stage.setScene(scene);
       stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

