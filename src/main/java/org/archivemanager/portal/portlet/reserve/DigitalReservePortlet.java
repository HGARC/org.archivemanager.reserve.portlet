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
		include("/jsp/viewer.jsp", renderRequest, renderResponse);
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
