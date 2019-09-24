package io.crf.cattlelog.aminranch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.crf.cattlelog.aminranch.domain.Ranch;


/**
 * Spring Data  repository for the Ranch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RanchRepository extends JpaRepository<Ranch, Long> {
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	List<Ranch> findAllByRancherId(Long id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Query("select ranch from Ranch ranch left join ranch.rancher rancher where rancher.userId = :id")
	List<Ranch> findAllByUserId(@Param("id") Integer id);
}
