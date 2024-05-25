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
    private Button btnUpdateList;

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

    @FXML
    void updateList() { homeViewModel.updateListCommand.execute(); }

    @InjectViewModel
    private HomeViewModel homeViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert btnClearTable != null :
                "fx:id=\"btnClearTable\" was not injected: check your FXML file 'PatientTableView.fxml'.";
        assert btnLoadTable != null :
                "fx:id=\"btnLoadTable\" was not injected: check your FXML file 'PatientTableView.fxml'.";
        assert btnUpdateList != null :
                "fx:id=\"btnUpdateList\" was not injected: check your FXML file 'PatientTableView.fxml'.";
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
