package com.epam.gorskiy.project.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewMessage {
	static public void message(String message, final Widget addWidget) {
		final DockPanel dock = new DockPanel();
		dock.setSpacing(1);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		dock.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
		dock.setStylePrimaryName("styleBackgroundRegistrateFormCancle");
		HTML html = new HTML("<h1>" + message + "</h1>");
		html.setStyleName("htmlMessage");
		dock.add(html, DockPanel.CENTER);

		RootLayoutPanel.get().add(dock);
		Timer timer = new Timer() {
			@Override
			public void run() {
				RootLayoutPanel.get().remove(dock);
				if (addWidget != null)
					RootLayoutPanel.get().add(addWidget);
			}
		};
		timer.schedule(3000);
	}

}
