package com.epam.gorskiy.project.client;

import java.util.LinkedList;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class Registration {
	private DockPanel dock = new DockPanel();
	private Button buttonCancel;
	private Button buttonApply;
	private TextBox surnameForm = new TextBox();
	private TextBox nameForm = new TextBox();
	private TextBox patronymicForm = new TextBox();
	private TextBox birthdayForm = new TextBox();
	private FlexTable flexTable = new FlexTable();
	private List<TextBox> listTextBox = new LinkedList<TextBox>();
	private DateBox dateBox = new DateBox();

	public Registration() {
		initButton();
		form();
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

	private void initDateBox(String name) {
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		dateBox.getDatePicker().setYearArrowsVisible(true);
		dateBox.setStyleName("styleTextBox");
		dateBox.getTextBox().setText(name);
	}

	private TextBox addNewTextBox(String name) {
		TextBox newTextBox = new TextBox();
		newTextBox.setText(name);
		newTextBox.setName(name);
		newTextBox.setStylePrimaryName("styleTextBox");
		focus(newTextBox, name);
		return newTextBox;
	}

	private void form() {
		listTextBox.add(addNewTextBox("Фамилия"));
		listTextBox.add(addNewTextBox("Имя"));
		listTextBox.add(addNewTextBox("Отчество"));
		listTextBox.add(addNewTextBox("Введите логин"));
		listTextBox.add(addNewTextBox("Ввведите пароль"));

		initDateBox("Дата рождения");

		flexTable.setCellSpacing(16);
		flexTable.setWidget(0, 0, new HTML("<h1> Регистрация </h1>"));
		int i = 1;
		for (TextBox element : listTextBox) {
			flexTable.setWidget(i++, 0, element);
			if (element.getText().equals("Отчество")) {
				flexTable.setWidget(i++, 0, dateBox);
			}
		}
		FlexTable flexTableForButton = new FlexTable();
		flexTableForButton.setCellSpacing(4);
		flexTableForButton.setWidget(0, 0, buttonApply);
		flexTableForButton.setWidget(0, 1, buttonCancel);
		flexTable.setWidget(i, 0, flexTableForButton);

		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(flexTable);
		decPanel.setStylePrimaryName("styleLayout");

		dock.setSpacing(1);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		dock.setStylePrimaryName("styleBackgroundRegistrateForm");
		dock.add(decPanel, DockPanel.CENTER);
	}

	private void initButton() {
		buttonCancel = new Button();
		buttonCancel.setText("Назад");
		buttonCancel.setStylePrimaryName("styleButton");
		buttonApply = new Button();
		buttonApply.setText("Применить");
		buttonApply.setStylePrimaryName("styleButton");
	}

	public Button getButtonCancel() {
		return buttonCancel;
	}

	public Button getButtonApply() {
		return buttonApply;
	}

	public DockPanel getForm() {
		return dock;
	}

	public String getSurname() {
		return surnameForm.getText();
	}

	public String getName() {
		return nameForm.getText();
	}

	public String getPatronymic() {
		return patronymicForm.getText();
	}

	public String getBirthday() {
		return birthdayForm.getText();
	}

	public String[] getAllDataFromForm() {
		String[] data = new String[listTextBox.size()];
		int i = 0;
		for (TextBox element : listTextBox) {
			data[i++] = element.getText();
			if (element.getName().equals("Отчество")) {
				data[i++] = dateBox.getTextBox().getText();
			}

		}
		return data;
	}

}
