package io.github.dnloop.pagination_javafx_sample.ui.patient;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import io.github.dnloop.pagination_javafx_sample.model.Patient;
import io.github.dnloop.pagination_javafx_sample.ui.home.HomeView;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;


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
