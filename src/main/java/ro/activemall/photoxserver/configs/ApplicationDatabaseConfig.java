package ro.activemall.photoxserver.configs;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author Badu
 * 
 *         Database configuration
 */
@Configuration
// telling where to look for repositories
@EnableJpaRepositories(basePackages = { "ro.activemall.photoxserver.repositories" })
// and that we need transaction mananagement - some methods require special
// annotation to perform transaction
@EnableTransactionManagement
public class ApplicationDatabaseConfig {

	// @Autowired
	// private Environment env;

	@Value("${jdbc.driverClassName}")
	String datadourceDriverClassName;

	@Value("${jdbc.url}")
	String datasourceURL;

	@Value("${jdbc.user}")
	String datasourceUser;

	@Value("${jdbc.pass}")
	String datasourcePassword;

	// configuring the data source
	public DataSource dataSource() throws Exception {
		// log.info("Connecting to " + env.getProperty("jdbc.url") + " using " +
		// env.getProperty("jdbc.user") + " '"+ env.getProperty("jdbc.pass") +
		// "'");

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(datadourceDriverClassName);
		dataSource.setUrl(datasourceURL);
		dataSource.setUsername(datasourceUser);
		dataSource.setPassword(datasourcePassword);
		return dataSource;
	}

	// the specific vendor adapter - if we need to migrate to PostgresSQL or
	// other type of database, this is what is changing
	public JpaVendorAdapter jpaVendorAdapter() {
		// log.info("JPA Vendor Adapter");
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(false);// true if you want to see
													// what hibernate does

		hibernateJpaVendorAdapter.setGenerateDdl(false);
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		hibernateJpaVendorAdapter
				.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
		hibernateJpaVendorAdapter.getJpaPropertyMap().put(
				"hibernate.cache.use_second_level_cache", "true");

		hibernateJpaVendorAdapter.getJpaPropertyMap().put(
				"hibernate.cache.provider_class",
				"net.sf.ehcache.hibernate.EhCacheProvider");
		hibernateJpaVendorAdapter.getJpaPropertyMap().put(
				"hibernate.cache.use_query_cache", "true");
		hibernateJpaVendorAdapter.getJpaPropertyMap().put(
				"hibernate.generate_statistics", "true");

		return hibernateJpaVendorAdapter;
	}

	// instructing the local container entity manager where to look for entities
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
			throws Exception {
		// log.info("LocalContainerEntityManagerFactoryBean");
		LocalContainerEntityManagerFactoryBean manager = new LocalContainerEntityManagerFactoryBean();
		manager.setDataSource(dataSource());
		manager.setJpaVendorAdapter(jpaVendorAdapter());
		manager.setPackagesToScan("ro.activemall.photoxserver.entities");
		manager.getJpaPropertyMap().put(
				"org.jadira.usertype.dateandtime.joda.PersistentDateTime",
				"DateTime");
		// Configures the naming strategy that is used when Hibernate creates
		// new database objects and schema elements
		manager.getJpaPropertyMap().put("hibernate.ejb.naming_strategy",
				"org.hibernate.cfg.ImprovedNamingStrategy");
		// If the value of this property is true, Hibernate will format the SQL
		// that is written to the console.
		manager.getJpaPropertyMap().put("hibernate.format_sql", "true");
		return manager;
	}

	@Bean
	public PlatformTransactionManager transactionManager(
			EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager result = new JpaTransactionManager();
		result.setEntityManagerFactory(entityManagerFactory);
		// log.info("Transaction manager");
		return result;
	}
}
