package ro.activemall.photoxserver.controllers;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.fileupload.FileUploadBase.IOFileUploadException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import ro.activemall.photoxserver.annotations.CurrentlyLoggedUser;
import ro.activemall.photoxserver.api.ApiUrls;
import ro.activemall.photoxserver.entities.Resource;
import ro.activemall.photoxserver.entities.User;
import ro.activemall.photoxserver.enums.RolesAsStrings;
import ro.activemall.photoxserver.exceptions.PhotoxException;
import ro.activemall.photoxserver.services.UploadService;

//WIP
@RestController
public class UploadController {
	private static Logger log = Logger.getLogger(UploadController.class);

	// @Autowired
	// private Environment env;

	@Autowired
	UploadService service;

	// String imagesStorageFolderName;

	String resourcesFolderStorage;

	@PostConstruct
	public void setup() {
		// imagesStorageFolderName = env.getProperty("imagesStorageFolderName");
		// resourcesFolderStorage =
		// env.getProperty("resourcesStorageFolderName");
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(method = RequestMethod.POST, value = ApiUrls.FILEUPLOAD, produces = ApiUrls.JSON)
	public String fileUpload(@RequestParam("file") MultipartFile file,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.imageUpload(file, loggedinUser);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(method = RequestMethod.POST, value = ApiUrls.RESOURCEUPLOAD, produces = ApiUrls.JSON)
	@ResponseBody
	public Resource resourceUpload(@RequestPart MultipartFile file,
			@RequestPart Resource resource,
			@PathVariable("resId") Long attachToResourceId,
			@CurrentlyLoggedUser User loggedinUser) {
		return service.resourceUpload(file, attachToResourceId, loggedinUser,
				resource);
	}

	// Note the {fileName:.+} instruction which allows whitespaces and other
	// weird characters
	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.GETRESOURCE, method = RequestMethod.HEAD)
	public ResponseEntity<String> getResource(@PathVariable String fileName,
			@CurrentlyLoggedUser User loggedinUser) {
		if (resourcesFolderStorage == null) {
			throw new PhotoxException(
					"Forgot to mention resourcesFolderStorage");
		}
		final HttpHeaders headers = new HttpHeaders();
		File resourceFile = new File(new File("").getAbsolutePath()
				+ File.separator + "static" + File.separator
				+ resourcesFolderStorage, fileName);
		// log.info("User "+loggedinUser.getUsername()+" is REQUESTING HEAD of "+fileName);
		if (!resourceFile.exists()) {
			return new ResponseEntity<String>(null, headers,
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(null, headers, HttpStatus.OK);
	}

	@Secured({ RolesAsStrings.ROLE_SUPER_ADMIN, RolesAsStrings.ROLE_PHOTOGRAPHER,
			RolesAsStrings.ROLE_PHOTOGRAPHERS_CLIENT })
	@RequestMapping(value = ApiUrls.GETRESOURCE, method = RequestMethod.GET)
	public HttpEntity<byte[]> getResource(@PathVariable String fileName,
			WebRequest request, @CurrentlyLoggedUser User loggedinUser)
			throws IOException {
		return service.getPhisicalResource(fileName, request, loggedinUser);
	}

	@ExceptionHandler(MultipartException.class)
	public PhotoxException handleMultipartException(MultipartException ex) {
		log.error("Upload CANCELED?");
		return new PhotoxException(ex.getMessage());
	}

	// TODO : test upload canceled
	@ExceptionHandler(IOFileUploadException.class)
	public PhotoxException handleIOFileUploadException(IOFileUploadException ex) {
		log.error("Upload CANCELED?");
		return new PhotoxException(ex.getMessage());
	}
}