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

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import io.github.dnloop.pagination_javafx_sample.model.Patient;
import io.github.dnloop.pagination_javafx_sample.ui.home.HomeView;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;


/**
 * I tried separating the view model from data operations but it didn't work well. It ended up being
 * a simple getter / setter. This means there is too much coupling between view model and data operations.
 */
public class PatientTableViewModel implements ViewModel {
    public final DataOperations dataOperations = new DataOperations();

    public ObservableList<Patient> getPatientsList() { return dataOperations.getPatientsList(); }

    public void setPagination(Pagination pagination) {
        dataOperations.setPagination(pagination);
    }

    /**
     * Set reference to {@link HomeView}. This allows to access the status and
     * progress indicator.
     */
    public void setHomeViewReference(HomeView homeView) {
        dataOperations.setHomeView(homeView);
    }

    public Command getLoadTableCommand() { return dataOperations.loadTableCommand; }

    public Command getDeleteTableCommand() { return dataOperations.deleteTableCommand; }

    public Command getUpdateListCommand() { return dataOperations.getUpdateListCommand(); }

    public void searchPatient(String text) { dataOperations.searchPatient(text); }
}
