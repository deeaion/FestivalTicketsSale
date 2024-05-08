package festival.clientfx;

import festival.clientfx.gui.EmployeeController;
import festival.clientfx.gui.LoginController;
import festival.networking.jsonprotocol.FestivalServicesJsonProxy;
import festival.service.IFestivalServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartJsonClientFX extends Application {
    private Stage primaryStage;

    private static int defaultFestivalPort = 55555;
    private static String defaultServer = "localhost";
    private Properties getProperties() throws IOException {
        Properties clientProps = new Properties();

            clientProps.load(StartJsonClientFX.class.getResourceAsStream("/festivalclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);

        return clientProps;
    }
    private IFestivalServices setServer(Properties clientProps) {
        String serverIP = clientProps.getProperty("festival.server.host", "localhost");
        int serverPort = defaultFestivalPort;
        try{
            serverPort = Integer.parseInt(clientProps.getProperty("festival.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultFestivalPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        IFestivalServices server = new FestivalServicesJsonProxy(serverIP, serverPort);
        return server;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = null;
        try {
            clientProps = getProperties();
         } catch (IOException e) {
        System.err.println("Cannot find file! " + e);
        return ;
        }
        IFestivalServices server=setServer(clientProps);
        loadStage(primaryStage, server);
    }



    private void loadStage(Stage primaryStage, IFestivalServices server) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/views/login-view.fxml"));
            Parent root=fxmlLoader.load();

            LoginController controller = fxmlLoader.<LoginController>getController();
            controller.setLogIn(server,primaryStage);

            FXMLLoader cloader=new FXMLLoader(getClass().getClassLoader().getResource("views/employee-view.fxml"));
            Parent croot=cloader.load();
            EmployeeController employeeCtrl=cloader.<EmployeeController>getController();
            employeeCtrl.setService(server);
            controller.setEmployeeController(employeeCtrl);
            controller.setParent(croot);

            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root ));
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
