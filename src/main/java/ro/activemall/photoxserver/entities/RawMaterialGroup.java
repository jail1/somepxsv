package ro.activemall.photoxserver.entities;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity(name = "rawmaterialgroup")
@Table(name = "photox_rawmaterial_groups")
public class RawMaterialGroup extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Size.List({ @Size(min = 0, message = "{error.size.min}"),
			@Size(max = 254, message = "{error.size.max}") })
	@NotNull(message = "{error.cannot.be.null}")
	@NotEmpty(message = "{error.cannot.be.empty}")
	@Column(name = "name", unique = true)
	private String name;

	// signals that this group is required in every product (like manufacturing
	// cost or electricity)
	// automatically added when creating a product
	@Column(name = "required_in_product", columnDefinition = "BIT", length = 1)
	@Basic
	private boolean requiredInEveryProduct = false;
	// signals that this group allows customization of the product
	@Column(name = "is_selector", columnDefinition = "BIT", length = 1)
	@Basic
	private boolean isSelector = true;

	@Column(name = "selector_sequence")
	private int selectorSequence = 0;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parentGroup", cascade = { CascadeType.ALL }, orphanRemoval = true)
	// @JoinTable(name = "photox_rawmaterials_grouping", joinColumns = {
	// @JoinColumn(name = "rawmaterialgroup_id") }, inverseJoinColumns = {
	// @JoinColumn(name = "rawmaterial_id") })
	private Set<RawMaterial> rawMaterials;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonInclude(Include.NON_NULL)
	public Set<RawMaterial> getRawMaterials() {
		return rawMaterials;
	}

	@JsonInclude(Include.NON_NULL)
	public void setRawMaterials(Set<RawMaterial> rawMaterials) {
		this.rawMaterials = rawMaterials;
	}

	public boolean getIsRequiredInEveryProduct() {
		return requiredInEveryProduct;
	}

	public void setIsRequiredInEveryProduct(boolean requiredInEveryProduct) {
		this.requiredInEveryProduct = requiredInEveryProduct;
	}

	public boolean getIsSelector() {
		return isSelector;
	}

	public void setIsSelector(boolean isSelector) {
		this.isSelector = isSelector;
	}

	public int getSelectorSequence() {
		return selectorSequence;
	}

	public void setSelectorSequence(int selectorSequence) {
		this.selectorSequence = selectorSequence;
	}

	@JsonIgnore
	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\tId: ").append(getId()).append("\n");
		builder.append("\tGroup Name: ").append(getName()).append("\n");
		builder.append("\tDefault for products: ")
				.append(getIsRequiredInEveryProduct()).append("\n");
		builder.append("\tSelector: ").append(getIsSelector()).append("\n");
		for (RawMaterial material : getRawMaterials()) {
			builder.append("\n\t\tMaterial: ").append("\n")
					.append(material.toString());
		}
		return builder.toString();
	}
}