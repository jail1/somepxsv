package ro.activemall.photoxserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.activemall.photoxserver.entities.ResourceTag;

public interface ResourcesTagsRepository extends
		JpaRepository<ResourceTag, Long> {
	// dummy - remove it when added something else
	@Query("SELECT t FROM resource_tag t WHERE t.id = :id")
	List<ResourceTag> get(@Param("id") Long id);
}
