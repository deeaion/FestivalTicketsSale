import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.Controller.LoginController;
import org.example.Model.Validators.ModelValidators.AngajatValidator;
import org.example.Model.Validators.ModelValidators.SpectacolValidator;
import org.example.Repository.DBAngajatRepository;
import org.example.Repository.DBBiletRepository;
import org.example.Repository.DBSpectacolRepository;
import org.example.Repository.Interfaces.AngajatRepository;
import org.example.Repository.Interfaces.BiletRepository;
import org.example.Repository.Interfaces.SpectacolRepository;
import org.example.Service.FestivalService;
import org.example.Service.FestivalServiceImplementation;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TicketSaleMain extends Application {
    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Properties properties=new Properties();
        try
        {
            properties.load(new FileReader("bd.config"));

        }
        catch (IOException e)
        {
            System.out.println("Could not find "+e);
        }
        SpectacolValidator spectacolValidator=new SpectacolValidator();
        AngajatValidator angajatValidator=new AngajatValidator();
        SpectacolRepository spectacolRepository=new DBSpectacolRepository(properties,spectacolValidator);
        AngajatRepository angajatRepository=new DBAngajatRepository(properties,angajatValidator);
        BiletRepository biletRepository=new DBBiletRepository(properties);
        FestivalService service=new FestivalServiceImplementation(angajatRepository,biletRepository,spectacolRepository);
        initLogIn(primaryStage,service);
    }
    private void initLogIn(Stage primaryStage,FestivalService service)
    {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/views/login-view.fxml"));
            Parent root = fxmlLoader.load();
            LoginController controller = fxmlLoader.getController();
            if (controller == null) {
                throw new IllegalStateException("Controller is null");
            }
            controller.setLogIn(service,primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
