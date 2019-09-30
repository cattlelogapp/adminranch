package io.crf.cattlelog.aminranch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.crf.cattlelog.aminranch.domain.Ranch;
import io.crf.cattlelog.aminranch.service.dto.RanchAccessDTO;

/**
 * Spring Data repository for the Ranch entity.
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
//	@Query("select ranch from Ranch ranch left join ranch.rancher rancher where rancher.userId = :id")
//	List<Ranch> findAllByUserId(@Param("id") Integer id);

	@Query(value = "SELECT ranch.id, ranch.name, ranch.location, ranch.rancher_id " + "FROM ranch\n"
			+ "	LEFT JOIN consultant_ranch AS contract ON contract.ranch_id = ranch.id\n"
			+ "	LEFT JOIN consultant ON consultant.id = contract.consultant_id\n"
			+ "	LEFT JOIN rancher ON rancher.id = ranch.rancher_id\n"
			+ "WHERE rancher.user_id = :userId OR (consultant.user_id = :userId "
			+ "AND contract.status = 'ACTIVE')", nativeQuery = true)
	List<Ranch> findAllByUserId(@Param("userId") Integer userId);

	@Query(value = "SELECT ranch.id, ranch.name, ranch.location, ranch.rancher_id \n" + "FROM ranch\n"
			+ "	LEFT JOIN consultant_ranch AS contract ON contract.ranch_id = ranch.id\n"
			+ "	LEFT JOIN consultant ON consultant.id = contract.consultant_id\n"
			+ "	LEFT JOIN rancher ON rancher.id = ranch.rancher_id\n"
			+ "WHERE (rancher.user_id <> :userId OR  rancher.user_id IS NULL) \n"
			+ "	AND ( (contract.status <> 'ACTIVE' OR contract.status IS NULL) \n"
			+ "    OR (consultant.user_id <> :userId OR  consultant.user_id IS NULL))", nativeQuery = true)
	List<Ranch> findAllForAccessByUserId(@Param("userId") Integer userId);

	@Modifying
	@Query(value = "INSERT INTO consultant_ranch (ranch_id, consultant_id, status) values (:ranchId, :consultantId, 'NEW')", nativeQuery = true)
	void registerAccess(@Param("ranchId") Integer ranchId, @Param("consultantId") Integer consultantId);

	@Query(value = "SELECT * FROM consultant_ranch WHERE ranch_id = :ranchId AND consultant_id = :consultantId", nativeQuery=true)
	Object findAccess(@Param("ranchId") Integer ranchId, @Param("consultantId") Integer consultantId);
}
