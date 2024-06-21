package festival.clientfx.gui;

import festival.model.Angajat;
import festival.service.Exceptions.FestivalException;
import festival.service.IFestivalServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.IOException;

public class LoginController {
    private IFestivalServices server;
    private EmployeeController employeeController;
    private Angajat angajat;
    Parent mainEmployeeParent;
    @FXML
    private Stage stage;

    @FXML
    private TextField email_field;

    @FXML
    private Button login_button;

    @FXML
    private PasswordField password_field;

    @FXML
    void handleLogIn(ActionEvent event) throws IOException {
        String nume=email_field.getText();
        String passwd=password_field.getText();

        try {
            this.angajat=server.logIn(nume,passwd,employeeController);
            if(angajat!=null)
            {

                    loadAngajat(event);

            }
            else
            {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Incorrect data!","Please check your user/email and password!");
            }
        } catch (FestivalException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error!",e.getMessage());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadAngajat(ActionEvent actionEvent) throws IOException {
        Stage stageN=new Stage();
        stageN.setTitle("Employee Window"+angajat.getNume());
        stageN.setScene(new Scene(mainEmployeeParent));
        stageN.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                employeeController.logOut();
                System.exit(0);
            }
        });

        stageN.show();
        employeeController.setAngajatController(server,stageN,angajat);

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

    }



    public void setLogIn(IFestivalServices service, Stage stage) {
        this.server=service;
        this.stage=stage;


    }

    public void setEmployeeController(EmployeeController employeeCtrl) {
        this.employeeController=employeeCtrl;
    }

    public void setParent(Parent croot) {
        this.mainEmployeeParent=croot;
    }
}
