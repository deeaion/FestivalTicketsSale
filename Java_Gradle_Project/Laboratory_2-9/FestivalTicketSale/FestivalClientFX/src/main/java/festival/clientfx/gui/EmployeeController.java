package festival.clientfx.gui;

import festival.model.Angajat;
import festival.model.Spectacol;
import festival.model.SpectacolDTO;
import festival.service.Exceptions.FestivalException;
import festival.service.IFestivalObserver;
import festival.service.IFestivalServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable, IFestivalObserver {
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
    private TableColumn<SpectacolDTO,String> col_artist_main;
    @FXML
    private TableColumn<SpectacolDTO, Integer> col_available_main;
    @FXML
    private TableColumn<SpectacolDTO, String> col_date_main;
    @FXML
    private TableColumn<SpectacolDTO,String> col_place_main;
    @FXML
    private TableColumn<SpectacolDTO, Integer> col_sold_main;
    //Table in secondary tab
    @FXML
    private TableColumn<SpectacolDTO,String> col_artist;
    @FXML
    private TableColumn<SpectacolDTO, Integer> col_available;
    @FXML
    private TableColumn<SpectacolDTO, String> col_date;
    @FXML
    private TableColumn<SpectacolDTO,String> col_place;

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
    private TableView<SpectacolDTO> tbl;

    @FXML
    private TableView<SpectacolDTO> tbl_Main;

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
            try {
                SpectacolDTO [] spectacols=service.filterSpectacole(datePick.getValue().atStartOfDay(),artistCombo.getValue(),false);
                spectacolObservableList.setAll(spectacols);

            } catch (FestivalException e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
            }
            tbl.setItems(spectacolObservableList);
        });
    }

    private void setListeners()
    {

        datePickMain.setOnAction(event -> {
            SpectacolDTO[] listFiltered= new SpectacolDTO[0];
            try {
                listFiltered = service.filterSpectacole(datePickMain.getValue().atStartOfDay(),artistCombo.getValue(),false);
            } catch (FestivalException e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
            }
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

    private void setCombo() throws FestivalException {
        String [] artists=service.getArtisti();
        List<String> artisti=new ArrayList<>();
        artisti.addAll(List.of(artists) );
        artisti.add("None");
        artistObservableList.setAll(artisti);
        artistCombo.setItems(artistObservableList);
        artistCombo.setValue("None");
    }

    private List<SpectacolDTO> filterHelper(DatePicker dateP, ComboBox<String> artistC, ObservableList<SpectacolDTO> spectacolObservable, TableView<SpectacolDTO> table,boolean filtered)
    {
        LocalDateTime dateTime=null;
        List<SpectacolDTO> list=new ArrayList<>();
        if(dateP.getValue()==null)
        {
            dateTime=LocalDateTime.now();
            try {
                list= List.of(service.filterSpectacole(dateTime, artistC.getValue(),false));
            } catch (FestivalException e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
            }
        }

        else
        {
            dateTime=dateP.getValue().atStartOfDay();
            try {
                list= List.of(service.filterSpectacole(dateTime, artistC.getValue(),true));
            } catch (FestivalException e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
            }
        }
        return list;
    }

    public void setService(IFestivalServices service) {
        this.service = service;
    }
    @FXML
    void handleFilter(ActionEvent event) {
        spectacolObservableListMain.setAll(filterHelper(datePickMain,artistCombo,spectacolObservableListMain,tbl_Main,false));
        tbl_Main.setItems(spectacolObservableListMain);
    }
    @FXML
    void handleFilterSecond(ActionEvent event) {
        spectacolObservableList.setAll(filterHelper(datePick,artistCombo,spectacolObservableList,tbl,false));
        tbl.setItems(spectacolObservableList);
    }
    @FXML
    void handleLogOut(ActionEvent event) {
        logOut();
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }
    @FXML
    void handleLogOutMain(ActionEvent event) {
        logOut();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    @FXML
    void handleClose(ActionEvent event) {
        logOut();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void handleSell(ActionEvent event) {
        SpectacolDTO spectacol=tbl_Main.getSelectionModel().getSelectedItem();
        int index=tbl_Main.getSelectionModel().getSelectedIndex();
        if(index<0)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error","Select a show to sell tickets to!");
            return;
        }
        String clientName=txt_client_main.getText();
        int ticketValue=slide_ticket_main.getValue();
        try
        {
            handleSellCommon(clientName,spectacol,ticketValue);

        }
        catch(Exception e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void updateTableSecondary(SpectacolDTO spectacol1) {
        if(spectacol1!=null) {


            handleFilterSecond(null);
        }

    }

    private void updateTableMain(SpectacolDTO spectacol1) {
        if (spectacol1!=null)
        {

                handleFilter(null);
        }

    }

    @FXML
    void handleSellSecond(ActionEvent event) {
        handleSellCommon(txt_client.getText(), tbl.getSelectionModel().getSelectedItem(), slide_ticket.getValue());
        //handleFilterSecond(null);
       // handleFilter(null);
    }

    private SpectacolDTO handleSellCommon(String clientName, SpectacolDTO selectedItem, int ticketValue) {
        try {
            SpectacolDTO updated=service.addBilet(clientName, Long.parseLong(selectedItem.getId_spectacol()), ticketValue,this);
//            updateTableMain(updated);
//            updateTableSecondary(updated);
            return updated;
        } catch (FestivalException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "!", "Bilet vandut cu succes!");
        }
        return null;
    }
    private IFestivalServices service;
    private Stage stage;
    private Angajat    angajat;
    public void setAngajatController(IFestivalServices service, Stage stage, Angajat angajat)
    {
        this.showDetails.setVisible(false);
        this.service=service;
        this.angajat=angajat;
        this.stage=stage;
        this.datePick.setValue(LocalDate.now());
        setTableMain();
        try {
            loadTableMain();
        } catch (FestivalException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
        }
        try{
            setTableDate();
            loadTableDate();
            setCombo();
            setListeners();
            setSpinners();
        }catch (FestivalException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
        }
        //setTableDate();
    }
    private void loadTableDate() throws FestivalException
    {
        handleFilter(null);
    }
    private void setTableDate() {
        col_artist.setCellValueFactory(new PropertyValueFactory<SpectacolDTO,String>("artist"));
        col_available.setCellValueFactory(new PropertyValueFactory<SpectacolDTO,Integer>("nrLocuriDisp"));
        col_date.setCellValueFactory(new PropertyValueFactory<SpectacolDTO,String>("data"));
        col_place.setCellValueFactory(new PropertyValueFactory<SpectacolDTO,String>("locatie"));
        tbl.setRowFactory(tv -> new TableRow<SpectacolDTO>() {
            @Override
            protected void updateItem(SpectacolDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.getNrLocuriDisp().equals("0")) {
                    setStyle("-fx-background-color: red;");
                } else {
                    setStyle("");
                }
            }
        });


    }

    ObservableList<SpectacolDTO> spectacolObservableListMain= FXCollections.observableArrayList();
    ObservableList<SpectacolDTO> spectacolObservableList=FXCollections.observableArrayList();
    private void loadTableMain() throws FestivalException
    {
        List<SpectacolDTO> spectacols=null;
        spectacols= List.of(service.getSpectacole());

        spectacolObservableListMain.setAll(spectacols);
        tbl_Main.setItems(spectacolObservableListMain);

    }
    private void setTableMain() {


        col_artist_main.setCellValueFactory(new PropertyValueFactory<SpectacolDTO,String>("artist"));
        col_available_main.setCellValueFactory(new PropertyValueFactory<SpectacolDTO,Integer>("nrLocuriDisp"));
        col_date_main.setCellValueFactory(new PropertyValueFactory<SpectacolDTO,String>("data"));
        col_place_main.setCellValueFactory(new PropertyValueFactory<SpectacolDTO,String>("locatie"));
        col_sold_main.setCellValueFactory(new PropertyValueFactory<SpectacolDTO,Integer>("nrLocuriVandute"));
        tbl_Main.setRowFactory(tv -> new TableRow<SpectacolDTO>() {
            @Override
            protected void updateItem(SpectacolDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (Integer.valueOf(item.getNrLocuriDisp()) == 0) {
                    setStyle("-fx-background-color: red;");
                } else {
                    setStyle("");
                }
            }
        });

        tbl_Main.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDetails.setVisible(true);
                lbl_artist.setText(newSelection.getArtist());
                lbl_date.setText(newSelection.getData());
                lbl_place.setText(newSelection.getLocatie());
                SpinnerValueFactory.IntegerSpinnerValueFactory availableFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.valueOf(newSelection.getNrLocuriDisp()), 0);
                slide_ticket.setValueFactory(availableFactory);
            }
        });


    }

    @Override
    public void recievedSellingOfTicket(SpectacolDTO spectacol, int nrLocuri, String nume) throws FestivalException {
        System.out.println("I come here!");
        Platform.runLater(()->{
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sold ticket","Bilet vandut cu succes!");
            updateTableMain(spectacol);
            updateTableSecondary(spectacol);
        });
    }
    public void login(String username,String pass) throws FestivalException
    {
        Angajat angajat1=new Angajat("","",username,username,pass);
        service.logIn(angajat1.getUsername(),angajat1.getPassword(),this);
        angajat=angajat1;
        System.out.println("Autentificarea e ok!!!");
    }
    @Override
    public void userLoggedIn(String username, String password) throws FestivalException {
        Platform.runLater(() -> {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Login","Autentificare reusita!");
        });

    }

    @Override
    public void userLoggedOut(String username, String password) throws FestivalException {
        Platform.runLater(() -> {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Logout","Deconectare reusita!");
        });

    }

    @Override
    public void recievedSpectacole(SpectacolDTO[] spectacole) throws FestivalException {
        Platform.runLater(() -> {
            spectacolObservableListMain.setAll(spectacole);
            spectacolObservableList.setAll(spectacole);
            try {
                loadTableMain();
            } catch (FestivalException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INIT : am in lista spectacole+"+spectacolObservableList.size());

        System.out.println("END INIT!!!!!!!!!");
    }

    public void logOut() {
        try {
            service.logOut(angajat);
        } catch (FestivalException e) {
            System.out.println("Logout error " + e);
        }
    }
}
