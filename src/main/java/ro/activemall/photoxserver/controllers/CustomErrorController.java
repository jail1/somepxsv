package ro.activemall.photoxserver.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.activemall.photoxserver.api.ApiUrls;

/**
 * 
 * @author Badu
 *
 *         Convert 404 into JSON message for Angular Application
 */
@RestController
public class CustomErrorController extends BasicErrorController {

	public CustomErrorController() {
		super(new DefaultErrorAttributes());
	}

	@RequestMapping(value = ApiUrls.ERROR_PATH, produces = ApiUrls.JSON)
	public ModelMap handleError(HttpServletRequest request) {
		return super.errorHtml(request).getModelMap();
	}

	@Override
	public String getErrorPath() {
		return ApiUrls.ERROR_PATH;
	}

}
