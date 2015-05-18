package ro.activemall.photoxserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.activemall.photoxserver.entities.Template;

public interface TemplatesRepository extends JpaRepository<Template, Long> {

}
