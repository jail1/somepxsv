package ro.activemall.photoxserver.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity(name = "rawmaterial")
@Table(name = "photox_rawmaterials")
public class RawMaterial extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// name of the raw material
	@NotNull
	@Column(name = "name", nullable = false)
	private String name;
	//
	@Column(name = "description")
	private String description;
	// Acquisition price
	@Column(name = "aquisition_price", precision = 10, length = 2, nullable = false)
	private double aquisitionPrice;
	// generic number of units (milliliters, square meters, etc)
	@Column(name = "aquisition_units", precision = 10, length = 2, nullable = false)
	private double aquisitionUnits;
	// calculated price using the rule of three (x = bc/a when a/b=c/x)
	@Column(name = "price_per_production_unit", precision = 10, length = 2, nullable = false)
	private double pricePerProductionUnit;
	// generic number of units used for producing an unit (in our case 1 square
	// meter)
	@Column(name = "units_per_production_unit", precision = 10, length = 2, nullable = false)
	private double unitsPerProductionUnit;
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "parent_group_id")
	private RawMaterialGroup parentGroup;

	@JsonIgnore
	public RawMaterialGroup getParentGroup() {
		return parentGroup;
	}

	@JsonIgnore
	public void setParentGroup(RawMaterialGroup parentGroup) {
		this.parentGroup = parentGroup;
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

	// TODO : make this null when someone else other than ADMIN requires
	// products list
	@JsonInclude(Include.NON_NULL)
	public double getAquisitionPrice() {
		return aquisitionPrice;
	}

	@JsonInclude(Include.NON_NULL)
	public void setAquisitionPrice(double aquisitionPrice) {
		this.aquisitionPrice = aquisitionPrice;
	}

	@JsonInclude(Include.NON_NULL)
	public double getAquisitionUnits() {
		return aquisitionUnits;
	}

	@JsonInclude(Include.NON_NULL)
	public void setAquisitionUnits(double aquisitionUnits) {
		this.aquisitionUnits = aquisitionUnits;
	}

	@JsonInclude(Include.NON_NULL)
	public double getPricePerProductionUnit() {
		return pricePerProductionUnit;
	}

	@JsonInclude(Include.NON_NULL)
	public void setPricePerProductionUnit(double pricePerProductionUnit) {
		this.pricePerProductionUnit = pricePerProductionUnit;
	}

	@JsonInclude(Include.NON_NULL)
	public double getUnitsPerProductionUnit() {
		return unitsPerProductionUnit;
	}

	@JsonInclude(Include.NON_NULL)
	public void setUnitsPerProductionUnit(double unitsPerProductionUnit) {
		this.unitsPerProductionUnit = unitsPerProductionUnit;
	}	

	@JsonIgnore
	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\t\tName: ").append(getName()).append("\n");
		builder.append("\t\tDescription: ").append(getDescription())
				.append("\n");
		builder.append("\t\tAq.units: ").append(getAquisitionUnits())
				.append("\n");
		builder.append("\t\tAq. price: ").append(getAquisitionPrice())
				.append("\n");
		builder.append("\t\tUnits per production unit: ")
				.append(getUnitsPerProductionUnit()).append("\n");
		builder.append("\t\tPrice per production unit: ")
				.append(getPricePerProductionUnit()).append("\n");		
		return builder.toString();
	}
}