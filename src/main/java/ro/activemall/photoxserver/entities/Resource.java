package ro.activemall.photoxserver.entities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import ro.activemall.photoxserver.enums.ResourcesTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity(name = "resource")
@Table(name = "photox_resources")
public class Resource extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	@JsonIgnore
	private boolean willReturnOwnerJson = false;

	@Column(name = "created_at", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createdDate;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@Column(name = "type", nullable = false)
	private int type;

	@Column(name = "sequence", nullable = true)
	private Integer sequence;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = Resource.class)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name = "parent_id", nullable = true)
	@OrderColumn(name = "sequence")
	private Set<Resource> children;

	// Note : if you do getParent(), hibernate will make sure it get's it.
	// Otherwise is kind of null??
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "parent_id", nullable = true, insertable = true, updatable = true, referencedColumnName = "id")
	private Resource parent;

	@Column(name = "title", length = 65534)
	private String title;

	@Column(name = "description", length = 65534)
	private String description;

	@Column(name = "extra", length = 65534)
	private String extra;

	@Column(name = "content_type", length = 255, nullable = true)
	private String contentType;

	@OneToMany(mappedBy = "resourceTag", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, targetEntity = ResourceTag.class)
	private Set<ResourceTag> tags;

	public Resource() {
		createdDate = new DateTime().toDateTime(DateTimeZone.UTC);
	}

	@PreUpdate
	protected void onUpdate() {
		if (createdDate == null) {
			createdDate = new DateTime().toDateTime(DateTimeZone.UTC);
		}
	}

	@JsonInclude(Include.NON_NULL)
	public DateTime getCreatedDate() {
		return createdDate;
	}

	@JsonInclude(Include.NON_NULL)
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@JsonIgnore
	public User getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(User owner) {
		this.owner = owner;
	}

	// Trick to return user with the resource
	@Transient
	@JsonInclude(Include.NON_NULL)
	public User getInstructor() {
		if (!willReturnOwnerJson)
			return null;
		return owner;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getSequence() {
		if (sequence == null) {
			System.out.println("NULL SEQUENCE FOR " + id);
		}
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@JsonInclude(Include.NON_NULL)
	public Set<Resource> getChildren() {
		return children;
	}

	@JsonInclude(Include.NON_NULL)
	public void setChildren(Set<Resource> children) {
		this.children = children;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonInclude(Include.NON_NULL)
	public String getDescription() {
		return description;
	}

	@JsonInclude(Include.NON_NULL)
	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@JsonInclude(Include.NON_NULL)
	public String getContentType() {
		return contentType;
	}

	@JsonInclude(Include.NON_NULL)
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@JsonInclude(Include.NON_NULL)
	public Set<ResourceTag> getTags() {
		return tags;
	}

	@JsonInclude(Include.NON_NULL)
	public void setTags(Set<ResourceTag> tags) {
		this.tags = tags;
	}

	@JsonIgnore
	public Resource getParent() {
		return parent;
	}

	@JsonIgnore
	public void setParent(Resource parent) {
		this.parent = parent;
	}

	@JsonIgnore
	public Resource(User newOwner, Resource parent) {
		this.owner = newOwner;
		this.createdDate = new DateTime().toDateTime(DateTimeZone.UTC);
		if (parent != null) {
			this.parent = parent;
		}
	}

	@JsonIgnore
	@Transient
	public Resource clone(User newOwner, Resource parent) {
		Resource newResource = new Resource(newOwner, parent);
		newResource.setType(this.getType());
		newResource.setSequence(this.getSequence());
		newResource.setTitle(this.getTitle());
		newResource.setDescription(this.getDescription());
		newResource.setExtra(this.getExtra());
		newResource.setContentType(this.getContentType());
		// TODO : clone TAGS
		// newResource.setTags(this.getTags());
		for (Iterator<Resource> iterator = this.children.iterator(); iterator
				.hasNext();) {
			Resource childResource = (Resource) iterator.next();
			if (newResource.getChildren() == null) {
				newResource.setChildren(new HashSet<Resource>());
			}
			newResource.getChildren().add(childResource.clone(newOwner, this));
		}
		return newResource;
	}

	// Trick to return owner with the resource
	@JsonIgnore
	@Transient
	public void setWillReturnOwnerJson(boolean value) {
		this.willReturnOwnerJson = value;
	}

	@JsonIgnore
	@Transient
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		if (type == ResourcesTypes.ROOT.getIntValue()) {
			builder.append("\n");
			builder.append(id + ") [ROOT][" + sequence + "] ");
		} else if (type == ResourcesTypes.CLIENT_FOLDER.getIntValue()) {
			builder.append("\n\t-");
			builder.append(id + ") [CH][" + sequence + "] ");
		} else if (type == ResourcesTypes.EVENT_FOLDER.getIntValue()) {
			builder.append("\n\t\t--");
			builder.append(id + ") [T][" + sequence + "] ");
			if (parent == null) {
				System.out.println("ORPHAN ???");
			}
		} else if (type == ResourcesTypes.FILE.getIntValue()) {
			builder.append("\n\t\t\t---");
			builder.append(id + ") [F][" + sequence + "] ");
			if (parent == null) {
				System.out.println("ORPHAN ???");
			}
		} else {
			builder.append("\n???UNKNOWN");
			builder.append(id + ")");
			if (parent == null) {
				System.out.println("ORPHAN ???");
			}
		}

		if (createdDate == null) {
			System.out.println("!!! createdDate IS NULL !!!");
		}

		builder.append(" title=");
		builder.append(title);
		if (description != null) {
			builder.append(" description=");
			builder.append(description);
		}
		if (contentType != null) {
			builder.append(" content type=");
			builder.append(contentType);
		}
		if (extra != null) {
			builder.append(" extra=");
			builder.append(extra);
		}
		if (tags != null) {
			if (tags.size() > 0) {
				builder.append(" tags=");
				builder.append(tags);
			}
		}

		if (children != null) {
			for (Resource child : children) {
				builder.append(child.toString());
			}
		}

		return builder.toString();
	}

}
