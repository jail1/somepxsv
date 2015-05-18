package ro.activemall.photoxserver.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.activemall.photoxserver.entities.Resource;
import ro.activemall.photoxserver.entities.ResourceTagName;
import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.enums.ResourcesTypes;
import ro.activemall.photoxserver.exceptions.PhotoxException;
import ro.activemall.photoxserver.json.PaginationJSON;
import ro.activemall.photoxserver.repositories.ResourcesRepository;
import ro.activemall.photoxserver.repositories.ResourcesTagsRepository;
import ro.activemall.photoxserver.repositories.TagsRepository;
import ro.activemall.photoxserver.repositories.UsersRepository;

@Service
public class ResourcesService extends ApplicationAbstractService {
	private static Logger log = Logger.getLogger(ResourcesService.class);

	@Autowired
	ResourcesRepository repository;

	@Autowired
	TagsRepository tagsRepo;

	@Autowired
	ResourcesTagsRepository resourcesTagsRepo;

	@Autowired
	UsersRepository usersRepository;

	@PersistenceContext
	EntityManager entityManager;

	// TODO : IMPORTANT - title cannot be null
	// creates a resource (alternatively we can use saveChildResource, so JSON
	// response is not that large)
	public List<Resource> createOrUpdateResources(List<Resource> resources,
			User loggedinUser) {
		List<Resource> result = new ArrayList<Resource>();
		for (Resource resource : resources) {
			if (resource.getId() == null) {
				log.error("[createOrUpdateResources] ID CANNOT BE NULL");
				throw new PhotoxException(getLocalizedMessage(
						"error.resource.notfound", loggedinUser));
			}
			if (resource.getSequence() == null) {
				log.error("[createOrUpdateResources] SEQ CANNOT BE NULL");
				throw new PhotoxException(getLocalizedMessage(
						"error.resource.badsequence", loggedinUser));
			}
			Resource foundResource = repository.findOne(resource.getId());
			if (foundResource == null) {
				throw new PhotoxException(getLocalizedMessage(
						"error.resource.notfound", loggedinUser));
			}
			if (foundResource.getOwner().getId() != loggedinUser.getId()) {
				throw new PhotoxException(getLocalizedMessage(
						"error.resource.owner", loggedinUser));
			}
			if (foundResource.getType() < ResourcesTypes.FILE.getIntValue()) {
				if (resource.getTitle() == null) {
					log.error("[createOrUpdateResources] TITLE CANNOT BE NULL FOR RESOURCES LESS 'FILE' TYPE");
					throw new PhotoxException(getLocalizedMessage(
							"error.resource.nulltitle", loggedinUser));
				}
			}
			// either update the sequence, or the title, description and extra
			if (foundResource.getSequence() != resource.getSequence()) {
				foundResource.setSequence(resource.getSequence());
			} else {
				if (resource.getDescription() != null) {
					foundResource.setDescription(resource.getDescription());
				}
				if (!foundResource.getTitle().equals(resource.getTitle())) {
					foundResource.setTitle(resource.getTitle());
				}
				if (resource.getExtra() != null) {
					foundResource.setExtra(resource.getExtra());
				}
			}
			// log.info(foundResource.toString());
			result.add(foundResource);
		}
		try {
			result = repository.save(result);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new PhotoxException(ex.getMessage());
		}
		// log.info("Resources saved!");
		return result;
	}

	// adds a child resource for a course, theme, etc
	public Resource saveChildResource(Resource resource,
			Long attachToResourceId, User loggedinUser) {
		Resource parentResource = repository.findOne(attachToResourceId);
		if (parentResource == null) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.notfound", loggedinUser));
		}
		if (resource.getTitle() == null) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.nulltitle", loggedinUser));
		}
		if (resource.getSequence() == null) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.badsequence", loggedinUser));
		}
		resource.setOwner(loggedinUser);
		resource.setParent(parentResource);
		// TODO : create sequence if it doesn't exists
		try {
			resource = repository.save(resource);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new PhotoxException(ex.getMessage());
		}
		// log.info("Attaching resource to "+attachToResourceId);
		// log.info(resource.toString());
		return resource;
	}

	public Resource createRootResource(Resource resource, User loggedinUser) {
		if (resource.getTitle() == null) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.nulltitle", loggedinUser));
		}
		resource.setOwner(loggedinUser);
		if (resource.getSequence() == null) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.badsequence", loggedinUser));
		}
		try {
			resource = repository.save(resource);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new PhotoxException(ex.getMessage());
		}
		// log.info("ROOT resource created");
		// log.info(resource.toString());
		return resource;
	}

	// returns the list of ROOT resource - that have something set in extra
	// field
	public List<Resource> getResourcesWithExtraFieldSet(User loggedinUser) {
		String sqlQuery = "SELECT r FROM resource r WHERE r.owner = :owner AND r.type = :type AND r.extra IS NOT NULL";
		// log.info("Listing exams paged : " + paginationData.toString() + "\n"
		// + sqlQuery);
		TypedQuery<Resource> query = entityManager.createQuery(sqlQuery,
				Resource.class);
		query.setParameter("owner", loggedinUser);
		query.setParameter("type", 0);// courses

		List<Resource> result = (ArrayList<Resource>) query.getResultList();
		// TODO : block the children... somehow
		return result;
	}

	// special query in repository (@see ResourcesRepository), which contains
	// the authenticated principal
	public List<Resource> findResourcesOwnedByAuthenticatedUser(User owner) {
		log.info("getting owned resources");
		return repository.findResourcesOwnedByAuthenticatedUser(owner);
	}

	// returns the list of ROOT resource - they contain it all
	public List<Resource> getResources(PaginationJSON paginationData,
			User loggedinUser) {
		String sqlQuery = "SELECT r FROM resource r WHERE r.owner = :owner AND r.type = :type ORDER BY r."
				+ paginationData.getSort();
		// log.info("Listing exams paged : " + paginationData.toString() + "\n"
		// + sqlQuery);
		TypedQuery<Resource> query = entityManager.createQuery(sqlQuery,
				Resource.class);
		query.setParameter("owner", loggedinUser);
		query.setParameter("type", 0);// courses
		query.setFirstResult((paginationData.getPage() - 1)
				* paginationData.getSize());
		query.setMaxResults(paginationData.getSize());

		List<Resource> result = (ArrayList<Resource>) query.getResultList();

		return result;
	}

	public List<Resource> searchResources(String term, User loggedinUser) {
		String sqlQuery = "SELECT r FROM resource r WHERE r.owner = :owner AND r.type = :type AND (UPPER(r.title) LIKE '%"
				+ term.toUpperCase()
				+ "%' OR UPPER(r.description) LIKE '%"
				+ term.toUpperCase() + "%') ORDER BY r.createdDate";

		TypedQuery<Resource> query = entityManager.createQuery(sqlQuery,
				Resource.class);
		query.setParameter("owner", loggedinUser);
		query.setParameter("type", 0);// courses

		List<Resource> result = (ArrayList<Resource>) query.getResultList();
		// log.info(((org.hibernate.Query)
		// query.unwrap(org.hibernate.Query.class)).getQueryString());
		return result;
	}

	/**
	 * public List<Resource> searchResources2(String term, User loggedinUser) {
	 * List<Predicate> criteriaListOr = new ArrayList<Predicate>();
	 * List<Predicate> criteriaListAnd = new ArrayList<Predicate>();
	 * 
	 * final CriteriaBuilder criteriaBuilder =
	 * entityManager.getCriteriaBuilder(); final CriteriaQuery criteriaQuery =
	 * criteriaBuilder.createQuery(Resource.class); final Root eventRoot =
	 * criteriaQuery.from(Resource.class);
	 * 
	 * Predicate predicate1 = criteriaBuilder.equal(eventRoot.get("owner"),
	 * loggedinUser); Predicate predicate2 =
	 * criteriaBuilder.equal(eventRoot.get("type"), 0); Predicate predicate3 =
	 * criteriaBuilder.like(criteriaBuilder.upper(eventRoot.get("title")), "%" +
	 * term.toUpperCase() + "%"); Predicate predicate4 =
	 * criteriaBuilder.like(criteriaBuilder.upper(eventRoot.get("description")),
	 * "%" + term.toUpperCase() + "%");
	 * 
	 * Predicate predicate = criteriaBuilder.and(predicate1, predicate2,
	 * criteriaBuilder.or(predicate3, predicate4));
	 * 
	 * criteriaQuery.where(predicate);
	 * 
	 * criteriaQuery.orderBy(criteriaBuilder.asc(eventRoot.get("createdDate")));
	 * 
	 * final TypedQuery<Resource> query =
	 * entityManager.createQuery(criteriaQuery);
	 * 
	 * log.info(((org.hibernate.Query)
	 * query.unwrap(org.hibernate.Query.class)).getQueryString());
	 * 
	 * return (List<Resource>) query.getResultList(); }
	 **/
	public Resource getResourceById(Long resourceId, User loggedinUser) {
		Resource result = repository.findOne(resourceId);
		if (result == null) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.notfound", loggedinUser));
		}
		if (result.getOwner().getId() != loggedinUser.getId()) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.owner", loggedinUser));
		}
		return result;
	}

	@Transactional
	public void updateSequenceForResource(Resource resource) {
		String sqlQuery = "UPDATE resource r SET r.sequence=r.sequence-1 WHERE r.parent=:parent AND r.sequence>:sequence";

		Query query = entityManager.createQuery(sqlQuery);
		query.setParameter("parent", resource.getParent());
		query.setParameter("sequence", resource.getSequence());
		// int updateCount = query.executeUpdate();
		query.executeUpdate();
		// log.info("Updated "+updateCount+" resources sequences");
	}

	// deletes a resource
	// TODO : make it accept Long resourceId instead of resource
	@Transactional
	public Resource deleteResource(Resource resource, User loggedinUser) {
		Resource result = repository.findOne(resource.getId());
		if (result == null || result.getOwner().getId() != loggedinUser.getId()) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.owner", loggedinUser));
		}
		try {
			if (result.getType() != ResourcesTypes.ROOT.getIntValue()) {
				updateSequenceForResource(result);
			}
			repository.delete(result);
			// log.info("Delete resource");
			// log.info(result);
		} catch (Exception ex) {
			log.error("Exception", ex);
			throw new PhotoxException(ex.getMessage());
		}
		// return nothing or HTTP.ok
		return result;
	}

	public Resource cloneResource(Long resourceId, Long targetUserId, User owner) {
		Resource target = repository.findOne(resourceId);
		if (target == null) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.notfound", owner));
		}
		if (target.getOwner().getId() != owner.getId()) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.owner", owner));
		}
		User targetOwner = usersRepository.findOne(targetUserId);
		Resource clonedResource = target.clone(targetOwner, null);
		try {
			clonedResource = repository.save(clonedResource);
			// log.info(clonedResource.toString());
		} catch (Exception ex) {
			log.error("Exception", ex);
			throw new PhotoxException(ex.getMessage());
		}
		// log.info("Resource cloned!");
		// TODO : send email to the target user announcing resource has been
		// cloned
		return clonedResource;
	}

	@Transactional
	public Resource transferResource(Long resourceId, Long newParentId,
			User loggedinUser) {
		Resource targetResource = repository.findOne(resourceId);
		if (targetResource == null) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.notfound", loggedinUser));
		}
		if (targetResource.getOwner().getId() != loggedinUser.getId()) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.owner", loggedinUser));
		}
		if (targetResource.getType() != ResourcesTypes.ROOT.getIntValue()) {
			// in parent of the moved resource, sequence number changes as well
			updateSequenceForResource(targetResource);
			// log.info("Old parent : "+targetResource.getParent().toString());
		}
		Resource newParentResource = repository.findOne(newParentId);
		if (newParentResource == null) {
			throw new PhotoxException(getLocalizedMessage(
					"error.resource.notfound", loggedinUser));
		}
		targetResource.setOwner(loggedinUser);
		targetResource.setParent(newParentResource);
		// sequence change (add as a last child)
		targetResource.setSequence(newParentResource.getChildren().size());
		try {
			targetResource = repository.save(targetResource);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new PhotoxException(ex.getMessage());
		}
		// log.info("resource "+targetResource.getTitle()+" moved to parent "+newParentResource.getTitle());
		// log.info(newParentResource.toString());
		return targetResource;
	}

	// declares a tag for a resource
	@SuppressWarnings("unused")
	public Resource declareTagToResource(ResourceTagName tag,
			Long attachToResourceId, User loggedinUser) {
		if (true) {
			throw new PhotoxException("Nu este implementat");
		}
		return new Resource();
	}

	// deletes a tag for a resource
	@SuppressWarnings("unused")
	public Resource deleteTagFromResource(ResourceTagName tag,
			Long attachToResourceId, User loggedinUser) {
		if (true) {
			throw new PhotoxException("Nu este implementat");
		}
		return new Resource();
	}
}
