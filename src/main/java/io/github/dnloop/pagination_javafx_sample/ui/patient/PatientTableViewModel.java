package io.github.dnloop.pagination_javafx_sample.ui.patient;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import io.github.dnloop.pagination_javafx_sample.model.Patient;
import io.github.dnloop.pagination_javafx_sample.service.PatientService;
import io.github.dnloop.pagination_javafx_sample.ui.home.HomeView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class PatientTableViewModel implements ViewModel {
    private final Log log = LogFactory.getLog(PatientTableViewModel.class);

    private static final int PAGE_SIZE = 50;

    private final PatientService service;

    private final Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);

    final ObservableList<Patient> patientsList = FXCollections.observableArrayList();

    private HomeView homeView;

    public ObservableList<Patient> getPatientsList() { return patientsList; }

    public final SimpleBooleanProperty updatedProperty = new SimpleBooleanProperty(false);

    private Pagination pagination;

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * Create page with {@value PAGE_SIZE } elements. This is used for pagination without filtering.
     */
    private void createPage(int i) {
        int from = i * PAGE_SIZE;
        int to = from + PAGE_SIZE;
        ObservableList<Patient> subList = FXCollections.observableArrayList();
        subList.addAll(patientsList.subList(from, to));
        patientsList.addAll(subList);
    }

    @Autowired
    public PatientTableViewModel(PatientService service) {
        this.service = service;
    }

    private final Task<Void> loadTableTask = new Task<>() {

        @Override
        protected Void call() {
            try {
                service.loadTable().get();
                log.info("Table loaded");
            } catch (InterruptedException | ExecutionException e) {
                log.error("Failed to load patients");
                log.debug(e.getMessage(), e);
            }
            return null;
        }


        @Override
        protected void succeeded() {
            homeView.setStatus("Table loaded");
            homeView.getProgressIndicator().setVisible(false);
            homeView.getProgressIndicator().progressProperty().unbind();
        }
    };

    public final Command loadTableCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            log.debug("Load table");
            homeView.getProgressIndicator().progressProperty().bind(loadTableTask.progressProperty());
            homeView.getProgressIndicator().setVisible(true);
            new Thread(loadTableTask).start();
        }
    });

    public final Command deleteTableCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            log.debug("Display confirm dialog: delete table");
            var result = confirmDialog.showAndWait();
            var filter = result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
            if (filter) {
                log.debug("Delete table");
                homeView.setStatus("Table deleted");
                service.deleteAll();
            }
        }
    });

    /**
     * Set reference to {@link HomeView}. This allows to access the status and
     * progress indicator.
     */
    public void setHomeViewReference(HomeView homeView) {
        this.homeView = homeView;
    }
}
