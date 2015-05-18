package ro.activemall.photoxserver.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "setting")
@Table(name = "photox_settings")
public class PlatformSetting extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "setting_key", unique = true, nullable = false)
	public String key;

	@Column(name = "setting_value", nullable = false)
	public String value;

	@Column(name = "typecast", nullable = false)
	public String typecast;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTypecast() {
		return typecast;
	}

	public void setTypecast(String typecast) {
		this.typecast = typecast;
	}

	public Object getEffectiveValue() {
		Object result = null;
		switch (this.typecast) {
		case "Integer":
			result = Integer.valueOf(this.value);
			break;
		case "Double":
			result = Double.valueOf(this.value);
			break;
		}
		return result;
	}

}
