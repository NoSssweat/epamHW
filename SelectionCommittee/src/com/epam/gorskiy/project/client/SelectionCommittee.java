package com.epam.gorskiy.project.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SelectionCommittee implements EntryPoint {

	private SingIN loggin;

	private final GreetingServiceAsync service = GWT.create(GreetingService.class);

	public void onModuleLoad() {

		loggin = new SingIN(service);
		RootLayoutPanel.get().add(SingIN.getPanel());

	}

}
