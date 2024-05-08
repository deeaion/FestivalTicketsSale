package org.example.Controller;

import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Controller.Admin.AdminController;
import org.example.Controller.Employee.EmployeeController;
import org.example.Service.FestivalService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField email_field;

    @FXML
    private Button login_button;

    @FXML
    private PasswordField password_field;

    @FXML
    void handleLogIn(ActionEvent event) throws IOException {
        if(service.verifyLogInInformation(email_field.getText(),password_field.getText()))
        {
            if(isAdmin(email_field.getText()))
            {
                loadAdmin();
            }
            else
            {
                loadAngajat();
            }
        }
        else
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Incorrect data!","Please check your user/email and password!");
        }
    }

    private void loadAngajat() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/employee/employee-view.fxml"));
        Parent root = fxmlLoader.load();
        EmployeeController controller = fxmlLoader.getController();
        if (controller == null) {
            throw new IllegalStateException("Controller is null");
        }
        controller.setAngajatController(service,stage);
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.show();
    }

    private void loadAdmin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/admin/admin-view.fxml"));
        Parent root = fxmlLoader.load();
        AdminController controller = fxmlLoader.getController();
        if (controller == null) {
            throw new IllegalStateException("Controller is null");
        }
        controller.setAdminController(service,stage);
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.show();
    }

    private boolean isAdmin(String emailField) {
        if((emailField.equals("admin")) || emailField.equals("admin@gmail.com"))
        {
            return true;
        }
        return false;
    }

    private FestivalService service;
    private Stage stage;

    public void setLogIn(FestivalService service, Stage stage) {
        this.service=service;
        this.stage=stage;

    }
}
