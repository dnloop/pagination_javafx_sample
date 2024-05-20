package io.github.dnloop.pagination_javafx_sample.ui.patient;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import io.github.dnloop.pagination_javafx_sample.model.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PatientTableView implements Initializable, FxmlView<PatientTableViewModel> {

    @FXML
    private Pagination pagination;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Patient> table;

    @FXML
    private TableColumn<Patient, String> colName;

    @InjectViewModel
    private PatientTableViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert colName != null : "fx:id=\"colName\" was not injected: check your FXML file 'PatientTableView.fxml'.";
        assert pagination != null :
                "fx:id=\"pagination\" was not injected: check your FXML file 'PatientTableView.fxml'.";
        assert searchField != null :
                "fx:id=\"searchField\" was not injected: check your FXML file 'PatientTableView.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'PatientTableView.fxml'.";
    }
}
