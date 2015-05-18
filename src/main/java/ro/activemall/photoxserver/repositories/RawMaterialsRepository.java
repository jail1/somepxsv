package ro.activemall.photoxserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.activemall.photoxserver.entities.RawMaterial;

public interface RawMaterialsRepository extends
		JpaRepository<RawMaterial, Long> {

}
