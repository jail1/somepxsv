package ro.activemall.photoxserver.entities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import ro.activemall.photoxserver.enums.FormFieldTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

//@Entity(name = "field")
//@Table(name = "photox_formfields")
public class FormField extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "target_user_id")
	private User targetUser;

	private String key;
	private FormFieldTypes type;
	private String label;
	private boolean required;
	public LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();

	private String targetTable;
	private String targetField;

	public FormField(String key, FormFieldTypes type, String label,
			boolean required) {
		this.key = key;
		this.type = type;
		this.label = label;
		this.required = required;
	}

	public FormField withOption(String key, String label) {
		options.put(key, label);
		return this;
	}

	public List<String> validate(Map<String, String> input) {
		List<String> messages = new ArrayList<String>();
		if (required) {
			if (!input.containsKey(key) || input.get(key) == null
					|| input.get(key).isEmpty()) {
				messages.add("Field " + label + " is required.");
			}
		}
		return messages;
	}
}
