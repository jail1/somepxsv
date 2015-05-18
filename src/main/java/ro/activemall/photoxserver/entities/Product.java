package ro.activemall.photoxserver.entities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity(name = "product")
@Table(name = "photox_products")
public class Product extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "is_listable", columnDefinition = "BIT", length = 1)
	@Basic
	private boolean isListable;

	@Column(name = "is_complex", columnDefinition = "BIT", length = 1)
	@Basic
	private boolean isComplex;
	// name of the product, e.g. album 30x20 cm, photo 30x20 cm
	@Column(name = "name")
	private String name;
	// description
	@Column(name = "description")
	private String description;
	// holds data to calculate ratio, e.g. w=0.3m;h=0.2m
	@Column(name = "extra_data")
	private String extraData;

	@Column(name = "lost_ratio", precision = 10, length = 2)
	private double lostRatio;
	// the ratio this product - is hidden from user, allows to calculate price
	// using generic measuring unit. e.g. ratio 0.6 => productPrice =
	// SUM(sellingPrice of all rawMaterials in selector) * 0.6
	@Column(name = "ratio", precision = 10, length = 2)
	private double ratio;
	// minimum allowed quantity to be selected by end-user
	// for products like albums, this is greater than one and used in
	// productPrice
	// effective quantity is set in the Invoice product
	@Column(name = "min_quantity")
	private int minQuantity = 1;

	// contains all the information required to calculate dynamic productPrice,
	// in a tree of rawMaterials
	// to better understanding, a product is like a recipe which has this
	// ingredients
	// If instead of Set a List is declared, the selectors will duplicate
	@JsonInclude(Include.NON_EMPTY)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "photox_products_selectors", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "rawmaterialgroup_id"))
	private Set<RawMaterialGroup> selectors;
	//TODO : attempt to change this into a SET
	@JsonInclude(Include.NON_EMPTY)
	@OneToMany(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER, orphanRemoval = false, targetEntity = Product.class)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name = "parent_id", nullable = true)
	private Set<Product> children;
	@JsonInclude(Include.NON_EMPTY)
	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, targetEntity = ProductPrice.class)
	private Set<ProductPrice> prices;

	@JsonInclude(Include.NON_EMPTY)
	public Set<Product> getChildren() {
		return children;
	}

	@JsonInclude(Include.NON_EMPTY)
	public void setChildren(Set<Product> children) {
		this.children = children;
	}

	public boolean getIsListable() {
		return isListable;
	}

	public void setListable(boolean isListable) {
		this.isListable = isListable;
	}

	public boolean getIsComplex() {
		return isComplex;
	}

	public void setIsComplex(boolean isComplex) {
		this.isComplex = isComplex;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public double getLostRatio() {
		return lostRatio;
	}

	public void setLostRatio(double lostRatio) {
		this.lostRatio = lostRatio;
	}

	public int getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(int min_quantity) {
		this.minQuantity = min_quantity;
	}

	@JsonInclude(Include.NON_EMPTY)
	public Set<RawMaterialGroup> getSelectors() {
		return selectors;
	}

	@JsonInclude(Include.NON_EMPTY)
	public void setSelectors(Set<RawMaterialGroup> selectors) {
		this.selectors = selectors;
	}

	@JsonInclude(Include.NON_EMPTY)
	public Set<ProductPrice> getPrices() {
		return prices;
	}

	@JsonInclude(Include.NON_EMPTY)
	public void setPrices(Set<ProductPrice> prices) {
		this.prices = prices;
	}

	@JsonIgnore
	@Transient
	public InvoiceProduct cloneForInvoice(Product product) {
		InvoiceProduct invProd = new InvoiceProduct();
		invProd.setName(this.name);
		// TODO : update this
		//TODO : clone prices
		return invProd;
	}

	@JsonIgnore
	@Transient
	public Product cloneForProduct() {
		Product result = new Product();
		result.setListable(false);
		result.setIsComplex(getIsComplex());
		result.setName(getName());
		result.setDescription(getDescription());
		result.setExtraData(getExtraData());
		result.setRatio(getRatio());
		result.setLostRatio(getLostRatio());
		result.setMinQuantity(getMinQuantity());
		if (getIsComplex()) {
			for (Iterator<Product> iterator = getChildren().iterator(); iterator.hasNext();) {
				Product childProduct = (Product) iterator.next();
				if (result.getChildren() == null) {
					result.setChildren(new HashSet<Product>());
				}
				result.getChildren().add(childProduct.cloneForProduct());
			}
		}else{
			result.setName("[Clona] " + getName());
			for (Iterator<RawMaterialGroup> iterator = getSelectors().iterator(); iterator.hasNext();){
				RawMaterialGroup group = (RawMaterialGroup) iterator.next();
				if (result.getSelectors() == null){
					result.setSelectors(new HashSet<RawMaterialGroup>());					
				}
				result.getSelectors().add(group);
			}
			for (Iterator<ProductPrice> iterator = getPrices().iterator(); iterator.hasNext();){
				ProductPrice price= (ProductPrice) iterator.next();
				if (result.getPrices() == null){
					result.setPrices(new HashSet<ProductPrice>());
				}
				result.getPrices().add(price);				
			}
			
		}
		return result;
	}

	@JsonIgnore
	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Listable : ").append(getIsListable()).append("\n");
		builder.append("Complex : ").append(getIsComplex()).append("\n");
		builder.append("Name : ").append(getName()).append("\n");
		builder.append("Description : ").append(getDescription()).append("\n");
		builder.append("Data : ").append(getExtraData()).append("\n");
		builder.append("Ratio : ").append(getRatio()).append("\n");
		builder.append("Lost ratio(%) : ").append(getLostRatio()).append("\n");
		builder.append("Min. quantity : ").append(getMinQuantity()).append("\n");
		if (getPrices() != null){
			for (ProductPrice price : getPrices()){
				builder.append("Prices : ").append(price.toString()).append("\n");
			}
		}
		if (getIsComplex()){
			if ( getChildren().size() > 0) {
				builder.append(getChildren().size() + " children : ").append("\n");
				for (Iterator<Product> iterator = getChildren().iterator(); iterator.hasNext();) {
					Product childProduct = (Product) iterator.next();
					builder.append(childProduct.toString()).append("\n");
				}
			}else{
				builder.append("NO children !").append("\n");
			}
		} else {
			if (getSelectors() != null) {
				builder.append(getSelectors().size() + " selectors : ").append("\n");
				for (Iterator<RawMaterialGroup> iterator = getSelectors()
						.iterator(); iterator.hasNext();) {
					RawMaterialGroup rawMaterialGroup = (RawMaterialGroup) iterator
							.next();
					builder.append(rawMaterialGroup.toString()).append("\n");
				}
			}else{
				builder.append("NO selectors !").append("\n");
			}
		}
		return builder.toString();
	}

}
