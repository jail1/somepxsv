package ro.activemall.photoxserver.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity(name = "invoice_product")
@Table(name = "photox_invoices_products")
public class InvoiceProduct extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "invoice_id")
	private Invoice invoice;

	@Column(name = "sequence", nullable = true)
	@OrderColumn(name = "sequence")
	private Integer sequence;

	@Column(name = "name")
	private String name;

	@Column(name = "um")
	private String um;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "price", precision = 10, length = 2)
	private double price;

	@Column(name = "vat")
	private int vat;

	@JsonIgnore
	public Invoice getInvoice() {
		return invoice;
	}

	@JsonIgnore
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonInclude(Include.NON_NULL)
	public String getUM() {
		return um;
	}

	@JsonInclude(Include.NON_NULL)
	public void setUM(String um) {
		this.um = um;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@JsonInclude(Include.NON_NULL)
	public int getVAT() {
		return vat;
	}

	@JsonInclude(Include.NON_NULL)
	public void setVAT(int vat) {
		this.vat = vat;
	}

	@JsonIgnore
	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\tName: ").append(getName()).append("\n");
		builder.append("\tPrice: ").append(getPrice()).append("\n");
		builder.append("\tV.A.T.[%]: ").append(getVAT()).append("\n");
		return builder.toString();
	}
}