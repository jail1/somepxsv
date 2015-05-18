package ro.activemall.photoxserver.security;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 
 * @author Badu Keeps the number of faulty login attempts so we can block hack
 *         attempts
 */
@Service
public class LoginAttemptService {

	private static Logger log = Logger.getLogger(LoginAttemptService.class);

	@Value("${application.max.login.attempts.before.block}")
	int MAX_ATTEMPT;

	private LoadingCache<String, Integer> attemptsCache;

	public LoginAttemptService() {
		super();
		attemptsCache = CacheBuilder.newBuilder()
				.expireAfterWrite(1, TimeUnit.DAYS)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	public void loginSucceeded(String key) {
		attemptsCache.invalidate(key);
	}

	public void loginFailed(String key) {
		int attempts = 0;
		try {
			attempts = attemptsCache.get(key);
		} catch (ExecutionException e) {
			attempts = 0;
		}
		attempts++;
		attemptsCache.put(key, attempts);
	}

	public boolean isBlocked(String key) {
		try {
			final boolean result = attemptsCache.get(key) >= MAX_ATTEMPT;
			if (result) {
				log.info("User from IP " + key + " has been blocked for a day");
			}
			return result;
		} catch (ExecutionException e) {
			return false;
		}
	}
}
