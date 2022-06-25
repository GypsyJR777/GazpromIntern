package main.repository;

import main.model.WikiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WikiRepository extends JpaRepository<WikiModel, Integer> {
    WikiModel findByTitle(String title);
}
