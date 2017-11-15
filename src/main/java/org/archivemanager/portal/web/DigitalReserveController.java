package org.archivemanager.portal.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.heed.openapps.entity.Entity;
import org.heed.openapps.search.SearchResult;

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

	List<Entity> list = new ArrayList<Entity>();
	List<Long> list_id = new ArrayList<Long>();

	@ResponseBody
	@RequestMapping(value="/get.json", method = RequestMethod.GET)
	public String getEntity(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=false) Long id) throws Exception {
				//add an the entity if provided an id
				if(id != null){
					if(!list_id.contains(id)){
						entityService = getEntityService();
						Entity ent = entityService.getEntity(id);
						list.add(ent);
						list_id.add(id);
					}
				}
				//return the current list of entities' uid's after iterating through them
				int len = list.size();
				String result = "";
				for(int i = 0; i<len; i++){
					Entity ent = list.get(i);
					SearchResult something = new SearchResult(ent);
					result = result + "\n" + something.getUid();
				}
				return result;
	}

	@ResponseBody
	@RequestMapping(value="/add.json", method = RequestMethod.GET)
	public void addEntity(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=true) Long id) throws Exception {
				//add the entity to the list
			if(!list_id.contains(id)){
		    entityService = getEntityService();
		    Entity ent = entityService.getEntity(id);
				list.add(ent);
				list_id.add(id);
			}
	}

	@ResponseBody
	@RequestMapping(value="/delete.json", method = RequestMethod.GET)
	public String deleteEntity(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=true) Long id) throws Exception {
				//delete the entity to the list
        entityService = getEntityService();
        Entity ent = entityService.getEntity(id);
				boolean yon = list.remove(ent);
				return "DELETED: " + String.valueOf(yon);
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
