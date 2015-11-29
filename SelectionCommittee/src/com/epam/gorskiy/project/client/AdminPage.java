package com.epam.gorskiy.project.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class AdminPage {
	private GreetingServiceAsync service = null;
	private final Registration registrationForm = new Registration();
	private FlexTable flexTable1 = new FlexTable();
	private DockPanel dock = new DockPanel();
	private MenuBar menu;
	private CheckBox[] allCheckBox = null;
	private Button enroll = new Button();
	private String login;

	public AdminPage(String login, GreetingServiceAsync service) {
		this.login = login;
		this.service = service;
		initMenu();
		initButton();
	}

	private void initButton() {
		enroll.setStyleName("styleButton");
		enroll.setText("Зачислить");
		enroll.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				List<String> data = new LinkedList<String>();
				for (CheckBox checkBox : allCheckBox) {
					if (checkBox.getValue()) {
						data.add(checkBox.getName());
					}
				}
				service.enrollTheStudents(data, new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						dock.remove(flexTable1);
						NewMessage.message("Данные обработаны", null);
					}

					@Override
					public void onFailure(Throwable caught) {
						dock.remove(flexTable1);
						NewMessage.message("Произошла ошибка", null);
						dock.add(flexTable1, DockPanel.SOUTH);
					}

				});

			}
		});
	}

	private void initMenu() {

		Command readDataCmd = new Command() {
			public void execute() {
				service.getApplicationEdmission(new AsyncCallback<List<String>>() {

					HTML getNewHTML(String string, String style) {
						HTML html = new HTML(string);
						html.setStyleName(style);
						return html;
					}

					CheckBox getNeCheckBox(String data) {
						CheckBox checkBox = new CheckBox();
						checkBox.setName(data);
						return checkBox;
					}

					@Override
					public void onSuccess(List<String> result) {


						FlexTable flexTable = new FlexTable();
						flexTable.setWidget(0, 1, getNewHTML("Имя", "htmlF"));
						flexTable.setWidget(0, 2, getNewHTML("Фамилия", "htmlF"));
						flexTable.setWidget(0, 3, getNewHTML("Отчество", "htmlF"));
						flexTable.setWidget(0, 4, getNewHTML("Дата рождения", "htmlF"));
						flexTable.setWidget(0, 5, getNewHTML("Институт", "htmlF"));
						flexTable.setWidget(0, 6, getNewHTML("Кафедра", "htmlF"));
						flexTable.setWidget(0, 7, getNewHTML("Предметы(Результат)", "htmlF"));
						int i = 0;
						allCheckBox = new CheckBox[result.size()];
						for (String elem : result) {
							String[] data = elem.split("\\;");
							allCheckBox[i] = getNeCheckBox(data[0]);
							flexTable.setWidget(i + 1, 0, allCheckBox[i]);
							for (int j = 1; j < data.length; j++) {
								flexTable.setWidget(i + 1, j, getNewHTML(data[j], "html"));
							}
							i++;
						}
						flexTable.setStyleName("myflexTable");
						flexTable.setCellPadding(4);
						flexTable.setCellSpacing(4);
		
						ScrollPanel sp = new ScrollPanel();
						sp.add(flexTable);
						sp.setAlwaysShowScrollBars(false);
						sp.setStyleName("myScroll");
						flexTable1.setWidget(0, 0, sp);
						flexTable1.setWidget(1, 0, enroll);
						dock.remove(registrationForm.getForm());
						dock.add(flexTable1, DockPanel.SOUTH);
					}

					@Override
					public void onFailure(Throwable caught) {
						NewMessage.message("Произошла ошибка при считывании данных", null);
					}
				});
			}
		};

		Command exitCmd = new Command() {
			public void execute() {
				exit();
			}

			private void exit() {
				RootLayoutPanel.get().remove(dock);
				RootLayoutPanel.get().add(SingIN.getPanel());
			}
		};

		Command deleteCmd = new Command() {
			public void execute() {
				delete();
			}

			private void delete() {
				service.deleteAdmin(login, new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							RootLayoutPanel.get().remove(dock);
							RootLayoutPanel.get().add(SingIN.getPanel());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
		};

		Command registrateNewAdmin = new Command() {
			public void execute() {
				dock.remove(flexTable1);
				dock.add(registrationForm.getForm(), DockPanel.SOUTH);
				registrationForm.getButtonApply().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						String[] data = registrationForm.getAllDataFromForm();
						service.registrateNewAdmin(data, new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {
								dock.remove(registrationForm.getForm());
								NewMessage.message(result, null);;
								dock.add(menu, DockPanel.CENTER);
							}

							@Override
							public void onFailure(Throwable caught) {
								dock.remove(registrationForm.getForm());
								NewMessage.message("Регистрация не выполнена", registrationForm.getForm());
							}
						});

					}
				});
				registrationForm.getButtonCancel().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						dock.remove(registrationForm.getForm());
					}
				});
			}
		};

		String styleMenuBar = "gwt-MenuBar";
		menu = new MenuBar();
		menu.setAutoOpen(true);
		menu.setWidth("500px");
		menu.setAnimationEnabled(true);

		MenuBar docMenu = new MenuBar(true);
		docMenu.addItem("Просмотреть поданные документы", readDataCmd);
		docMenu.setStyleName("gwt-MenuBar");
		MenuItem docItem = new MenuItem("[Документы]", docMenu);

		MenuBar persAccountMenu = new MenuBar(true);
		persAccountMenu.addItem("Удалить учетную запись", deleteCmd);
		persAccountMenu.addItem("Зарегестрировать администратора", registrateNewAdmin);
		persAccountMenu.addItem("Выход", exitCmd);
		persAccountMenu.setStyleName(styleMenuBar);
		persAccountMenu.addSeparator();
		MenuItem persAccounItem = new MenuItem("[Личный кабинет]", persAccountMenu);
		menu.addItem(docItem);
		menu.addItem(persAccounItem);
		menu.setStyleName(styleMenuBar);

		dock.setSpacing(1);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		dock.setStylePrimaryName("styleBackgroundSingIn");
		dock.add(menu, DockPanel.CENTER);
	}

	public DockPanel getForm() {
		return dock;
	}
}
