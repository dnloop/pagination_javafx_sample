package io.github.dnloop.pagination_javafx_sample.repository;

import io.github.dnloop.pagination_javafx_sample.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer>,
        PagingAndSortingRepository<Patient, Integer> {

    List<Patient> findByNameContainingIgnoreCaseOrderByName(String name);
    Page<Patient> findByNameContainingIgnoreCaseOrderByName(String name, Pageable pageable);
}
