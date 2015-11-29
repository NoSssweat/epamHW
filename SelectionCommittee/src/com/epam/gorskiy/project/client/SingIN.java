package com.epam.gorskiy.project.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;


public class SingIN {
	private static DockPanel dockPanel;
	private Button singIn;
	private Button registration;
	private TextBox error = new TextBox();
	private TextBox userID = new TextBox();
	private PasswordTextBox password = new PasswordTextBox();
	private FlexTable layout = new FlexTable();
	private FlexTable flexTableForButton = new FlexTable();
	private GreetingServiceAsync service = null;
	
	private static Registration registerForm;
	private String login = "";
	private static EntrantsPage entrantsPage;
	private static AdminPage adminPage;

	public SingIN(GreetingServiceAsync service) {
		this.service = service;
		initButton();
		dockPanel = registrationTabPanel();
		clickButtons();
	}

	private static void focus(final TextBox box, final String text) {

		box.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				box.selectAll();
			}
		});

		box.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (box.getText().equals("")) {
					box.setText(text);
				}
			}
		});

	}

	private DecoratorPanel singInPanel() {

		userID.setWidth("200px");
		userID.setMaxLength(25);
		userID.setText("Логин");
		userID.setTitle("Введите логин");
		userID.setStyleName("styleTextBox");
		focus(userID, "Логин");
		password.setMaxLength(20);
		password.setWidth("200px");
		password.setText("Пароль");
		password.setTitle("Введите пароль");
		password.setStyleName("styleTextBox");
		focus(password, "Пароль");
		error.setStyleName("errorText");
		error.setEnabled(false);
		
		layout.setCellSpacing(12);
		layout.setWidget(0, 0, error);
		layout.setWidget(1, 0, userID);
		layout.setWidget(2, 0, password);
		flexTableForButton.setCellSpacing(4);
		flexTableForButton.setWidget(0, 0, singIn);
		flexTableForButton.setWidget(0, 1, registration);
		layout.setWidget(3, 0, flexTableForButton);

		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(layout);
		decPanel.setStylePrimaryName("styleLayout");

		return decPanel;
	}

	private void initButton() {
		singIn = new Button();
		singIn.setText("Вход");
		singIn.setStylePrimaryName("styleButton");
		registration = new Button();
		registration.setText("Регистрация");
		registration.setStylePrimaryName("styleButton");
	}

	private DockPanel registrationTabPanel() {

		DockPanel dock = new DockPanel();
		dock.setSpacing(1);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		dock.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
		dock.setStylePrimaryName("styleBackgroundSingIn");
		dock.add(singInPanel(), DockPanel.CENTER);

		return dock;
	}
	
	private void clickButtons() {
		singIn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setErrorText(false);
				login = getLogin();
				service.isSingIn(getLogin(), getPassword(), new AsyncCallback<Integer>() {
					@Override
					public void onSuccess(Integer result) {
						if (result == 2) {
							setLogin("Логин");
							setPassword("Пароль");
							entrantsPage = new EntrantsPage(login, service);
							entrantsPage.setErrorText(false);
							RootLayoutPanel.get().remove(getPanel());
							RootLayoutPanel.get().add(entrantsPage.getForm());
						} else if (result == 1) {
							setLogin("Логин");
							setPassword("Пароль");
							adminPage = new AdminPage(login, service);
							RootLayoutPanel.get().remove(getPanel());
							RootLayoutPanel.get().add(adminPage.getForm());
						} else {
							setErrorText(true);							
						}
					}
					@Override
					public void onFailure(Throwable caught) {
					}
				});
				
			}
		});

		registration.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setLogin("Логин");
				setPassword("Пароль");
				RootLayoutPanel.get().remove(getPanel());
				registerForm = new Registration();
				RootLayoutPanel.get().add(registerForm.getForm());

				registerForm.getButtonCancel().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						RootLayoutPanel.get().remove(registerForm.getForm());
						RootLayoutPanel.get().add(getPanel());
					}
				});

				registerForm.getButtonApply().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						
						registratNewAbiturient(registerForm.getAllDataFromForm());
					}

					private void registratNewAbiturient(String[] data) {
						service.registrateNewEntrant(data, new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {
								RootLayoutPanel.get().remove(registerForm.getForm());
								NewMessage.message(result, getPanel());
							}

							@Override
							public void onFailure(Throwable caught) {
								RootLayoutPanel.get().remove(registerForm.getForm());
								NewMessage.message("Регистрация не выполнена", registerForm.getForm());
							}

							
						});
					}
				});
			}
		});
	}

	public static DockPanel getPanel() {
		return dockPanel;
	}

	public Button getSingIn() {
		return singIn;
	}

	public Button getRegistration() {
		return registration;
	}

	public void setErrorText(boolean isError) {
		if (isError) {
			error.setText("Неверный логин или пароль");
		} else {
			error.setText("");
		}
	}

	public String getLogin() {
		return userID.getText();
	}

	public String getPassword() {
		return this.password.getText();
	}
	
	public void setLogin(String login) {
		userID.setText(login);
	}

	public void setPassword(String password) {
		this.password.setText(password);
	}

}
