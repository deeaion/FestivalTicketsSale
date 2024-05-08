package org.example.Controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.Controller.MessageAlert;
import org.example.Model.Angajat;
import org.example.Service.FestivalService;

import java.util.List;

public class AdminEmployeeController {

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<Angajat, String> colEmail;

    @FXML
    private TableColumn<Angajat, String> colFName;

    @FXML
    private TableColumn<Angajat, String> colLName;

    @FXML
    private TableColumn<Angajat, String> colPassword;

    @FXML
    private TableColumn<Angajat, String> colUsername;

    @FXML
    private TableView<Angajat> tblEmployee;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFName;

    @FXML
    private TextField txtLName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;
    ObservableList<Angajat> observableList= FXCollections.observableArrayList();
    @FXML
    void handleAddEmployee(ActionEvent event) {
        try {
            service.addAngajat(txtFName.getText(),txtLName.getText(),txtEmail.getText(),txtUsername.getText(),txtPassword.getText());
        }
        catch (RuntimeException exception)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error!",exception.getMessage());
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Employee","Employee added successfully!");
    }
    private void initModel() {
        List<Angajat> angajatList= (List<Angajat>) service.getAngajati();
        observableList.setAll(angajatList);
        colEmail.setCellValueFactory(new PropertyValueFactory<Angajat,String>("email"));
        colFName.setCellValueFactory(new PropertyValueFactory<Angajat,String>("nume"));
        colLName.setCellValueFactory(new PropertyValueFactory<Angajat,String>("prenume"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Angajat,String>("password"));
        colUsername.setCellValueFactory(new PropertyValueFactory <Angajat,String>("username"));
        tblEmployee.setItems(observableList);
    }
    @FXML
    public void initialize()
    {
//        initModel();
//        tblEmployee.setItems(observableList);

    }
    private FestivalService service;
    private Stage stage;
    public void setEmployeeController(FestivalService service, Stage stage) {
        this.service=service;
        this.stage=stage;
        initModel();
    }
}
