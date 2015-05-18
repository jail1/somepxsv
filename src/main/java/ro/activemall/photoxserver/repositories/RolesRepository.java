package ro.activemall.photoxserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.activemall.photoxserver.entities.Role;

public interface RolesRepository extends JpaRepository<Role, Long> {
	// TODO : learn SQL like this one
	// public interface MessageRepository extends CrudRepository<Message, Long>{
	// @Query("SELECT m FROM message m where m.to.id = ?#{hasRole('ROLE_ADMIN') ? '%' : principal.id}")
	// Iterable<Message> findAll();
	// }
	// which returns all messages if it's and admin, or only for that principal
	// if it's not
	// In the future, watch for @EnableAclSecurity - which is going to keep it
	// easier
}