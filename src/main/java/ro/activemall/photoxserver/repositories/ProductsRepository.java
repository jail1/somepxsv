package ro.activemall.photoxserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.activemall.photoxserver.entities.Product;

public interface ProductsRepository extends JpaRepository<Product, Long> {
	@Query("SELECT p FROM product p WHERE p.isListable = 0 AND parent_id IS NULL AND p.id <> :exceptId AND p.isComplex = 0")
	public List<Product> getAllUnlistedProducts(@Param("exceptId") Long exceptId);
	
	@Query("SELECT p FROM product p WHERE p.isListable = 1")
	public List<Product> getListedProducts();
}
