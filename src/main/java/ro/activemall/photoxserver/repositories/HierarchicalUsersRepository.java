package ro.activemall.photoxserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.activemall.photoxserver.entities.HierarchicalUser;

public interface HierarchicalUsersRepository extends JpaRepository<HierarchicalUser, Long> {

}
