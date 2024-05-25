/*
 * MIT License
 *
 * Copyright (c) 2024 Emi Rodriguez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.dnloop.pagination_javafx_sample.ui.patient;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import io.github.dnloop.pagination_javafx_sample.model.Patient;
import javafx.beans.property.ReadOnlyStringWrapper;
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

        viewModel.setPagination(pagination);
        table.setItems(viewModel.getPatientsList());
        colName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        // keep an eye out on this method. It needs debouncing
        searchField.textProperty().addListener((observable, oldValue, newValue) -> viewModel.searchPatient(newValue));
    }

    @FXML
    public void searchPatient() {
        viewModel.searchPatient(searchField.getText());
    }
}
