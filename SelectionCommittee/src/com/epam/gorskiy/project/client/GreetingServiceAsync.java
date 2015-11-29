package com.epam.gorskiy.project.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void isSingIn(String login, String password, AsyncCallback<Integer> asyncCallback);

	void sendApplicationForAdmission(String login, int[] scores, String[] data, AsyncCallback<Boolean> callback);

	void registrateNewEntrant(String[] data, AsyncCallback<String> callback);

	void registrateNewAdmin(String[] data, AsyncCallback<String> callback);

	void getApplicationEdmission(AsyncCallback<List<String>> callback);

	void enrollTheStudents(List<String> data, AsyncCallback<Boolean> asyncCallback);

	void getDataEntrant(String login, AsyncCallback<String> asyncCallback);

	void deleteAdmin(String login, AsyncCallback<Boolean> asyncCallback);

	void deleteEntrant(String login, AsyncCallback<Boolean> asyncCallback);

	void getInstitutes(AsyncCallback<String[]> asyncCallback);

	void getdepartmentOfIAMM(AsyncCallback<String[]> asyncCallback);

	void getdepartmentOfIG(AsyncCallback<String[]> asyncCallback);

	void getdepartmentOfIEB(AsyncCallback<String[]> asyncCallback);

	void getdepartmentOfIFNT(AsyncCallback<String[]> asyncCallback);

	void getdepartmentOfIMES(AsyncCallback<String[]> asyncCallback);

	void getdepartmentOfIMOP(AsyncCallback<String[]> asyncCallback);

	void getdepartmentOfIPTS(AsyncCallback<String[]> asyncCallback);

	void getdepartment(String departament, AsyncCallback<String[]> callback);

	void getSubjects(String institute, AsyncCallback<String[]> asyncCallback);
}