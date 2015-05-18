package ro.activemall.photoxserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.activemall.photoxserver.entities.PlatformSetting;
import ro.activemall.photoxserver.repositories.PlatformSettingsRepository;

@Service
public class PlatformSettingsService extends ApplicationAbstractService {
	@Autowired
	PlatformSettingsRepository repository;

	public PlatformSetting createOrUpdateSetting(PlatformSetting setting) {
		// TODO : validate - settings are unique key
		return repository.save(setting);
	}
}
