package com.epam.gorskiy.project.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class EntrantsTable {

	private ListBox listBoxInst = new ListBox();
	private ListBox listBoxDep = new ListBox();
	private FlexTable flexTableForInstitutes = new FlexTable();
	private TextBox newTextBox1 = new TextBox();
	private TextBox newTextBox2 = new TextBox();
	private TextBox newTextBox3 = new TextBox();
	private GreetingServiceAsync service;
	private String[] institutesName;
	private String[][] department;
	private String[][] subjects;

	public int[] getScres() {
		int[] scres = { Integer.parseInt(newTextBox1.getText()), Integer.parseInt(newTextBox2.getText()),
				Integer.parseInt(newTextBox3.getText()) };
		return scres;
	}

	public String[] getInstitutesData() {
		String[] dep = department[listBoxInst.getSelectedIndex()];
		String[] data = { institutesName[listBoxInst.getSelectedIndex()], dep[listBoxDep.getSelectedIndex()] };
		return data;
	}

	public EntrantsTable(GreetingServiceAsync service) {
		this.service = service;
		getInstitutes();
		listBoxInst.setVisibleItemCount(1);
		listBoxDep.setVisibleItemCount(1);
		listBoxInst.setSize("450px", "20px");
		listBoxDep.setSize("450px", "20px");
		listBoxInst.setStyleName("ListBox");
		listBoxDep.setStyleName("ListBox");
		
		listBoxInst.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				flexTableForInstitutes.setWidget(0, 0,
						listBoxScores(listBoxInst.getSelectedIndex()));
			}
		});
	}


	void getInstitutesBox(String[] listData) {
		for (int i = 0; i < listData.length; i++) {
			listBoxInst.addItem(listData[i]);
		}

		listBoxInst.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				getDepartmentBox(listBoxInst.getSelectedIndex());
			}
		});
	}
	
	void getDepartmentBox(int index) {
		listBoxDep.clear();
		getDepartmentBox(department[index]);
	}
	
	void getDepartmentBox(String[] data) {
		for (int i = 0; i < data.length; i++) {
			listBoxDep.addItem(data[i]);
		}
	}

	void initTextBox(final TextBox textBox) {
		textBox.setStylePrimaryName("styleTextBox");
		textBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int keyCode = event.getNativeKeyCode();

				if (event.isAnyModifierKeyDown())
					textBox.cancelKey();

				if (parseNumberKey(keyCode)) {
					return;
				} else {
					textBox.cancelKey();
				}

			}

			@SuppressWarnings("nls")
			private boolean parseNumberKey(int keyCode) {
				
				switch (keyCode) {
				case KeyCodes.KEY_ZERO:
				case KeyCodes.KEY_ONE:
				case KeyCodes.KEY_TWO:
				case KeyCodes.KEY_THREE:
				case KeyCodes.KEY_FOUR:
				case KeyCodes.KEY_FIVE:
				case KeyCodes.KEY_SIX:
				case KeyCodes.KEY_SEVEN:
				case KeyCodes.KEY_EIGHT:
				case KeyCodes.KEY_NINE:
				case KeyCodes.KEY_NUM_ZERO:
				case KeyCodes.KEY_NUM_ONE:
				case KeyCodes.KEY_NUM_TWO:
				case KeyCodes.KEY_NUM_THREE:
				case KeyCodes.KEY_NUM_FOUR:
				case KeyCodes.KEY_NUM_FIVE:
				case KeyCodes.KEY_NUM_SIX:
				case KeyCodes.KEY_NUM_SEVEN:
				case KeyCodes.KEY_NUM_EIGHT:
				case KeyCodes.KEY_LEFT:
				case KeyCodes.KEY_RIGHT:
				case KeyCodes.KEY_DELETE:
				case KeyCodes.KEY_BACKSPACE:
				case KeyCodes.KEY_ENTER:
				case KeyCodes.KEY_ESCAPE:
				case KeyCodes.KEY_TAB:
					return true;
				}
				return false;
			}
		});
		textBox.setMaxLength(3);
	}
	
	private FlexTable listBoxScores(String[] subject) {

		HTML html1 = new HTML(subject[0]);
		HTML html3 = new HTML(subject[1]);
		HTML html2 = new HTML(subject[2]);		
		html1.setStyleName("html");
		html2.setStyleName("html");
		html3.setStyleName("html");
		
		initTextBox(newTextBox1);
		initTextBox(newTextBox2);
		initTextBox(newTextBox3);
		
		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(4);
		flexTable.setWidget(0, 0, html1);
		flexTable.setWidget(1, 0, newTextBox1);
		flexTable.setWidget(2, 0, html3);
		flexTable.setWidget(3, 0, newTextBox2);
		flexTable.setWidget(4, 0, html2);
		flexTable.setWidget(5, 0, newTextBox3);
		return flexTable;
	}

	private FlexTable listBoxScores(int instNum) {

		return listBoxScores(subjects[instNum]);
	}
	
	
	public FlexTable getScoresForm() {
		return flexTableForInstitutes;
	}

	public ListBox getListBoxInst() {
		return listBoxInst;
	}

	public ListBox getListBoxDep() {
		return listBoxDep;
	}
	
	private void getInstitutes() {
		service.getInstitutes(new AsyncCallback<String[]>(){

			@Override
			public void onSuccess(String[] result) {
				institutesName = result;
				getInstitutesBox(institutesName);
				department = new String[institutesName.length][];
				for(int i = 0; i < institutesName.length; i++) {
					getDepartment(institutesName[i],i);
				}
				subjects = new String[institutesName.length][]; 
				for(int i = 0; i < institutesName.length; i++) {
					getSubjects(institutesName[i],i);
				}
			}
				
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				

			}});
	}

	private void getDepartment(String institute, final int i) {
		service.getdepartment(institute, new AsyncCallback<String[]>(){

			@Override
			public void onSuccess(String[] result) {
				department[i] = result;
				if(i==0) {
					getDepartmentBox(result);
				}
			}
				
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	private void getSubjects(String institute, final int i) {
		service.getSubjects(institute, new AsyncCallback<String[]>(){

			@Override
			public void onSuccess(String[] result) {
				subjects[i] = result;
				if(i==0) {
					flexTableForInstitutes.setWidget(0, 0, listBoxScores(result));
				}
			}
				
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
}
