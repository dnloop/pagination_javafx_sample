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
            log.info("Load table");
            patientTuple.getViewModel().getLoadTableCommand().execute();
        }
    });

    public final Command deleteTableCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            log.info("Delete table");
            patientTuple.getViewModel().getDeleteTableCommand().execute();
        }
    });

    public Command updateListCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            log.debug("Update list");
            patientTuple.getViewModel().getUpdateListCommand().execute();
        }
    });


    public Parent getPatientView() {
        return patientTuple.getView();
    }

    public void setHomeViewReference(HomeView homeView) {
        patientTuple.getViewModel().setHomeViewReference(homeView);
    }
}
