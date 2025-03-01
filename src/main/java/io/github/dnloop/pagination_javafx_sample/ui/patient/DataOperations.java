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

import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import io.github.dnloop.pagination_javafx_sample.model.Patient;
import io.github.dnloop.pagination_javafx_sample.service.PatientService;
import io.github.dnloop.pagination_javafx_sample.support.BeanUtil;
import io.github.dnloop.pagination_javafx_sample.support.PageableUtility;
import io.github.dnloop.pagination_javafx_sample.ui.home.HomeView;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * It has methods to perform database operations as well as data generation.
 * This could be a way to separate database operations from ViewModel in order to make it more easy
 * to maintain. I need to use another class to deal only with pagination, since it is a common operation
 * in several views with different models I can use an abstract class with common methods.
 * 1º stage: Use this class as is, see if any wrong assumptions arises.
 * 2º stage: Create a separate class for pagination.
 * There is a third use case that requires a search filter, this could be done in another class. But maybe have it
 * in this class? Not all lists require pagination but they do have a simple filtering method for underlying
 * data source.
 */
@Component
public class DataOperations {
    private final Log log = LogFactory.getLog(DataOperations.class);

    public DataOperations() {
        Platform.runLater(() -> confirmDialog = new Alert(Alert.AlertType.CONFIRMATION));
    }

    private final PatientService service = BeanUtil.getBean(PatientService.class);

    private Alert confirmDialog;

    private final ObservableList<Patient> patientsList = FXCollections.observableArrayList();

    private Pagination pagination;

    private HomeView homeView;

    /**
     * Each task is used to bind its status to a progress indicator and a status message.
     * In this current development stage it is a simple label that requires to be clicked in order
     * to hide its last message. A more complex approach could be connect to a notification service
     * with default timings.
     */
    private Task<Void> loadTableTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                var tableLoad = service.loadTable().exceptionally(e -> {
                    log.error("Failed to load patients: " + e.getMessage());
                    Platform.runLater(() -> onTaskCleanup("Table loading failed"));
                    return null;
                }).thenRun(() -> Platform.runLater(() -> {
                    log.debug("Unbind progress indicator [loadTableTask]");
                    onTaskCleanup("Table loaded");
                }));

                try {
                    log.info("Loading table");
                    tableLoad.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Failed to load patients: " + e.getMessage());
                    log.debug(e.getMessage(), e);
                }

                return null;
            }
        };
    }

    /**
     * Once the task is completed it needs to be cleaned up by unbinding the progress indicator and
     * setting the status from previous task.
     */
    private void onTaskCleanup(String status) {
        homeView.getProgressIndicator().progressProperty().unbind();
        homeView.getProgressIndicator().setVisible(false);
        homeView.setStatus(status);
    }

    public final Command loadTableCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            log.debug("[loadTableCommand] Bind progress indicator and start thread");
            bindProgressIndicator(loadTableTask().progressProperty());
            new Thread(loadTableTask()).start();
        }
    });

    /**
     * Binds a progress indicator to a task property before executing the task.
     */
    private void bindProgressIndicator(ReadOnlyDoubleProperty progressProperty) {
        // bind progress indicator
        homeView.getProgressIndicator().progressProperty().unbind(); // just in case of multiple calls
        homeView.getProgressIndicator().progressProperty().bind(progressProperty);
        homeView.getProgressIndicator().setVisible(true);
    }

    private Task<Void> deleteTableTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                var deleteTable = service.deleteAll().exceptionally(e -> {
                    log.error("Failed to delete table: " + e.getMessage());
                    Platform.runLater(() -> onTaskCleanup("Table delete failed"));
                    return null;
                }).thenRun(() -> Platform.runLater(() -> {
                    log.debug("Unbind progress indicator [deleteTableTask]");
                    patientsList.clear();
                    pagination.setPageCount(1);
                    onTaskCleanup("Table deleted");
                }));

                try {
                    log.info("Deleting table");
                    deleteTable.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Failed to delete table: " + e.getMessage());
                    log.debug(e.getMessage(), e);
                }

                return null;
            }
        };
    }

    public final Command deleteTableCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            log.debug("Display confirm dialog: delete table");
            var result = confirmDialog.showAndWait();
            var filter = result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
            if (filter) {
                log.debug("[deleteTableCommand] Bind progress indicator and start thread");
                // bind progress indicator
                bindProgressIndicator(deleteTableTask().progressProperty());
                new Thread(deleteTableTask()).start();
            }
        }
    });

    public ObservableList<Patient> getPatientsList() { return patientsList; }

    public void setPagination(Pagination pagination) {
        pagination.currentPageIndexProperty().addListener(unfilteredListener);
        this.pagination = pagination;
    }

    private Integer currentPage = 0;

    private String query;

    /**
     * Default pagination change listener available when the user first opens the view.
     * This allows to click on new page index to search each page.
     */
    private final ChangeListener<Number> unfilteredListener =
            (observable, oldValue, newValue) -> createPage(newValue.intValue(), query);

    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    /**
     * Create page with 50 elements. This is used for both types of pagination (unfiltered / filtered).
     */
    private void createPage(int index, String query) {
        log.debug("Current page: " + index);
        currentPage = index;
        if (query == null || query.isEmpty()) {
            log.debug("No filter: " + query);
            updateList.execute();
        } else {
            log.debug("Filtering: " + query);
            this.query = query;
            filterList.execute();
        }
    }

    private final Command filterList = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            CompletableFuture<Void> filtered =
                    service.findByName(query, PageableUtility.of(currentPage, "name"))
                           .thenAccept(result -> {
                               if (result.hasContent()) {
                                   log.debug("Filtered list size: " + result.getContent().size());
                                   log.debug("Filtered total pages: " + result.getTotalPages());
                                   log.debug("Filtered total patients: " + result.getTotalElements());
                                   Platform.runLater(() -> {
                                       patientsList.setAll(result.getContent());
                                       pagination.setPageCount(result.getTotalPages());
                                   });
                               } else {
                                   Platform.runLater(() -> {
                                       homeView.setStatus("No patients found");
                                       query = null;
                                       patientsList.clear();
                                       pagination.setPageCount(1);
                                   });
                               }
                           });

            try {
                filtered.get();
                log.info("Filtered patients  loaded");
            } catch (InterruptedException | ExecutionException e) {
                log.error("Failed to load patients");
                log.debug(e.getMessage(), e);
            }
        }
    }, true);

    /**
     * Used to query observed value on text field, it can be used also as a stand-alone action.
     * It sets the current page to 0 and performs a filtered search.
     */
    public void searchPatient(String text) {
        log.debug("Called searchPatient: " + text);
        createPage(0, text);
    }

    private final Command updateList = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            log.info("Loading patients");
            CompletableFuture<Void> patients =
                    service.findAll(PageableUtility.of(currentPage, "name"))
                           .thenAccept(result -> {
                               if (result.hasContent()) {
                                   log.debug("Patients list size: " + result.getContent().size());
                                   log.debug("Patients total pages: " + result.getTotalPages());
                                   log.debug("Total patients: " + result.getTotalElements());
                                   Platform.runLater(() -> {
                                       patientsList.setAll(result.getContent());
                                       pagination.setPageCount(result.getTotalPages());
                                   });
                               }
                           });

            try {
                patients.get();
                log.info("Patients loaded");
            } catch (InterruptedException | ExecutionException e) {
                log.error("Failed to load patients");
                log.debug(e.getMessage(), e);
            }
        }
    }, true);

    /**
     * It loads initial data and subsequent updates from database.
     */
    public Command getUpdateListCommand() { return updateList; }
}