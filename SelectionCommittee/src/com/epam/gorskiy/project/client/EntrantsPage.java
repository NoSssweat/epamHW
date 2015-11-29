package com.epam.gorskiy.project.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;

public class EntrantsPage {
	
	private MenuBar menu;
	private DockPanel dock;
	private EntrantsTable entrantsTable = null;
	private FlexTable flexTableForInstitutes = new FlexTable();
	private Button send = new Button();
	private TextBox error = new TextBox();
	private String errorMassege = "Максимальное количество баллов по предмету равно 100";
	private DecoratorPanel decPanelRes = new DecoratorPanel();
	private DecoratorPanel decPanelDoc = new DecoratorPanel();
	private String login = "";
	private GreetingServiceAsync service = null;

	public EntrantsPage(String login, GreetingServiceAsync service) {
		this.login = login;
		this.service = service;
		send.setText("Отправить");
		send.setStyleName("styleButton");
		error.setStyleName("errorText");
		error.setEnabled(false);
		error.setMaxLength(errorMassege.length());
		error.setSize("400px", "20px");
		initMenu();
		sendListner();
	}

	public Button getSend() {
		return send;
	}

	public EntrantsTable getScoresForm() {
		return entrantsTable;
	}

	Command cmdDocuments = new Command() {
		public void execute() {
			documentsForm();
		}
	};

	public void setErrorText(boolean isError) {
		if (isError) {
			error.setText(errorMassege);
		} else {
			error.setText("");
		}
	}

	public void sendListner() {
		send.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String[] data = getScoresForm().getInstitutesData();
				int[] scores = getScoresForm().getScres();
				boolean isError = false;
				for (int i = 0; i < scores.length; i++) {
					if (scores[i] > 100) {
						isError = true;
						setErrorText(isError);
						break;
					}
				}
				if (!isError) {
					service.sendApplicationForAdmission(login, scores, data, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							NewMessage.message("Произошла ошибка при отправке", null);
						}

						@Override
						public void onSuccess(Boolean result) {
							dock.remove(decPanelDoc);
							NewMessage.message("Данные отправлены на обработку", null);
						}
					});
				}
			}
		});
	}

	private void resultForm() {
		service.getDataEntrant(login, new AsyncCallback<String>() {

			HTML getNewHTML(String string, String style) {
				HTML html = new HTML(string);
				html.setStyleName(style);
				return html;
			}

			@Override
			public void onSuccess(String result) {
				
				HTML surname = getNewHTML("Фамилия: ", "html");
				HTML name = getNewHTML("Имя: ", "html");
				HTML patronymic = getNewHTML("Отчество: ", "html");
				HTML scores = getNewHTML("Дата рождения: ", "html");
				HTML resutl = getNewHTML("Результат: ", "html");
				
				FlexTable flexTableRes = new FlexTable();
				flexTableRes.setWidget(1, 0, surname);
				flexTableRes.setWidget(2, 0, name);
				flexTableRes.setWidget(3, 0, patronymic);
				flexTableRes.setWidget(4, 0, scores);
				flexTableRes.setWidget(5, 0, resutl);

				int i = 1;
				String[] data = result.split("\\;");
				for (String string : data) {
					flexTableRes.setWidget(i++, 1, getNewHTML(string, "html"));
				}
				
				decPanelRes.setWidget(flexTableRes);
				decPanelRes.setStylePrimaryName("styleLayout");

				dock.remove(decPanelDoc);
				dock.add(decPanelRes, DockPanel.SOUTH);

			}

			@Override
			public void onFailure(Throwable caught) {
				NewMessage.message("Произошла ошибка при считывании данных", null);
			}

		});

	}

	private void documentsForm() {

		if (entrantsTable == null) {
			
			entrantsTable = new EntrantsTable(service);
		
			flexTableForInstitutes.setCellSpacing(4);
			flexTableForInstitutes.setWidget(0, 0, entrantsTable.getListBoxInst());
			flexTableForInstitutes.setWidget(1, 0, entrantsTable.getListBoxDep());
			
			HTML html = new HTML("Баллы по ЕГЭ:");
			html.setStyleName("html");
			flexTableForInstitutes.setWidget(3, 0, html);
			flexTableForInstitutes.setWidget(4, 0, entrantsTable.getScoresForm());
			flexTableForInstitutes.setWidget(5, 0, error);
			flexTableForInstitutes.setWidget(6, 0, send);

			decPanelDoc.setWidget(flexTableForInstitutes);
			decPanelDoc.setStylePrimaryName("styleLayout");
		}
		dock.remove(decPanelRes);
		dock.add(decPanelDoc, DockPanel.SOUTH);
	}

	Command documentCmd = new Command() {
		public void execute() {
			documentsForm();
		}
	};

	Command resultCmd = new Command() {
		public void execute() {
			resultForm();
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
			service.deleteEntrant(login, new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if(result) {
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

	private void initMenu() {
		
		String styleMenuBar = "gwt-MenuBar";
		menu = new MenuBar();
		menu.setAutoOpen(true);
		menu.setWidth("500px");
		menu.setAnimationEnabled(true);

		MenuBar docMenu = new MenuBar(true);
		docMenu.addItem("Подать документы", documentCmd);
		docMenu.addItem("Результаты приема", resultCmd);
		docMenu.setStyleName("gwt-MenuBar");
		MenuItemSeparator mSeparatorHor = new MenuItemSeparator();
		mSeparatorHor.setStyleName("SeparatorHor");
		docMenu.insertSeparator(mSeparatorHor, 1);
		docMenu.addSeparator();
		MenuItem docItem = new MenuItem("[Документы]", docMenu);

		MenuBar persAccountMenu = new MenuBar(true);
		persAccountMenu.addItem("Удалить учетную запись", deleteCmd);
		persAccountMenu.addItem("Выход", exitCmd);
		persAccountMenu.setStyleName(styleMenuBar);
		persAccountMenu.addSeparator();
		MenuItem persAccounItem = new MenuItem("[Личный кабинет]", persAccountMenu);
		menu.addItem(docItem);
		menu.addItem(persAccounItem);
		menu.setStyleName(styleMenuBar);

		dock = new DockPanel();
		dock.setSpacing(1);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		dock.setStylePrimaryName("styleBackgroundSingIn");
		dock.add(menu, DockPanel.CENTER);
	}

	public DockPanel getForm() {
		return dock;
	}

}
