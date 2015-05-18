package ro.activemall.photoxserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ro.activemall.photoxserver.entities.RawMaterialGroup;

public interface RawMaterialGroupsRepository extends JpaRepository<RawMaterialGroup, Long> {
	@Query("SELECT g FROM rawmaterialgroup g WHERE g.requiredInEveryProduct = 1")
	List<RawMaterialGroup> findRequiredInEveryProductMaterials();
}
