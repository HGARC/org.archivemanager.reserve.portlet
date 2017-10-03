package org.archivemanager.portal.portlet.reserve;

import java.io.IOException;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.archivemanager.model.Attribute;
import org.archivemanager.model.AttributeValue;
import org.archivemanager.model.Breadcrumb;
import org.archivemanager.model.Item;
import org.archivemanager.model.Paging;
import org.archivemanager.model.Result;
import org.archivemanager.model.ResultSet;
import org.archivemanager.util.EntityRepositoryModelUtil;
import org.heed.openapps.User;
import org.heed.openapps.data.Sort;
import org.heed.openapps.dictionary.RepositoryModel;
import org.heed.openapps.search.SearchAttribute;
import org.heed.openapps.search.SearchAttributeValue;
import org.heed.openapps.search.SearchNode;
import org.heed.openapps.search.SearchRequest;
import org.heed.openapps.search.SearchResponse;
import org.heed.openapps.search.SearchResult;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;


public class DigitalReservePortlet extends PortletSupport {
	private static Log log = LogFactoryUtil.getLog(DigitalReservePortlet.class);
	
	
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		HttpServletRequest httpReq = PortalUtil.getHttpServletRequest(renderRequest);
		HttpServletRequest httpReq2 = PortalUtil.getOriginalServletRequest(httpReq);
		try {
			PermissionChecker permissionChecker = PermissionCheckerFactoryUtil.create(themeDisplay.getUser());
			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		} catch(Exception e) {
			e.printStackTrace();
		}
		String id = httpReq2.getParameter("id");
		String[] sorts = new String[] {"name_e"};
		String size = "10";
		
		String query = httpReq2.getParameter("query") != null ? httpReq2.getParameter("query") : "";
		String page = httpReq2.getParameter("page") != null ? httpReq2.getParameter("page") : "1";
		
		ResultSet results = new ResultSet();
		int end = Integer.valueOf(size) * Integer.valueOf(page);
		int start = end - Integer.valueOf(size);
		
		if(id != null && !query.contains("source_assoc:"+id)) {
			query += " source_assoc:"+id;
		}
		if(id != null && id.length() > 0) {				
			renderRequest.setAttribute("baseUrl", "?id="+id+"&");
		} else {
			renderRequest.setAttribute("baseUrl", "");
		}			
		renderRequest.getPortletSession().setAttribute("LIFERAY_SHARED_QUERY", query, PortletSession.APPLICATION_SCOPE);
		User user = getSecurityService().getCurrentUser(httpReq2);
			
		SearchRequest sQuery = new SearchRequest(RepositoryModel.ITEM, query);
		sQuery.setAttributes(true);
		sQuery.setStartRow(start);
		sQuery.setEndRow(end);
		if(sorts != null) {
			for(String sort : sorts) {
				Sort lSort = null;
				String[] sortStrings = sort.split(",");
				for(String sortStr : sortStrings) {
					String[] s = sortStr.split(" ");
					if(s.length == 2) {
						boolean reverse = s[1].equals("asc") ? true : false;
						if(s[0].endsWith("_")) lSort = new Sort(Sort.LONG, s[0], reverse);
						else lSort = new Sort(Sort.STRING, s[0], reverse);						
					} else if(s.length == 1) {
						if(s[0].endsWith("_")) lSort = new Sort(Sort.LONG, s[0], true);
						else lSort = new Sort(Sort.STRING, s[0], false);
					}
					sQuery.addSort(lSort);
				}
			}
		}
		sQuery.setUser(user);
				
		SearchResponse searchResponse = getSearchService().search(sQuery);
		if(searchResponse != null) {
		results.setQuery(query.trim());
		
			int maxFieldSize = PrefsParamUtil.getInteger(renderRequest.getPreferences(), renderRequest, "maxFieldSize", 0);
			results.setStart(searchResponse.getStartRow());
			results.setEnd(searchResponse.getEndRow());
			results.setTime(searchResponse.getTime());
			results.setResultCount(searchResponse.getResultSize());
			results.setQuery(query);
			results.setPageSize(Integer.valueOf(size));
			EntityRepositoryModelUtil modelUtility = new EntityRepositoryModelUtil(getEntityService());
			
			for(SearchResult searchResult :searchResponse.getResults()) {
				Result result = modelUtility.getItem(searchResult.getEntity());								
				
				if(result != null) {
					if(result.getDescription() != null && maxFieldSize > 0) {
						if(result.getDescription().length() > 250) {
							result.setDescription(result.getDescription().substring(0, 250)+"...");
						}
					}
					if(result instanceof Item && maxFieldSize > 0) {
						Item item = (Item)result;
						if(item.getSummary() != null) {
							if(item.getSummary().length() > maxFieldSize) {
								item.setSummary(item.getSummary().substring(0, maxFieldSize)+"...");
							}
						}
					}
					results.getResults().add(result);
				}
			}
			if(!searchResponse.getAttributes().isEmpty()) {
				for(SearchAttribute att : searchResponse.getAttributes()) {
					Attribute attribute = new Attribute(att.getName());
					for(SearchAttributeValue valueNode : att.getValues()) {
						String name = valueNode.getName();
						String pageQuery = valueNode.getQuery().replace("//",  "/");
						if(results.getPageSize() != 10) pageQuery += "&size="+results.getPageSize();
						if(results.getSort() != null) pageQuery += "&sort="+results.getSort();
						AttributeValue value = new AttributeValue(name, pageQuery, String.valueOf(valueNode.getCount()));
						attribute.getValues().add(value);
					}
					attribute.setCount(String.valueOf(attribute.getValues().size()));
					if(attribute.getName().equals("Collections") && attribute.getValues().size() <= 1) attribute.setDisplay(false);
					else if(attribute.getValues().size() == 0) attribute.setDisplay(false);
					else attribute.setDisplay(true);
					results.getAttributes().add(attribute);
				}
			}
			if(!searchResponse.getBreadcrumb().isEmpty()) {
				for(SearchNode crumbNode : searchResponse.getBreadcrumb()) {
					String pageQuery = crumbNode.getQuery().replace("//",  "/").trim();
					if(crumbNode.getLabel() != null) {
						Breadcrumb crumb = new Breadcrumb(crumbNode.getLabel().trim(), pageQuery);
						results.getBreadcrumbs().add(crumb);
						results.setQuery(crumb.getQuery());
						results.setLabel(crumb.getName());
					}
				}
			}
			doPaging(results, query, Integer.valueOf(page), "");
		}
						
		include("/jsp/viewer.jsp", renderRequest, renderResponse);
	}
	
		protected void doPaging(ResultSet results, String query, int currentPage, String parameters) {
			int pageCount = 0;		
			if(results.getResultCount() > results.getPageSize()) {
				double ratio = (double)results.getResultCount() / results.getPageSize();
				pageCount = (int)(Math.ceil(ratio));
			}
			results.setPageCount(pageCount);
			results.setPage(currentPage);
			
			if(query != null && !query.equals("all results")) {
				results.setQuery("query="+query);
			} else {
				results.setQuery("query="+parameters);
			}
			
			if(results.getPageSize() != 10) {
				for(Breadcrumb page : results.getBreadcrumbs()) {
					page.setQuery(page.getQuery() + "&size="+results.getPageSize());
				}
			}
			if(results.getSort() != null) {
				for(Breadcrumb page : results.getBreadcrumbs()) {
					page.setQuery(page.getQuery() + "&sort="+results.getSort());
				}
			}	
			
			int startPage = currentPage < 5 ? 1 : currentPage - 4;
			int endPage = (pageCount >= 10) ? 10 : pageCount;
			/*
			if(currentPage == 1) {
				String pageQuery = results.getQuery()+"&page=1&size="+results.getPageSize();
				if(results.getSort() != null) pageQuery += "&sort="+results.getSort();
				results.getPaging().add(new Paging("1", pageQuery));
			}
			*/
			if(results.getPage() > 1 && pageCount > 1) {
				String pageQuery = results.getQuery()+"&page="+(currentPage - 1);
				pageQuery += "&size="+results.getPageSize();
				if(results.getSort() != null) pageQuery += "&sort="+results.getSort();
				results.getPaging().add(new Paging("Previous", pageQuery));
			}
			for(int i = startPage; i <= endPage; i++) {
				String pageQuery = results.getQuery()+"&page="+i;
				pageQuery += "&size="+results.getPageSize();
				if(results.getSort() != null) pageQuery += "&sort="+results.getSort();
				results.getPaging().add(new Paging(String.valueOf(i), pageQuery));
			}
			if(pageCount > currentPage) {
				String pageQuery = results.getQuery()+"&page="+(currentPage + 1);
				pageQuery += "&size="+results.getPageSize();
				if(results.getSort() != null) pageQuery += "&sort="+results.getSort();
				results.getPaging().add(new Paging("Next", pageQuery));
			}
		}
	protected void include(String path, RenderRequest renderRequest, RenderResponse renderResponse)	throws IOException, PortletException {
		PortletRequestDispatcher portletRequestDispatcher =	getPortletContext().getRequestDispatcher(path);
		if (portletRequestDispatcher == null) {
			log.error(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}
}
