package org.archivemanager.portal.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.heed.openapps.data.RestResponse;
import org.heed.openapps.entity.EntityService;
import org.heed.openapps.search.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;


@Controller
@RequestMapping("/reserve")
public class DigitalReserveController {
	private EntityService entityService;
	private SearchService searchService;
	
	
	@ResponseBody
	@RequestMapping(value="/get.json", method = RequestMethod.GET)
	public RestResponse<Object> fetchEntity(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=false) Long id) throws Exception {
		RestResponse<Object> data = new RestResponse<Object>();
		
	
		
		
		return data;
	}
	
	
	public SearchService getSearchService() {
		if(searchService == null) {
			BeanLocator locator = PortletBeanLocatorUtil.getBeanLocator("archivemanager-portlet");
			if(locator != null)
				searchService = (SearchService)locator.locate("searchService");
		}
		return searchService;
	}
	public EntityService getEntityService() {
		if(entityService == null) {
			BeanLocator locator = PortletBeanLocatorUtil.getBeanLocator("archivemanager-portlet");
			if(locator != null)
				entityService = (EntityService)locator.locate("entityService");
		}
		return entityService;
	}
}
