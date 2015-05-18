package ro.activemall.photoxserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.activemall.photoxserver.entities.User;

public interface UsersRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM user u WHERE u.username = :username OR u.loginName = :username")
	User findByUsernameOrLoginName(@Param("username") String username);

	@Query("SELECT u FROM user u WHERE u.username = :username")
	User findByUsername(@Param("username") String username);
	
	@Query("SELECT COUNT(u) FROM user u WHERE u.username = :username")
	Long countUsersWithUsername(@Param("username") String username);
	
	@Query("SELECT COUNT(u) FROM user u WHERE u.loginName = :loginName")
	Long countUsersWithLoginName(@Param("loginName") String loginName);
}