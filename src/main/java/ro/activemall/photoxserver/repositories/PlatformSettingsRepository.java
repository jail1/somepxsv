package ro.activemall.photoxserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.activemall.photoxserver.entities.PlatformSetting;

public interface PlatformSettingsRepository extends
		JpaRepository<PlatformSetting, Long> {
	@Query("SELECT s FROM setting s WHERE s.key = :key")
	PlatformSetting getPropertyByName(@Param("key") String key);
}
