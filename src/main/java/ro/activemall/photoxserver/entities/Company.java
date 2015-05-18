package ro.activemall.photoxserver.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "company")
@Table(name = "photox_companies")
public class Company extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "registered_at", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime regDate;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@Column(name = "name")
	private String name;

	@Column(name = "vat_payer", columnDefinition = "BIT", length = 1)
	@Basic
	private boolean vat_payer = false;

	@Column(name = "is_default", columnDefinition = "BIT", length = 1)
	@Basic
	private boolean isDefault = true;

	@Column(name = "cif")
	private String cif;

	@Column(name = "rc")
	private String rc;

	@Column(name = "address")
	private String address;

	@Column(name = "phone")
	private String phone;

	@Column(name = "iban")
	private String iban;

	@Column(name = "bank")
	private String bank;

	@JsonIgnore
	public User getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public boolean isVATPayer() {
		return vat_payer;
	}

	public void setVATPayer(boolean vat_payer) {
		this.vat_payer = vat_payer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCIF() {
		return cif;
	}

	public void setCIF(String cif) {
		this.cif = cif;
	}

	public String getRC() {
		return rc;
	}

	public void setRC(String rc) {
		this.rc = rc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIBAN() {
		return iban;
	}

	public void setIBAN(String iban) {
		this.iban = iban;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@JsonIgnore
	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("Owner: ").append(getOwner().getUsername()).append("\n");
		builder.append("Name: ").append(getName()).append("\n");
		builder.append("pays V.A.T.: ").append(isVATPayer()).append("\n");
		builder.append("Address: ").append(getAddress()).append("\n");
		builder.append("CIF: ").append(getCIF()).append("\n");
		builder.append("RC : ").append(getRC()).append("\n");
		builder.append("IBAN : ").append(getIBAN()).append("\n");
		builder.append("Bank : ").append(getBank()).append("\n");
		builder.append("Phone: ").append(getPhone()).append("\n");

		return builder.toString();
	}
}
