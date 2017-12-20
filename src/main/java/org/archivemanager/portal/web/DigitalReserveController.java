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
import org.heed.openapps.security.SecurityService;
import org.heed.openapps.Group;

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
import com.liferay.portal.util.PortalUtil;


@Controller
@RequestMapping("/reserve")
public class DigitalReserveController {
    private EntityService entityService;
    private SearchService searchService;
    private SecurityService securityService;

    // @Autowired
    // User user;

    User user = new User();
    FormatInstructions inst = new FormatInstructions();
		//dummy has the list of subjects and their respective items
    Useri dummy = new Useri(user.getUsername(), user.getEmail());

    //a get request that returns all the current subjects
    @ResponseBody
    @RequestMapping(value="/subject/view", method = RequestMethod.GET)
    public RestResponse<Subject> viewSubjects(HttpServletRequest request, HttpServletResponse response) throws Exception{
      RestResponse<Subject> data = new RestResponse();
        entityService = getEntityService();
        Entity ent = entityService.getEntity(11);
        data.getResponse().setData(dummy.getSubjects());
        return data;
    }

    //just for testing: checking user's data
    @ResponseBody
    @RequestMapping(value="/user", method = RequestMethod.GET)
    public String viewUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
      String result = String.valueOf(checkEducator(request));
      return result;
    }

    //returns the new made subject or just returns the corresponding subject
    //going to be changed to post
    @ResponseBody
    @RequestMapping(value="/subject/add", method = RequestMethod.GET)
    public void addSubject(HttpServletRequest request, HttpServletResponse response, @RequestParam(required=true) String sub)throws Exception{
      if(checkEducator(request)){
        dummy.addSubject(sub);
      }
    }

    //deletes a whole subject
    //going to be changed to post
    @ResponseBody
    @RequestMapping(value="/subject/delete", method = RequestMethod.GET)
    public void deleteSubject(HttpServletRequest request, HttpServletResponse response, @RequestParam(required=true) String sub)throws Exception{
      if(checkEducator(request)){
        dummy.deleteSubject(sub);
      }
    }

    //add entity to a subject
    //going to be changed to post
    @ResponseBody
    @RequestMapping(value="/subject/add.json", method = RequestMethod.GET)
    public RestResponse<Object> addEntity(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(required=true) Long id,
                          @RequestParam(required=true) String sub) throws Exception {
        //add the entity to the list
        RestResponse data = new RestResponse();
        if(checkEducator(request)){
          entityService = getEntityService();
          Entity ent = entityService.getEntity(id);
          Object obj = entityService.export(inst, ent);
          dummy.addItem(sub, obj);
          data.getResponse().addData(obj);
        }
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
        if(checkEducator(request)){
          entityService = getEntityService();
          Entity ent = entityService.getEntity(id);
  				//how the subject looks like now
  				Subject updated = dummy.deleteItem(sub, entityService.export(inst, ent));
        }
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
  public SecurityService getSecurityService() {
    if(securityService == null) {
      BeanLocator locator = PortletBeanLocatorUtil.getBeanLocator("archivemanager-portlet");
      securityService = (SecurityService)locator.locate("securityService");
    }
    return securityService;
  }

  private boolean checkEducator(HttpServletRequest request) throws Exception{
    HttpServletRequest httpReq2 = PortalUtil.getOriginalServletRequest(request);
    User user2 = getSecurityService().getCurrentUser(request);
    List<Group> groups = user2.getGroups();
    int len = groups.size();
    if(len != 0){
      for(int i =0; i < len; i++){
        Group grp = groups.get(i);
        String nm = grp.getName();
        if(nm.equals("Educators")){
          return true;
        }
      }
    }
    return false;
  }


}
