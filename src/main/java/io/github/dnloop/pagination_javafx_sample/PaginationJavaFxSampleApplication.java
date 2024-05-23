package io.github.dnloop.pagination_javafx_sample;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import io.github.dnloop.pagination_javafx_sample.ui.home.HomeView;
import io.github.dnloop.pagination_javafx_sample.ui.home.HomeViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.github.dnloop.pagination_javafx_sample")
@EnableJpaRepositories("io.github.dnloop.pagination_javafx_sample.repository")
@EntityScan("io.github.dnloop.pagination_javafx_sample.model")
public class PaginationJavaFxSampleApplication extends Application {

    private static final Log log = LogFactory.getLog(PaginationJavaFxSampleApplication.class);

    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        launch(PaginationJavaFxSampleApplication.class, args);
    }

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .sources(PaginationJavaFxSampleApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Home view must be prepared here to ensure it has a reference to the stage
        log.info("[ Loading main application layout ]");
        ViewTuple<HomeView, HomeViewModel> viewTuple =
                FluentViewLoader.fxmlView(HomeView.class).load();

        primaryStage.setTitle(" -·=[ PaginationJavaFxSample ]=·-");
        primaryStage.setScene(new Scene(viewTuple.getView()));
        viewTuple.getViewModel().setHomeViewReference(viewTuple.getCodeBehind());
        viewTuple.getCodeBehind().setStatus("Ready");
        log.info("[ Application running ]");
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
        log.info("[ Application Terminated Successfully ]");
    }
}
