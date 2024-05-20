package io.github.dnloop.pagination_javafx_sample.ui.home;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeView implements Initializable, FxmlView<HomeViewModel> {
    @FXML
    private Button btnClearTable;

    @FXML
    private Button btnLoadTable;

    @FXML
    private Label lbStatus;

    @FXML
    private BorderPane mainContent;

    @FXML
    private ProgressIndicator pgStatus;

    @FXML
    void clearTable() {
        homeViewModel.deleteTableCommand.execute();
    }

    @FXML
    void loadTable() {
        homeViewModel.loadTableCommand.execute();
    }

    @InjectViewModel
    private HomeViewModel homeViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert btnClearTable != null :
                "fx:id=\"btnClearTable\" was not injected: check your FXML file 'PatientTableView.fxml'.";
        assert btnLoadTable != null :
                "fx:id=\"btnLoadTable\" was not injected: check your FXML file 'PatientTableView.fxml'.";
        assert lbStatus != null : "fx:id=\"lbStatus\" was not injected: check your FXML file 'PatientTableView.fxml'.";
        assert mainContent != null :
                "fx:id=\"mainContent\" was not injected: check your FXML file 'PatientTableView.fxml'.";
        assert pgStatus != null : "fx:id=\"pgStatus\" was not injected: check your FXML file 'PatientTableView.fxml'.";

        mainContent.setCenter(homeViewModel.getPatientView());
        lbStatus.setOnMouseClicked(e -> lbStatus.setText(""));
        pgStatus.setVisible(false);
    }

    public void setStatus(String status) {
        lbStatus.setText(status);
    }

    public ProgressIndicator getProgressIndicator() {
        return pgStatus;
    }
}
