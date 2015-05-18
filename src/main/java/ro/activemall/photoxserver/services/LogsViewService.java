package ro.activemall.photoxserver.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ro.activemall.photoxserver.entities.UserLogActivity;
import ro.activemall.photoxserver.json.LogsCountPerUserJSON;
import ro.activemall.photoxserver.repositories.UsersLoggedActivitiesRepository;

@Service
public class LogsViewService extends ApplicationAbstractService {
	private static Logger log = Logger.getLogger(LogsViewService.class);

	@SuppressWarnings("unused")
	private void dummy() {
		log.info("Dummy");
	}

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	UsersLoggedActivitiesRepository repository;

	@Value("${application.delete.userLogs.olderThanDays}")
	int deleteLogsOlderThanDays;

	public List<UserLogActivity> getLogsForUser(Long userId) {
		return repository.getLogsForUser(userId);
	}

	@Transactional
	public List<LogsCountPerUserJSON> getNumberOfLogs() {
		Query deleteQuery = entityManager
				.createQuery("DELETE FROM user_log_activity l WHERE l.eventDate < :daysAgo");
		DateTime daysAgo = new DateTime().minusDays(deleteLogsOlderThanDays);
		deleteQuery.setParameter("daysAgo", daysAgo);
		int deleted = deleteQuery.executeUpdate();
		if (deleted > 0) {
			// log.info(deleted +
			// " rows were deleted because they were older than "+deleteLogsOlderThanDays+" days ago");
		}
		TypedQuery<LogsCountPerUserJSON> countQuery = entityManager
				.createQuery(
						"SELECT NEW ro.examo.server.json.admin.LogsCountPerUserJSON(COUNT(l), l.targetUser.id) FROM user_log_activity l GROUP BY l.targetUser.id",
						LogsCountPerUserJSON.class);
		return countQuery.getResultList();
	}

	@Transactional
	public int deleteLogsForUser(Long userId) {
		String sqlQuery = "DELETE FROM user_log_activity l WHERE l.targetUser.id = :userId";
		Query query = entityManager.createQuery(sqlQuery);
		query.setParameter("userId", userId);
		return query.executeUpdate();
	}
}
