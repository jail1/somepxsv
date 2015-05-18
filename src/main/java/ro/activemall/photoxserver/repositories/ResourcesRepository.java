package ro.activemall.photoxserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.activemall.photoxserver.entities.Resource;
import ro.activemall.photoxserver.entities.User;

public interface ResourcesRepository extends JpaRepository<Resource, Long> {

	@Query("SELECT COUNT(r) FROM resource r WHERE r.owner = :owner AND type = 0")
	Long countOwnedResources(@Param("owner") User owner);

	@Query("SELECT r FROM resource r WHERE r.owner = :owner AND type = 0")
	List<Resource> findResourcesOwnedByAuthenticatedUser(
			@Param("owner") User owner);
}
