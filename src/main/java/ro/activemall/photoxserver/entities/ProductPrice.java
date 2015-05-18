package ro.activemall.photoxserver.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "price")
@Table(name = "photox_products_prices")
public class ProductPrice extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@OneToOne
	@JoinColumn(name = "rawmaterial_id")
	private RawMaterial material;
	
	@Column(name = "selling_price", precision = 10, length = 2)
	private double sellingPrice;
	
	@JsonIgnore
	public Product getProduct() {
		return product;
	}

	@JsonIgnore
	public void setProduct(Product product) {
		this.product = product;
	}	
	@JsonIgnore
	public RawMaterial getMaterial() {
		return material;
	}
	@JsonIgnore
	public void setMaterial(RawMaterial material) {
		this.material = material;
	}
	
	public Long getMaterialId(){
		return getMaterial().getId();
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	
	@JsonIgnore
	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\tId: ").append(getId()).append("\n");		
		builder.append("\tMaterial Id: ").append(getMaterialId()).append("\n");
		builder.append("\tSelling price (EUR): ").append(getSellingPrice()).append("\n");
		return builder.toString();
	}
}