package io.crf.cattlelog.aminranch.repository;

import io.crf.cattlelog.aminranch.domain.Rancher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Rancher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RancherRepository extends JpaRepository<Rancher, Long> {

}
