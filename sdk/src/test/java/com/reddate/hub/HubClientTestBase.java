package com.reddate.hub;

public class HubClientTestBase {

	public com.reddate.hub.sdk.HubClient getDidClient() {
		String url = "https://didservice.bsngate.com:18602";
		String token = "3wxYHXwAm57grc9JUr2zrPHt9HC";
		String projectId = "8320935187";
			
		com.reddate.hub.sdk.HubClient hubClient = new com.reddate.hub.sdk.HubClient(url,projectId,token);
		return hubClient;
	}

}
