package com.epam.gorskiy.project.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	Integer isSingIn(String login, String password);

	String registrateNewEntrant(String[] name) throws IllegalArgumentException;

	String registrateNewAdmin(String[] name);

	String getDataEntrant(String login);

	boolean sendApplicationForAdmission(String login, int[] scores, String[] data);

	List<String> getApplicationEdmission();

	boolean enrollTheStudents(List<String> data);

	boolean deleteAdmin(String login);

	boolean deleteEntrant(String login);

	String[] getInstitutes();

	String[] getdepartmentOfIAMM();

	String[] getdepartmentOfIG();

	String[] getdepartmentOfIEB();

	String[] getdepartmentOfIFNT();

	String[] getdepartmentOfIMES();

	String[] getdepartmentOfIMOP();

	String[] getdepartmentOfIPTS();

	String[] getdepartment(String departament);

	String[] getSubjects(String institute);
}
