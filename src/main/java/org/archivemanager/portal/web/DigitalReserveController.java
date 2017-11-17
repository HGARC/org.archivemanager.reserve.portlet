package org.archivemanager.portal.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.heed.openapps.entity.Entity;
import org.heed.openapps.search.SearchResult;
import org.heed.openapps.QName;

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

	Subjects subjectsList = new Subjects();

  //a get request that returns all the current subjects
  @ResponseBody
	@RequestMapping(value="/subject/view", method = RequestMethod.GET)
  public String viewSubjects(){
    List<Subject> subjects = subjectsList.getListSub();
	  int len = subjects.size();
		String result = "";
		for(int i = 0; i < len; i++){
			result = result + printSubject(subjects.get(i));
		}
		return result;
  }

  //returns the new made subject or just returns the corresponding subject
	@ResponseBody
  @RequestMapping(value="/subject/add", method = RequestMethod.GET)
  public String addSubject(@RequestParam(required=true) String sub){
    return printSubject(subjectsList.addSubject(sub));
  }


  //deletes a whole subject
	@ResponseBody
  @RequestMapping(value="/subject/delete", method = RequestMethod.GET)
  public void deleteSubject(@RequestParam(required=true) String sub){
    subjectsList.deleteSubject(sub);
  }

	//add entity to a subject
	@ResponseBody
	@RequestMapping(value="/subject/add.json", method = RequestMethod.GET)
	public void addEntity(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=true) Long id,
			@RequestParam(required=true) String sub) throws Exception {
				//add the entity to the list
				entityService = getEntityService();
				Entity ent = entityService.getEntity(id);
				subjectsList.addItem(sub, ent);
	}

  //delete the entity that belongs in a subject
	@ResponseBody
	@RequestMapping(value="/subject/delete.json", method = RequestMethod.GET)
	public String deleteEntity(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=true) Long id,
			@RequestParam(required=true) String sub) throws Exception {
				//delete the entity to the list
				entityService = getEntityService();
				Entity ent = entityService.getEntity(id);
				Subject updated = subjectsList.deleteItem(sub, ent);
				return "updated to: " + "<br>"+ printSubject(deleted);
	}

	//this method is just to make sure we have the entities.
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
					result = result + "<br>" + something.getUid();
				}
				return result;
	}

//----------here are the helper methods------------

	//From Entity object to a String (i need json)
	public String printEntity(Entity ent){
		String result = "UID: " + ent.getUid();
		result = result + "<br>"+ "ID: " + Long.toString(ent.getId());
		result = result + "<br>"+ "Name: " + ent.getName();
		result = result + "<br>" + "Source: " + ent.getSource() + "\n";
		result = result + "<br>" + "QName: " + ent.getQName();
		return result+"<br>";
	}

	//From Subject Object to String (i need json)
	public String printSubject(Subject sub){
		List<Entity> listEnt = sub.getList();
		int len = listEnt.size();
		String result = "SUBJECT: " + sub.getId();
		for(int i = 0; i < len; i++){
			result = result + "<br>" + printEntity(listEnt.get(i));
		}
		return result + "<br>";
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
