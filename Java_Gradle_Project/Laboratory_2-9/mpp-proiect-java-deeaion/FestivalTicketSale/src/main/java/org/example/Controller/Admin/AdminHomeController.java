package org.example.Controller.Admin;

import javafx.stage.Stage;
import org.example.Service.FestivalService;

public class AdminHomeController {
    private FestivalService service;
    private Stage stage;
    public void setHome(FestivalService service, Stage stage) {
        this.service=service;
        this.stage=stage;
    }
}
