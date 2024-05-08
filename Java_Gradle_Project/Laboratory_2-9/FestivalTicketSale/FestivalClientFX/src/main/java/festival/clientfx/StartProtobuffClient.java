package festival.clientfx;

import festival.clientfx.gui.EmployeeController;
import festival.clientfx.gui.LoginController;
import festival.networking.protobuffprotocol.FestivalServicesProtoProxy;
import festival.service.IFestivalServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartProtobuffClient extends Application {
    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Properties clientProps=new Properties();

        try {
            clientProps.load(StartProtobuffClient.class.getResourceAsStream("/festivalclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        String serverIP=clientProps.getProperty("festival.server.host",defaultServer);
        int serverPort=defaultChatPort;
        try{
            serverPort=Integer.parseInt(clientProps.getProperty("festival.server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+defaultChatPort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);
        IFestivalServices services=new FestivalServicesProtoProxy(serverIP, serverPort);
        loadStage(primaryStage, services); }

    public static void main(String[] args) {

       launch(args);
    }
    private void loadStage(Stage primaryStage, IFestivalServices server) throws IOException {
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

    }}

