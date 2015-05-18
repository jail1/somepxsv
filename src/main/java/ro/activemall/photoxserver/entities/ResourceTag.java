package ro.activemall.photoxserver.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "resource_tag")
@Table(name = "photox_resources_tags")
public class ResourceTag extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "resource_id", nullable = false)
	private Resource resourceTag;

	@OneToOne
	@JoinColumn(name = "tag_id", nullable = false)
	private ResourceTagName tag;

	public Resource getResourceTag() {
		return resourceTag;
	}

	public void setResourceTag(Resource resourceTag) {
		this.resourceTag = resourceTag;
	}

	public ResourceTagName getTag() {
		return tag;
	}

	public void setTag(ResourceTagName tagName) {
		this.tag = tagName;
	}

	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResourceTag [id=");
		builder.append(id);
		builder.append(", resourceTag=");
		builder.append(resourceTag);
		builder.append(", tag=");
		builder.append(tag);
		builder.append("]");
		return builder.toString();
	}
}
