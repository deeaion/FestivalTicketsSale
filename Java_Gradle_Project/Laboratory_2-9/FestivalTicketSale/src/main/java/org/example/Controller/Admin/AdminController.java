package org.example.Controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.Service.FestivalService;

import java.io.IOException;

public class AdminController {

    @FXML
    private BorderPane BorderPane;

    @FXML
    private ImageView btnClose;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnFestivals;

    @FXML
    private Button btnHome;

    @FXML
    void handleClose(MouseEvent event) {

    }

    @FXML
    void loadEmployees(ActionEvent event) throws IOException {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/views/admin/admin-employee-view.fxml"));
        AnchorPane view= null;

        view = stageLoader.load();
        BorderPane.setCenter(view);
        AdminEmployeeController controller=stageLoader.getController();
        controller.setEmployeeController(service,stage);
    }

    @FXML
    void loadFestivals(ActionEvent event) throws IOException {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/views/admin/admin-tickets-view.fxml"));
        AnchorPane view= null;

        view = stageLoader.load();
        BorderPane.setCenter(view);
        AdminTicketsController controller=stageLoader.getController();
        controller.setTicketsController(service,stage);
    }

    @FXML
    void loadHome(ActionEvent event) throws IOException {
        FXMLLoader stageLoader=new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/views/admin/admin-home-view.fxml"));
        AnchorPane view= null;

            view = stageLoader.load();
            BorderPane.setCenter(view);
            AdminHomeController controller=stageLoader.getController();
            controller.setHome(service,stage);

    }
    private FestivalService service;
    private Stage stage;
    public void setAdminController(FestivalService service, Stage stage) throws IOException {
        this.service=service;
        this.stage=stage;
        loadHome(null);
    }
}
