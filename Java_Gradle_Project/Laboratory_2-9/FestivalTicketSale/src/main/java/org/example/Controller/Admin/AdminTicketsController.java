package org.example.Controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.Controller.MessageAlert;
import org.example.Model.Bilet;
import org.example.Model.Spectacol;
import org.example.Service.FestivalService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AdminTicketsController {

    @FXML
    private Button btnAddShow;

    @FXML
    private TableColumn<Spectacol, String> colArtist;

    @FXML
    private TableColumn<Spectacol, Integer> colAvailable;

    @FXML
    private TableColumn<Spectacol, LocalDateTime> colDate;

    @FXML
    private TableColumn<Spectacol, String> colPlace;

    @FXML
    private TableColumn<Spectacol, Integer> colSold;

    @FXML
    private ListView<Bilet> listTickets;

    @FXML
    private Spinner<Integer> spinAvailable;

    @FXML
    private Spinner<Integer> spinSold;

    @FXML
    private TableView<Spectacol> tblShows;

    @FXML
    private TextField txtArtist;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtPlace;
    @FXML
    private TextField txtHour;
    @FXML
    private TextField txtMinute;
    public ObservableList<Spectacol> modelSpectacole = FXCollections.observableArrayList();


    @FXML
    void handleAddShow(ActionEvent event) {
        try {
            LocalDate date= txtDate.getValue();
            int hour= Integer.parseInt(txtHour.getText());
            int minute=Integer.parseInt(txtMinute.getText());
            LocalDateTime dateWithHour=LocalDateTime.of(date, LocalTime.of(hour,minute));
            service.addSpectacol(txtPlace.getText(), dateWithHour, spinAvailable.getValue(), spinSold.getValue(), txtArtist.getText());
        }
        catch (RuntimeException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"!",e.getMessage());
        }

    }
    private FestivalService service;
    private Stage stage;
    public void setTicketsController(FestivalService service, Stage stage) {
        this.service=service;
        this.stage=stage;
        initTable();

    }
    public void initialize()
    {
        SpinnerValueFactory.IntegerSpinnerValueFactory availableFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        spinAvailable.setValueFactory(availableFactory);

        // Configure spinSold
        SpinnerValueFactory.IntegerSpinnerValueFactory soldFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        spinSold.setValueFactory(soldFactory);
    }

    private void initTable() {
        List<Spectacol> spectacolList= (List<Spectacol>) service.getSpectacole();
        modelSpectacole.setAll(spectacolList);
        setTable();
    }

    private void setTable() {
        colArtist.setCellValueFactory(new PropertyValueFactory<Spectacol,String>("artist"));
        colPlace.setCellValueFactory(new PropertyValueFactory<Spectacol,String>("locatie"));
        colDate.setCellValueFactory(new PropertyValueFactory<Spectacol,LocalDateTime>("data"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<Spectacol,Integer>("numar_locuri_disponibile"));
        colSold.setCellValueFactory(new PropertyValueFactory<Spectacol,Integer>("numar_locuri_vandute"));
        tblShows.setItems(modelSpectacole);
        tblShows.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadTickets(newSelection);
            }
        });
    }
    ObservableList<Bilet> modelTickets=FXCollections.observableArrayList();

    private void loadTickets(Spectacol newSelection) {
        List<Bilet> tickets= (List<Bilet>) service.getBileteSpectacol(newSelection);
        if(tickets!=null)
            modelTickets.setAll(tickets);
        listTickets.setItems(modelTickets);

    }
}
