package ro.activemall.photoxserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.activemall.photoxserver.entities.ResourceTagName;

public interface TagsRepository extends JpaRepository<ResourceTagName, Long> {
	// dummy - to be removed when added another method
	@Query("SELECT tn FROM tag_name tn WHERE tn.id = :id")
	List<ResourceTagName> get(@Param("id") Long id);
}
