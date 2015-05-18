package ro.activemall.photoxserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.activemall.photoxserver.entities.ProductPrice;

public interface ProductPricesRepository extends JpaRepository<ProductPrice, Long> {
	@Query("SELECT p FROM price p WHERE p.product.id = :productId AND p.material.id = :materialId")
	ProductPrice findPriceForProductAndMaterial(@Param("productId") Long productId, @Param("materialId") Long materialId);
}
