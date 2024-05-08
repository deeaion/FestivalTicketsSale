package org.example.Controller.Employee;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.Controller.MessageAlert;
import org.example.Model.Spectacol;
import org.example.Service.FestivalService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EmployeeController {
    //filter items
    @FXML
    private ComboBox<String> artistCombo;
    @FXML
    private DatePicker datePickMain;


    //Sell items

    @FXML
    private Button btnSell;

    @FXML
    private Button btn_sell;
    //Log out
    @FXML
    private Button btn_log_out;

    @FXML
    private Button btn_log_out_main;

    @FXML
    private VBox showDetails;
    //Table in main tab
    @FXML
    private TableColumn<Spectacol,String> col_artist_main;
    @FXML
    private TableColumn<Spectacol, Integer> col_available_main;
    @FXML
    private TableColumn<Spectacol, LocalDateTime> col_date_main;
    @FXML
    private TableColumn<Spectacol,String> col_place_main;
    @FXML
    private TableColumn<Spectacol, Integer> col_sold_main;
    //Table in secondary tab
    @FXML
    private TableColumn<Spectacol,String> col_artist;
    @FXML
    private TableColumn<Spectacol, Integer> col_available;
    @FXML
    private TableColumn<Spectacol, LocalDateTime> col_date;
    @FXML
    private TableColumn<Spectacol,String> col_place;

    //filter secondary tab
    @FXML
    private DatePicker datePick;


    //secondary tab
    @FXML
    private Tab dateTab;

    @FXML
    private DialogPane details_menu;

    @FXML
    private Label lbl_artist;

    @FXML
    private Label lbl_date;

    @FXML
    private Label lbl_place;

    @FXML
    private Spinner<Integer> slide_ticket;
    @FXML
    private Spinner<Integer> slide_ticket_main;

    @FXML
    private TableView<Spectacol> tbl;

    @FXML
    private TableView<Spectacol> tbl_Main;

    @FXML
    private TextField txt_client;
    @FXML
    private TextField txt_client_main;
    @FXML
    private TabPane tabPane;
    private void setSpinners()
    {
        SpinnerValueFactory.IntegerSpinnerValueFactory availableFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        slide_ticket_main.setValueFactory(availableFactory);

        // Configure spinSold
        SpinnerValueFactory.IntegerSpinnerValueFactory soldFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        slide_ticket.setValueFactory(soldFactory);
        datePick.setOnAction(event -> {
            spectacolObservableList.setAll((Collection<? extends Spectacol>) service.filterSpectacole(datePick.getValue().atStartOfDay(),artistCombo.getValue()));
            tbl.setItems(spectacolObservableList);
        });
    }
    private void setListeners()
    {
        datePickMain.setOnAction(event -> {
            List<Spectacol> listFiltered= (List<Spectacol>) service.filterSpectacole(datePickMain.getValue().atStartOfDay(),artistCombo.getValue());
            spectacolObservableListMain.setAll(listFiltered);
            tbl_Main.setItems(spectacolObservableListMain);
            spectacolObservableList.setAll(listFiltered);
            tbl.setItems(spectacolObservableList);

            tabPane.getSelectionModel().select(1);
        });
        datePick.setOnAction(event -> {
            handleFilterSecond(event);
        });
        artistCombo.setOnAction(event -> {
            handleFilter(event);
        });
    }
    ObservableList<String> artistObservableList= FXCollections.observableArrayList();
    @FXML
    public void initialize()
    {

        setSpinners();
        setListeners();

    }

    private void setCombo() {
        List<String> artisti= (List<String>) service.getArtisti();
        artisti.add("None");
        artistObservableList.setAll(artisti);
        artistCombo.setItems(artistObservableList);
        artistCombo.setValue("None");
    }

    private List<Spectacol> filterHelper(DatePicker dateP, ComboBox<String> artistC, ObservableList<Spectacol> spectacolObservable, TableView<Spectacol> table)
    {
        LocalDateTime dateTime=null;
        List<Spectacol> list=new ArrayList<>();
        if(dateP.getValue()==null)
        {
            dateTime=null;
            list= (List<Spectacol>) service.filterSpectacole(dateTime,artistC.getValue());
        }

        else
        {
            dateTime=dateP.getValue().atStartOfDay();
            list= (List<Spectacol>) service.filterSpectacole(dateTime,artistC.getValue());
        }
        return list;
    }

    @FXML
    void handleFilter(ActionEvent event) {
        spectacolObservableListMain.setAll(filterHelper(datePickMain,artistCombo,spectacolObservableListMain,tbl_Main));
        tbl_Main.setItems(spectacolObservableListMain);
    }
    @FXML
    void handleFilterSecond(ActionEvent event) {
        spectacolObservableList.setAll(filterHelper(datePick,artistCombo,spectacolObservableList,tbl));
        tbl.setItems(spectacolObservableList);
    }
    @FXML
    void handleLogOut(ActionEvent event) {
        stage.close();
    }

    @FXML
    void handleSell(ActionEvent event) {
        handleSellCommon(txt_client_main.getText(), tbl_Main.getSelectionModel().getSelectedItem(), slide_ticket_main.getValue());
        handleFilter(null);
        handleFilterSecond(null);
    }

    @FXML
    void handleSellSecond(ActionEvent event) {
        handleSellCommon(txt_client.getText(), tbl.getSelectionModel().getSelectedItem(), slide_ticket.getValue());
        handleFilterSecond(null);
        handleFilter(null);
    }

    private void handleSellCommon(String clientName, Spectacol selectedItem, int ticketValue) {
        try {
            service.addBilet(clientName, selectedItem, ticketValue);
        } catch (RuntimeException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "!", e.getMessage());
            return;
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "!", "Bilet vandut cu succes!");
    }
    private FestivalService service;
    private Stage stage;
    public void setAngajatController(FestivalService service, Stage stage) {
        this.showDetails.setVisible(false);
        this.service=service;
        this.stage=stage;
        setTableMain();
        spectacolObservableList.setAll((Collection<? extends Spectacol>) service.getSpectacole());
        setTableDate();
        setCombo();
    }

    private void setTableDate() {
        col_artist.setCellValueFactory(new PropertyValueFactory<Spectacol,String>("artist"));
        col_available.setCellValueFactory(new PropertyValueFactory<Spectacol,Integer>("numar_locuri_disponibile"));
        col_date.setCellValueFactory(new PropertyValueFactory<Spectacol,LocalDateTime>("data"));
        col_place.setCellValueFactory(new PropertyValueFactory<Spectacol,String>("locatie"));
        tbl.setRowFactory(tv -> new TableRow<Spectacol>() {
            @Override
            protected void updateItem(Spectacol item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.getNumar_locuri_disponibile() == 0) {
                    setStyle("-fx-background-color: red;");
                } else {
                    setStyle("");
                }
            }
        });
        tbl.setItems(spectacolObservableList);

    }

    ObservableList<Spectacol> spectacolObservableListMain= FXCollections.observableArrayList();
    ObservableList<Spectacol> spectacolObservableList=FXCollections.observableArrayList();
    private void setTableMain() {
        spectacolObservableListMain.setAll((Collection<? extends Spectacol>) service.getSpectacole());
        col_artist_main.setCellValueFactory(new PropertyValueFactory<Spectacol,String>("artist"));
        col_available_main.setCellValueFactory(new PropertyValueFactory<Spectacol,Integer>("numar_locuri_disponibile"));
        col_date_main.setCellValueFactory(new PropertyValueFactory<Spectacol,LocalDateTime>("data"));
        col_place_main.setCellValueFactory(new PropertyValueFactory<Spectacol,String>("locatie"));
        col_sold_main.setCellValueFactory(new PropertyValueFactory<Spectacol,Integer>("numar_locuri_vandute"));
        tbl_Main.setRowFactory(tv -> new TableRow<Spectacol>() {
            @Override
            protected void updateItem(Spectacol item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.getNumar_locuri_disponibile() == 0) {
                    setStyle("-fx-background-color: red;");
                } else {
                    setStyle("");
                }
            }
        });
        tbl_Main.setItems(spectacolObservableListMain);
        tbl_Main.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDetails.setVisible(true);
                lbl_artist.setText(newSelection.getArtist());
                lbl_date.setText(newSelection.getData().toString());
                lbl_place.setText(newSelection.getLocatie());
                SpinnerValueFactory.IntegerSpinnerValueFactory availableFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, newSelection.getNumar_locuri_disponibile(), 0);
                slide_ticket.setValueFactory(availableFactory);
            }
        });


    }

}
