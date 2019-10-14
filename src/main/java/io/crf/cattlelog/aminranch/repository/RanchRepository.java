package io.crf.cattlelog.aminranch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.crf.cattlelog.aminranch.domain.Ranch;
import io.crf.cattlelog.aminranch.service.dto.RanchAccessDTO;
import io.crf.cattlelog.aminranch.service.dto.RanchWithAccessDTO;

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

	@Query(value = "SELECT DISTINCT ranch.id, ranch.name, ranch.location, ranch.rancher_id " + "FROM ranch\n"
			+ "	LEFT JOIN consultant_ranch AS contract ON contract.ranch_id = ranch.id\n"
			+ "	LEFT JOIN consultant ON consultant.id = contract.consultant_id\n"
			+ "	LEFT JOIN rancher ON rancher.id = ranch.rancher_id\n"
			+ "WHERE rancher.user_id = :userId OR (consultant.user_id = :userId "
			+ "AND contract.status = 'ACTIVE')", nativeQuery = true)
	List<Ranch> findAllByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT new io.crf.cattlelog.aminranch.service.dto.RanchWithAccessDTO(r.id, r.name, t.status, c.userId) \n"
			+ "	FROM Ranch r \n" + "		LEFT JOIN Contract t ON r.id = t.ranch.id \n"
			+ "		LEFT JOIN Consultant c ON t.consultant.id = c.id \n"
			+ "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :ranchName,'%'))")
	List<RanchWithAccessDTO> findAllForConsultantWithAccessByRanchName(@Param("ranchName") String ranchName);
	
	@Query(value = "SELECT new io.crf.cattlelog.aminranch.service.dto.RanchWithAccessDTO(r.id, r.name, t.status, c.userId) \n"
			+ "	FROM Ranch r \n" + "		LEFT JOIN Contract t ON r.id = t.ranch.id \n"
			+ "		LEFT JOIN Consultant c ON t.consultant.id = c.id \n")
	List<RanchWithAccessDTO> findAllForConsultantWithAccess();

	@Query(value = "SELECT new io.crf.cattlelog.aminranch.service.dto.RanchWithAccessDTO(r.id, r.name, t.status, c.userId) \n"
			+ "	FROM Ranch r \n" + "		LEFT JOIN Contract t ON r.id = t.ranch.id \n"
			+ "		LEFT JOIN Consultant c ON t.consultant.id = c.id \n"
			+ "		LEFT JOIN Rancher a ON a.id = r.rancher.id \n"
			+ "	WHERE (t.status IS NOT NULL AND a.userId = :userId)")
	List<RanchWithAccessDTO> findAllForRancherWithAccessByUserId(@Param("userId") Long userId);

	@Modifying
	@Query(value = "INSERT INTO consultant_ranch (ranch_id, consultant_id, status) values (:ranchId, :consultantId, 'NEW')", nativeQuery = true)
	void registerAccess(@Param("ranchId") Long ranchId, @Param("consultantId") Long consultantId);

	@Modifying
	@Query(value = "UPDATE consultant_ranch SET status = 'ACTIVE' WHERE ranch_id = :ranchId AND consultant_id = :consultantId", nativeQuery = true)
	void grantAccess(@Param("ranchId") Long ranchId, @Param("consultantId") Long consultantId);
	
	@Modifying
	@Query(value = "DELETE FROM consultant_ranch WHERE ranch_id = :ranchId AND consultant_id = :consultantId", nativeQuery = true)
	void removeAccess(@Param("ranchId") Long ranchId, @Param("consultantId") Long consultantId);

	@Query(value = "SELECT * FROM consultant_ranch WHERE ranch_id = :ranchId AND consultant_id = :consultantId", nativeQuery = true)
	Object findAccess(@Param("ranchId") Long ranchId, @Param("consultantId") Long consultantId);

	@Modifying
	@Query(value = "DELETE FROM consultant_ranch WHERE ranch_id = :ranchId AND consultant_id = :consultantId", nativeQuery = true)
	void deleteAccess(@Param("ranchId") Long ranchId, @Param("consultantId") Long consultantId);

}
