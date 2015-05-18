package ro.activemall.photoxserver.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ro.activemall.photoxserver.entities.HierarchicalUser;
import ro.activemall.photoxserver.entities.PlatformSetting;
import ro.activemall.photoxserver.entities.Role;
import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.enums.UserRoles;
import ro.activemall.photoxserver.events.SendEmailEvent;
import ro.activemall.photoxserver.exceptions.PhotoxException;
import ro.activemall.photoxserver.json.JodaDateTimeObjectMapper;
import ro.activemall.photoxserver.json.PaginationJSON;
import ro.activemall.photoxserver.json.UserAndRoleJSON;
import ro.activemall.photoxserver.json.UserPreferencesDataJSON;
import ro.activemall.photoxserver.repositories.HierarchicalUsersRepository;
import ro.activemall.photoxserver.repositories.PlatformSettingsRepository;
import ro.activemall.photoxserver.repositories.UsersRepository;
import ro.activemall.photoxserver.utils.passwords.MD5Generator;
import ro.activemall.photoxserver.utils.passwords.PasswordAndTokenPO;
import ro.activemall.photoxserver.utils.passwords.PasswordGenerator;

@Service
public class UsersService extends ApplicationAbstractService {
	private static Logger log = Logger.getLogger(UsersService.class);

	@Autowired
	ApplicationContext appContext;

	int DEFAULT_PAGE = 0;

	int DEFAULT_PAGE_SIZE = 10;

	@Value("${application.default.server.language}")
	String defaultUserPreferedLanguage;

	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	EntityManagerFactory entityManagerFactory;

	// @Autowired
	// private Environment env;

	boolean orderByAscending = true;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	HierarchicalUsersRepository refereeRepository;

	@Autowired
	UsersRepository repository;

	@Autowired
	PlatformSettingsRepository settingsRepository;

	@Value("${applicaiton.defaultuserpreferences.folder}")
	String userPreferencesFolderName;

	@PostConstruct
	public void setup() {
		if (repository.count() < 1) {
			log.info("Fresh database - creating root account");
			createRootAccount();

			log.info("Importing external SQL");
			importSQLs();
			log.info("External SQL done!");
		}
		// TODO : re-enable for production
		// setEuroValue();
	}

	public String activateAccount(String token, Long userId) {
		User existingUser = repository.findOne(userId);
		if (existingUser == null) {
			return "Utilizatorul pentru care se face activarea nu a fost gasit";
		}
		if (existingUser.getActivationString().equals(token)) {
			existingUser.setAccountEnabled(true);
			repository.save(existingUser);
			return getLocalizedMessage("error.activation.success", existingUser);
		}
		return getLocalizedMessage("error.activation.badtoken", existingUser);
	}

	public User createAccountOnlyWithEmail(String email) {
		// assertMatches(email, EMAIL_REGEX, "Invalid email.");
		// assertMatches(password, PASSWORD_REGEX,
		// "Password must have at least 6 characters, with 1 numeric and 1 uppercase character.");
		User user = new User();
		User existingUser = repository.findByUsername(email.toLowerCase());
		if (existingUser == null) {
			// Bug fix : validation fails when upper case letters are
			// encountered
			user.setUsername(email.toLowerCase());
			// log.info(user.getUsername());
			user.setAccountEnabled(false);
			Role role = new Role();
			role.setRole(UserRoles.ROLE_PHOTOGRAPHER.getIntValue());
			user.setRole(role);			
			// Attempt to extract first name / last name from user's email address
			String wholeNames = email.replaceAll("@.*", "").replace(".", " ");
			String[] names = wholeNames.split(" ");
			String lastName = "";
			String firstName = "";
			if (names.length >= 2) {
				for (int i = 0; i < names.length; i++) {
					String currentName = Character.toUpperCase(names[i].charAt(0)) + names[i].substring(1).toLowerCase();
					if (i == names.length - 1) {
						lastName = currentName;
					} else {
						if (firstName.equals("")) {
							firstName = currentName;
						} else {
							firstName += " " + currentName;
						}
					}
				}
			}
			// end bug fix
			user.setFirstName(firstName);
			user.setLastName(lastName);

			user.setMobilePhone("");
			if (user.getPreferedLanguage() == null) {
				user.setPreferedLanguage(defaultUserPreferedLanguage);
			}
			String token = UUID.randomUUID().toString();
			user.setActivationString(token);
			String password = PasswordGenerator.getRandomString(8);
			SendEmailEvent event = new SendEmailEvent(user,
					SendEmailEvent.REGISTER);
			event.setExtra(new PasswordAndTokenPO(password, token));
			user.setPassword(passwordEncoder.encode(password));
			repository.save(user);
			publisher.publishEvent(event);
		} else {
			user = existingUser;
		}
		saveUserPreference(user, new UserPreferencesDataJSON(defaultUserPreferedLanguage));
		return user;
	}

	private User createRootAccount() {
		User rootUser = new User();

		rootUser.setFirstName("ActiveMall");
		rootUser.setLastName("SRL");
		rootUser.setMobilePhone("+4000000");
		rootUser.setPassword("4dm1n1str4");
		rootUser.setAccountEnabled(true);// or else, you won't be able to login
		Role rootRole = new Role();
		rootRole.setRole(UserRoles.ROLE_SUPER_ADMIN.getIntValue());
		rootUser.setRole(rootRole);
		rootUser.setUsername("office@activemall.ro");
		rootUser.setLoginName("admin");
		rootUser.setPreferedLanguage(defaultUserPreferedLanguage);
		// step : creating a password for this account
		String newPassword = rootUser.getPassword();
		// step : encoding password with salt
		rootUser.setPassword(passwordEncoder.encode(newPassword));
		// step : setting activation string
		rootUser.setActivationString(MD5Generator.md5(rootUser.getUsername()
				+ "/-=-\"" + newPassword, "salt"));
		// step : saving the user
		User saved = rootUser;
		try {
			saved = repository.save(rootUser);
		} catch (Exception ex) {
			log.error("Exception", ex);
		}
		// TODO : check why user id is NULL
		saveUserPreference(rootUser, new UserPreferencesDataJSON(
				defaultUserPreferedLanguage));
		// init all application settings using external SQL, but for MySQL
		return saved;
	}

	public User deleteUser(User user) {
		// TODO : send user email with account deleted
		// TODO : clean users files
		repository.delete(user);
		user.setId(0L);
		return user;
	}

	public String emailCheck(String email) {		
		Long result = repository.countUsersWithUsername(email.toLowerCase());
		//User user = repository.findByUsername(email.toLowerCase());
		//log.info("Verifying uniqueness " + email + " gave result "+result);
		if (result > 0) {			
			//log.error(user.toString());
			return "{\"isUnique\" : \"false\"}";
		}		
		return "{\"isUnique\" : \"true\"}";
	}

	public String loginCheck(String loginname) {		
		Long result = repository.countUsersWithLoginName(loginname.toLowerCase());
		//User user = repository.findByUsername(email.toLowerCase());
		log.info("Verifying uniqueness " + loginname + " gave result "+result);
		if (result > 0) {			
			//log.error(user.toString());
			return "{\"isUnique\" : \"false\"}";
		}		
		return "{\"isUnique\" : \"true\"}";
	}

	public User findByUsernameOrLoginName(String username) {
		return repository.findByUsernameOrLoginName(username);
	}

	public User findUserByUserName(String username) {
		return repository.findByUsername(username);
	}

	public Role fixUserWithNoRole(User user, boolean withSave) {
		Role newRole = new Role();
		newRole.setUser(user);
		newRole.setRole(UserRoles.ROLE_PHOTOGRAPHERS_CLIENT.getIntValue());
		user.setRole(newRole);
		if (withSave) {
			repository.save(user);
		}
		return newRole;
	}

	public List<User> getAllUsers() {
		return repository.findAll();
	}

	public Set<HierarchicalUser> getRefereeList(User authenticatedUser) {
		return refereeRepository.findOne(authenticatedUser.getId()).getReferees();
	}

	public User getUser(Long userId) {
		return repository.findOne(userId);
	}

	public List<User> getUsersPagedAndOrdered(PaginationJSON paginationData) {
		// log.info("Listing users paged : " + paginationData.toString());
		String sqlQuery = "SELECT u FROM user u ORDER BY u."
				+ paginationData.getSort();
		// log.info(sqlQuery);
		TypedQuery<User> query = (TypedQuery<User>) entityManager
				.createQuery(sqlQuery);

		query.setFirstResult((paginationData.getPage() - 1)
				* paginationData.getSize());
		query.setMaxResults(paginationData.getSize());

		List<User> result = (ArrayList<User>) query.getResultList();
		// log.info(result.toString());
		return result;
	}

	private void importSQLs() {
		try {
			EntityManager entityManager2 = entityManagerFactory
					.createEntityManager();
			Resource resource = appContext
					.getResource("classpath:templates/sql/initial_data.sql");
			InputStream is = resource.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(is));
			String line;
			entityManager2.getTransaction().begin();
			while ((line = bufferedReader.readLine()) != null) {
				log.info("Executing SQL : " + line);
				entityManager2.createNativeQuery(line).executeUpdate();
			}
			bufferedReader.close();
			entityManager2.getTransaction().commit();
			entityManager2.close();
		} catch (IOException io) {
			log.error("IOException", io);
		}
	}

	public UserPreferencesDataJSON readUserPreferencesJSON(User user) {
		try {
			JodaDateTimeObjectMapper mapper = new JodaDateTimeObjectMapper();
			String jarPath = new File("").getAbsolutePath();
			String prefsFolder = jarPath + File.separator + "static"
					+ File.separator + userPreferencesFolderName
					+ File.separator;
			File sourceFile = new File(prefsFolder + user.getId() + ".json");
			UserPreferencesDataJSON json = mapper.readValue(sourceFile,
					UserPreferencesDataJSON.class);
			return json;
		} catch (Exception ex) {
			log.error("Exception", ex);
		}
		return new UserPreferencesDataJSON();
	}

	public UserPreferencesDataJSON saveUserPreference(User user,
			UserPreferencesDataJSON data) {
		if (user.getId() == null || user.getId() == 0) {
			throw new PhotoxException("User id NULL or ZERO ?");
		}
		try {
			JodaDateTimeObjectMapper mapper = new JodaDateTimeObjectMapper();
			String jarPath = new File("").getAbsolutePath();
			String prefsFolder = jarPath + File.separator + "static"
					+ File.separator + userPreferencesFolderName
					+ File.separator;
			File destinationFile = new File(prefsFolder + user.getId()
					+ ".json");
			new File(prefsFolder).mkdirs();
			mapper.writeValue(destinationFile, data);
		} catch (Exception ex) {
			log.error("Exception", ex);
		}
		return data;
	}

	// private static final Pattern EMAIL_REGEX =
	// Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

	public void saveUsersLastSeenDate(User authenticatedUser) {
		authenticatedUser.setLastLoggedIn(DateTime.now());
		// log.info("(last seen saved)");
		repository.save(authenticatedUser);
	}

	public List<User> search(String fragment) {
		List<Predicate> criteriaList = new ArrayList<Predicate>();

		final CriteriaBuilder criteriaBuilder = entityManager
				.getCriteriaBuilder();
		final CriteriaQuery criteriaQuery = criteriaBuilder
				.createQuery(User.class);
		final Root eventRoot = criteriaQuery.from(User.class);

		Predicate predicate1 = criteriaBuilder.like(
				criteriaBuilder.upper(eventRoot.get("username")), "%"
						+ fragment.toUpperCase() + "%");
		criteriaList.add(predicate1);
		Predicate predicate2 = criteriaBuilder.like(
				criteriaBuilder.upper(eventRoot.get("firstName")), "%"
						+ fragment.toUpperCase() + "%");
		criteriaList.add(predicate2);
		Predicate predicate3 = criteriaBuilder.like(
				criteriaBuilder.upper(eventRoot.get("lastName")), "%"
						+ fragment.toUpperCase() + "%");
		criteriaList.add(predicate3);

		criteriaQuery.where(criteriaBuilder.or((Predicate[]) criteriaList
				.toArray(new Predicate[0])));

		criteriaQuery.orderBy(criteriaBuilder.asc(eventRoot.get("id")));
		final TypedQuery query = entityManager.createQuery(criteriaQuery);

		return (ArrayList<User>) query.getResultList();
	}

	public String setLanguageForLoggedInUser(String language,
			User authenticatedUser) {
		if (authenticatedUser != null) {
			authenticatedUser.setPreferedLanguage(language);
			repository.save(authenticatedUser);
			// log.info("Saved current user's language : "+language);
			return "{\"language\":\"" + language + "\"}";
		}
		throw new PhotoxException(getLocalizedMessage("error.authentication",
				authenticatedUser));
	}

	public User setRoleForUser(UserAndRoleJSON userAndRole,
			User authenticatedUser) {
		if (userAndRole.getUser().getId() < 0) {
			throw new PhotoxException(getLocalizedMessage("error.userid.zerro",
					authenticatedUser));
		}
		User existingUser = repository.findByUsername(userAndRole.getUser()
				.getUsername());
		Role newRole = null;
		if (existingUser == null) {
			throw new PhotoxException(getLocalizedMessage("error.nosuch.user",
					authenticatedUser));
		}
		if (existingUser.getRole() == null) {
			newRole = fixUserWithNoRole(existingUser, false);
			userAndRole.getUser().setRole(newRole);
		} else {
			userAndRole.getUser().setRole(existingUser.getRole());
		}
		// log.info("User "+userAndRole.getUser().toString()+" updated");
		if (!userAndRole.getDowngrade()) {
			// if it's downgrade (revoke role)
			if (userAndRole.getRole() == UserRoles.ROLE_SUPER_ADMIN
					.getIntValue()) {
				existingUser.getRole().setRole(
						UserRoles.ROLE_PHOTOGRAPHER.getIntValue());
			} else if (userAndRole.getRole() == UserRoles.ROLE_PHOTOGRAPHER
					.getIntValue()) {
				existingUser.getRole().setRole(
						UserRoles.ROLE_PHOTOGRAPHERS_CLIENT.getIntValue());
			} else if (userAndRole.getRole() == UserRoles.ROLE_PHOTOGRAPHERS_CLIENT
					.getIntValue()) {
				log.error("Cannot downgrade user role");
				throw new PhotoxException(getLocalizedMessage(
						"error.userrole.downgrade", authenticatedUser));
			}
		} else {
			if (newRole == null) {
				existingUser.getRole().setRole(userAndRole.getRole());
			} else {
				existingUser.getRole().setRole(
						UserRoles.ROLE_PHOTOGRAPHERS_CLIENT.getIntValue());
			}
		}
		if (userAndRole.getUser().getChangePasswordTo() != null) {
			userAndRole.getUser().setPassword(
					passwordEncoder.encode(userAndRole.getUser()
							.getChangePasswordTo()));
		} else {
			// preserve existing password
			userAndRole.getUser().setPassword(existingUser.getPassword());
		}
		User result = repository.save(userAndRole.getUser());
		// log.info("Role of " + result.getRole().toString()+ " was set to " +
		// result.getUsername() +
		// " PWD : "+userAndRole.getUser().getPassword());
		return result;
	}

	// updates user data from admin
	public User updateUser(User user, User authenticatedUser) {
		if (user.getId() < 0) {
			throw new PhotoxException(getLocalizedMessage("error.userid.zerro",
					authenticatedUser));
		}
		User existingUser = repository.findByUsername(user.getUsername());
		if (existingUser == null) {
			throw new PhotoxException(getLocalizedMessage("error.nosuch.user",
					authenticatedUser));
		}
		if (existingUser.getRole() == null) {
			Role newRole = fixUserWithNoRole(existingUser, false);
			existingUser.setRole(newRole);
		} else {
			user.setRole(existingUser.getRole());
		}
		if (user.getChangePasswordTo() != null) {
			log.info("Password detected : " + user.getChangePasswordTo());
			user.setPassword(passwordEncoder.encode(user.getChangePasswordTo()));
		} else {
			log.info("Password preserved : " + existingUser.getPassword());
			// preserve existing password
			user.setPassword(existingUser.getPassword());
		}
		// log.info("User "+user.toString()+" updated");
		return repository.save(user);
	}

	@Transactional
	public User createAccount(User user, User authenticatedUser) {
		String email = user.getUsername().toLowerCase();
		String loginname = user.getLoginName().toLowerCase();
		if (repository.countUsersWithUsername(email) > 0){
			throw new PhotoxException(getLocalizedMessage("error.user.username.exists", authenticatedUser));
		}
		if (repository.countUsersWithLoginName(loginname) > 0){
			throw new PhotoxException(getLocalizedMessage("error.user.loginname.exists", authenticatedUser));
		}
		user.setUsername(email);
		user.setAccountEnabled(true);
		
		Role role = new Role();
		role.setRole(UserRoles.ROLE_PHOTOGRAPHER.getIntValue());
		user.setRole(role);			
		// Attempt to extract first name / last name from user's email address
		String wholeNames = email.replaceAll("@.*", "").replace(".", " ");
		String[] names = wholeNames.split(" ");
		String lastName = "";
		String firstName = "";
		if (names.length >= 2) {
			for (int i = 0; i < names.length; i++) {
				String currentName = Character.toUpperCase(names[i].charAt(0)) + names[i].substring(1).toLowerCase();
				if (i == names.length - 1) {
					lastName = currentName;
				} else {
					if (firstName.equals("")) {
						firstName = currentName;
					} else {
						firstName += " " + currentName;
					}
				}
			}
		}
		if (user.getFirstName() == null){
			user.setFirstName(firstName);
		}
		if (user.getLastName() == null){
			user.setLastName(lastName);
		}		
		if (user.getPreferedLanguage() == null) {
			user.setPreferedLanguage(defaultUserPreferedLanguage);
		}
		String token = UUID.randomUUID().toString();
		user.setActivationString(token);
		String password = PasswordGenerator.getRandomString(8);
		if (user.getChangePasswordTo() != null) {
			password = user.getChangePasswordTo();
		}				
		SendEmailEvent event = new SendEmailEvent(user, SendEmailEvent.REGISTER);
		event.setExtra(new PasswordAndTokenPO(password, token));
		user.setPassword(passwordEncoder.encode(password));		
		user = repository.save(user);
		
		setParentOfUser(user.getId(), authenticatedUser.getId());
		
		publisher.publishEvent(event);
		saveUserPreference(user, new UserPreferencesDataJSON(defaultUserPreferedLanguage));
		return user;
	}
	@Transactional
	public void setParentOfUser(Long childId, Long parentId){
		String sqlQuery = "UPDATE photox_users SET parent_id = :parent_id WHERE id = :child_id";
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("child_id", childId);
		query.setParameter("parent_id", parentId);
		query.executeUpdate();
	}

	// works with authenticated user
	public User updateUserProfile(User user, User authenticatedUser) {
		log.info("updateUserProfile");
		log.info(user.toString());
		if (!authenticatedUser.getUsername().equals(user.getUsername())
				|| !authenticatedUser.getId().equals(user.getId())) {
			throw new PhotoxException(getLocalizedMessage("error.authentication", authenticatedUser));
		}
		User existingUser = repository.findByUsername(authenticatedUser.getUsername());		
		if (existingUser == null) {
			throw new PhotoxException(getLocalizedMessage("error.nosuch.user",authenticatedUser));
		}
		existingUser.setFirstName(user.getFirstName());
		existingUser.setLastName(user.getLastName());
		existingUser.setMobilePhone(user.getMobilePhone());
		if (existingUser.getRole() == null) {
			Role newRole = fixUserWithNoRole(existingUser, false);
			existingUser.setRole(newRole);
		} else {
			user.setRole(existingUser.getRole());
		}
		if (user.getChangePasswordTo() != null) {
			// send email with credentials change
			SendEmailEvent event = new SendEmailEvent(authenticatedUser, SendEmailEvent.CHANGE_PASSWORD);
			event.setExtra(user.getChangePasswordTo());
			publisher.publishEvent(event);
			existingUser.setPassword(passwordEncoder.encode(user.getChangePasswordTo()));
		} else {
			// preserve existing password
			//user.setPassword(existingUser.getPassword());
		}		
		return repository.save(existingUser);
	}

	// TODO : move this and SaxCurrencyHandler into scheduled task that runs on
	// startup and every 24 hours
	public void setEuroValue() {
		PlatformSetting setting = settingsRepository
				.getPropertyByName("applicationEuro");
		// log.info("Old EUR : "+setting.getEffectiveValue());
		try {
			log.info("Getting BNR EUR value...");
			String bnrWebService = "http://www.bnro.ro/nbrfxrates.xml";
			URL bnrUrl = new URL(bnrWebService);
			URLConnection connection = bnrUrl.openConnection();
			connection.setConnectTimeout(120000);
			connection.setReadTimeout(120000);
			BufferedReader details = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			InputSource inputSource = new InputSource(details);
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			SaxCurrencyHandler currencyHandler = new SaxCurrencyHandler();
			saxParser.parse(inputSource, currencyHandler);

			setting.setValue(currencyHandler.currencies.get("EUR"));
			settingsRepository.save(setting);
			log.info("Saved today EUR : " + setting.getEffectiveValue());
		} catch (Exception ex) {
			log.error("Exception", ex);
		}
	}

	public class SaxCurrencyHandler extends DefaultHandler {

		public Map<String, String> currencies = new HashMap<String, String>();
		private String currency;
		private String currencyValue;
		private boolean readValue = false;

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("rate".equals(qName.toLowerCase())) {
				readValue = true;
				currency = attributes.getValue("currency");
			}
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("rate".equals(qName.toLowerCase())) {
				currencies.put(currency, currencyValue);
			}
			readValue = false;
		}

		public void characters(char ch[], int start, int length)
				throws SAXException {
			String value = new String(ch, start, length).trim();
			if (value.length() == 0)
				return; // ignore white space
			if (readValue) {
				currencyValue = value;
			}
		}
	}

}
