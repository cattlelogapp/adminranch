package io.crf.cattlelog.aminranch.repository;

import io.crf.cattlelog.aminranch.domain.Consultant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Consultant entity.
 */
@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

    @Query(value = "select distinct consultant from Consultant consultant left join fetch consultant.ranches",
        countQuery = "select count(distinct consultant) from Consultant consultant")
    Page<Consultant> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct consultant from Consultant consultant left join fetch consultant.ranches")
    List<Consultant> findAllWithEagerRelationships();

    @Query("select consultant from Consultant consultant left join fetch consultant.ranches where consultant.id =:id")
    Optional<Consultant> findOneWithEagerRelationships(@Param("id") Long id);

}
