package ro.activemall.photoxserver.json;

public class LogsCountPerUserJSON {
	public Long logsCount;
	public Long userId;

	public LogsCountPerUserJSON(Long count, Long uid) {
		this.logsCount = count;
		this.userId = uid;
	}
}
