package io.github.dnloop.pagination_javafx_sample.service;

import io.github.dnloop.pagination_javafx_sample.model.Patient;
import io.github.dnloop.pagination_javafx_sample.repository.PatientRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.instancio.Select.field;

/**
 * It has methods to perform database operations as well as data generation.
 */

@Service
public class PatientService {
    private final PatientRepository repository;

    private final Log log = LogFactory.getLog(PatientService.class);

    @Autowired
    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<Page<Patient>> findAll(Pageable pageable) {
        log.debug("Page number: " + pageable.getPageNumber());
        log.debug("Page size: " + pageable.getPageSize());
        return CompletableFuture.completedFuture(repository.findAll(pageable));
    }

    @Async
    public CompletableFuture<Page<Patient>> findByName(String name, Pageable pageable) {
        log.debug("Page number: " + pageable.getPageNumber());
        log.debug("Page size: " + pageable.getPageSize());
        return CompletableFuture.completedFuture(repository.findByNameContainingIgnoreCaseOrderByName(name, pageable));
    }

    @Async
    public CompletableFuture<Void> deleteAll() {
        log.debug("Delete all patients");
        return CompletableFuture.runAsync(repository::deleteAll);
    }

    @Async
    public CompletableFuture<Void> loadTable() {
        log.debug("Insert all patients (1000)");
        List<Patient> patients = Instancio.ofList(Patient.class)
                                          .size(1000)
                                          .generate(field(Patient::getName), gen -> gen.string().length(3, 60))
                                          .ignore(field(Patient::getId))
                                          .create();
        return CompletableFuture.runAsync(() -> repository.saveAll(patients));
    }
}
