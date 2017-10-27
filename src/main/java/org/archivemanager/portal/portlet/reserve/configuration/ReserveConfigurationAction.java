package org.archivemanager.portal.portlet.reserve.configuration;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;

public class ReserveConfigurationAction extends DefaultConfigurationAction {

	
	public String render(PortletConfig config, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception { 

		return "/jsp/reserve/configuration.jsp";
		
	}
}