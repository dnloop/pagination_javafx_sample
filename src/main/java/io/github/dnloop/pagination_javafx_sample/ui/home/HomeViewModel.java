package io.github.dnloop.pagination_javafx_sample.ui.home;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import io.github.dnloop.pagination_javafx_sample.ui.patient.PatientTableView;
import io.github.dnloop.pagination_javafx_sample.ui.patient.PatientTableViewModel;
import javafx.scene.Parent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HomeViewModel implements ViewModel {

    private static final Log log = LogFactory.getLog(HomeViewModel.class);

    private final ViewTuple<PatientTableView, PatientTableViewModel> patientTuple =
            FluentViewLoader.fxmlView(PatientTableView.class).load();

    public final Command loadTableCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() {
                log.debug("Load table");
                patientTuple.getViewModel().loadTableCommand.execute();
            }
        }, true);

    public final Command deleteTableCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() {
                log.debug("Delete table");
                patientTuple.getViewModel().deleteTableCommand.execute();
            }
        });


    public Parent getPatientView() {
        return patientTuple.getView();
    }

    public void setHomeViewReferece(HomeView homeView) {
        patientTuple.getViewModel().setHomeViewReference(homeView);
    }
}
