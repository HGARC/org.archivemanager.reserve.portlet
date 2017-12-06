package org.archivemanager.portal.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//packages I used
import org.heed.openapps.entity.Entity;
import org.heed.openapps.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.heed.openapps.search.data.RestData;
import org.heed.openapps.entity.data.FormatInstructions;

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

    //@Autowired
    //User user;

    User user = new User();
    FormatInstructions inst = new FormatInstructions();
		//dummy has the list of subjects and their respective items
    Useri dummy = new Useri(user.getUsername(), user.getEmail());

    //a get request that returns all the current subjects
    @ResponseBody
    @RequestMapping(value="/subject/view", method = RequestMethod.GET)
    public RestResponse<Subject> viewSubjects() throws Exception{
      RestResponse<Subject> data = new RestResponse();
        entityService = getEntityService();
        Entity ent = entityService.getEntity(11);
        data.getResponse().setData(dummy.getSubjects());
        return data;
    }

    //returns the new made subject or just returns the corresponding subject
    //going to be changed to post
    @ResponseBody
    @RequestMapping(value="/subject/add", method = RequestMethod.GET)
    public void addSubject(@RequestParam(required=true) String sub){
        dummy.addSubject(sub);
    }

    //deletes a whole subject
    //going to be changed to post
    @ResponseBody
    @RequestMapping(value="/subject/delete", method = RequestMethod.GET)
    public void deleteSubject(@RequestParam(required=true) String sub){
        dummy.deleteSubject(sub);
    }

    //add entity to a subject
    //going to be changed to post
    @ResponseBody
    @RequestMapping(value="/subject/add.json", method = RequestMethod.GET)
    public RestResponse<Object> addEntity(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(required=true) Long id,
                          @RequestParam(required=true) String sub) throws Exception {
        //add the entity to the list
        entityService = getEntityService();
        Entity ent = entityService.getEntity(id);
        Object obj = entityService.export(inst, ent);
        dummy.addItem(sub, obj);
        RestResponse data = new RestResponse();
        data.getResponse().addData(obj);
        return data;
    }

    //delete the entity that belongs in a subject
    //going to be changed to post
    @ResponseBody
    @RequestMapping(value="/subject/delete.json", method = RequestMethod.GET)
    public void deleteEntity(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(required=true) Long id,
                               @RequestParam(required=true) String sub) throws Exception {
        //delete the entity to the list
        entityService = getEntityService();
        Entity ent = entityService.getEntity(id);
				//how the subject looks like now
				Subject updated = dummy.deleteItem(sub, entityService.export(inst, ent));
    }

	public SearchService getSearchService() {
		if(searchService == null) {
			BeanLocator locator = PortletBeanLocatorUtil.getBeanLocator("archivemanager-portlet");
			if(locator != null){
				searchService = (SearchService)locator.locate("searchService");
			}
		}
		return searchService;
	}
	public EntityService getEntityService() {
		if(entityService == null) {
			BeanLocator locator = PortletBeanLocatorUtil.getBeanLocator("archivemanager-portlet");
			if(locator != null) {
				entityService = (EntityService)locator.locate("entityService");
			}
		}
		return entityService;
	}
}
