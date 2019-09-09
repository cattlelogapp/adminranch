package io.crf.cattlelog.aminranch.repository;

import io.crf.cattlelog.aminranch.domain.Ranch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ranch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RanchRepository extends JpaRepository<Ranch, Long> {

}
