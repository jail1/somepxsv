package ro.activemall.photoxserver.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ro.activemall.photoxserver.enums.InvoicesTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity(name = "invoice")
@Table(name = "photox_invoices")
public class Invoice extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "created_at", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createdDate;

	@Column(name = "due_to", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime dueTo;

	// @See InvoicesTypes
	@Column(name = "status")
	private int status = 0;

	@Column(name = "serie")
	private String serie;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@OneToOne
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	@OneToOne
	@JoinColumn(name = "target_company_id", nullable = false)
	private Company targetCompany;

	@OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = {
			CascadeType.MERGE, CascadeType.REFRESH }, targetEntity = InvoiceProduct.class)
	private Set<InvoiceProduct> products;

	@Column(name = "discount")
	private Long discount;

	public Invoice() {
		createdDate = new DateTime();
		// TODO : externalize into app settings
		dueTo = new DateTime().plusDays(30);
	}

	@JsonInclude(Include.NON_NULL)
	public DateTime getCreatedDate() {
		return createdDate;
	}

	@JsonInclude(Include.NON_NULL)
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@JsonInclude(Include.NON_NULL)
	public DateTime getDueTo() {
		return dueTo;
	}

	@JsonInclude(Include.NON_NULL)
	public void setDueTo(DateTime dueTo) {
		this.dueTo = dueTo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@JsonIgnore
	public User getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Company getTargetCompany() {
		return targetCompany;
	}

	public void setTargetCompany(Company targetCompany) {
		this.targetCompany = targetCompany;
	}

	public Set<InvoiceProduct> getProducts() {
		return products;
	}

	public void setProducts(Set<InvoiceProduct> products) {
		this.products = products;
	}

	@JsonInclude(Include.NON_NULL)
	public Long getDiscount() {
		return discount;
	}

	@JsonInclude(Include.NON_NULL)
	public void setDiscount(Long discount) {
		this.discount = discount;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	@JsonIgnore
	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Owner: ").append(getOwner().getUsername()).append("\n");
		builder.append("Serie: ").append(getSerie()).append("\n");
		builder.append("Company: ").append(getCompany().toString())
				.append("\n");
		builder.append("Target Company: ").append(getCompany().toString())
				.append("\n");

		builder.append("Status: ").append(InvoicesTypes.toString(getStatus()))
				.append("\n");
		double total = 0.0;
		double totalVAT = 0.0;
		for (InvoiceProduct invProd : getProducts()) {
			// TODO : calculate total
			// total += invProd.
			if (getCompany().isVATPayer()) {
				builder.append("V.A.T. [24%] = ").append(getDiscount())
						.append("\n");
			}
		}
		builder.append("Total: ").append(total).append("\n");
		builder.append("Discount: ").append(getDiscount()).append("\n");
		// TODO : calculate total after discount
		builder.append("Total after discount: ").append(total).append("\n");
		return builder.toString();
	}

}
