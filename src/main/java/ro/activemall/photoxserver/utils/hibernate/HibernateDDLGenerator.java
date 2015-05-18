package ro.activemall.photoxserver.utils.hibernate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateDDLGenerator {
	public static void main(String[] args) {
		execute(args[0], args[1], args[2]);
	}

	private static Iterable<Class> getClasses(String packageName)
			throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		LinkedList<File> dirs = new LinkedList<>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		LinkedList<Class> classes = new LinkedList<>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes;
	}

	private static LinkedList<Class> findClasses(File directory,
			String packageName) throws ClassNotFoundException {
		LinkedList<Class> classes = new LinkedList<>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					classes.addAll(findClasses(file,
							packageName + "." + file.getName()));
				} else if (file.getName().endsWith(".class")) {
					classes.add(Class.forName(packageName
							+ '.'
							+ file.getName().substring(0,
									file.getName().length() - 6)));
				}
			}
		}
		return classes;
	}

	private static LinkedList<Class> findAnnotatedClasses(String packageName) {
		LinkedList<Class> classes = new LinkedList<>();
		try {
			for (Class clazz : getClasses(packageName)) {
				classes.add(clazz);
			}
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Class not found exception occurred.",
					ex);
		} catch (IOException ex) {
			throw new RuntimeException("IO exception occurred.", ex);
		}
		return classes;
	}

	private static void execute(String fileName, String packageName,
			String dialect) {
		Configuration configuration = new Configuration();
		configuration.setProperty(Environment.DIALECT, dialect);
		for (Class<?> entityClass : findAnnotatedClasses(packageName)) {
			configuration.addAnnotatedClass(entityClass);
		}
		SchemaExport schemaExport = new SchemaExport(configuration);
		schemaExport.setDelimiter(";");
		schemaExport.setOutputFile(fileName);
		schemaExport.setFormat(false);
		schemaExport.create(false, false);
	}
}
