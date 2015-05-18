package ro.activemall.photoxserver.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.activemall.photoxserver.entities.UserLogActivity;

public interface UsersLoggedActivitiesRepository extends
		JpaRepository<UserLogActivity, Long> {

	@Query("SELECT l FROM user_log_activity l WHERE l.targetUser.id = :userId ORDER BY l.eventDate DESC")
	List<UserLogActivity> getLogsForUser(@Param("userId") Long userId);

	@Query("SELECT COUNT(l) FROM user_log_activity l WHERE l.targetUser.id = :userId ORDER BY l.eventDate DESC")
	Long countLogsForUser(@Param("userId") Long userId);

	// TODO : create APIs
	@Transactional
	@Modifying
	@Query(value = "TRUNCATE TABLE user_log_activity", nativeQuery = true)
	void clear();
}