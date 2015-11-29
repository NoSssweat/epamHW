package com.epam.gorskiy.project.server;

import java.util.List;

import com.epam.gorskiy.project.client.GreetingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	private DataBase dataBase = new DataBase();

	@Override
	public String registrateNewEntrant(String[] input) throws IllegalArgumentException {
		return dataBase.addNewEntrant(input);
	}

	@Override
	public Integer isSingIn(String login, String password) {
		return dataBase.isSingIn(login, password);
	}

	@Override
	public boolean sendApplicationForAdmission(String login, int[] scores, String[] data) {
		return dataBase.addNewEntrantApplicationForAdmission(login, scores, data);
	}

	@Override
	public String registrateNewAdmin(String[] data) {
		return dataBase.addNewAdmin(data);
	}

	@Override
	public List<String> getApplicationEdmission() {
		return dataBase.getApplicationEdmission();
	}

	@Override
	public boolean enrollTheStudents(List<String> data) {
		if (data == null)
			return false;
		return dataBase.enrollTheStudent(data);
	}

	@Override
	public String getDataEntrant(String login) {
		return dataBase.getDataEntrant(login);
	}

	@Override
	public boolean deleteAdmin(String login) {
		return dataBase.deleteAdmin(login);
	}

	@Override
	public boolean deleteEntrant(String login) {
		return dataBase.deleteEntrant(login);
	}

	@Override
	public String[] getInstitutes() {
		return dataBase.getInstitutes();
	}

	@Override
	public String[] getdepartment(String departament) {
		return dataBase.getDepartment(departament);
	}

	@Override
	public String[] getdepartmentOfIAMM() {
		return dataBase.getdepartmentOfFRT();
	}

	@Override
	public String[] getdepartmentOfIG() {
		return dataBase.getdepartmentOfGF();
	}

	@Override
	public String[] getdepartmentOfIEB() {
		return dataBase.getdepartmentOfFIBS();
	}

	@Override
	public String[] getdepartmentOfIFNT() {
		return dataBase.getdepartmentOfFEL();
	}

	@Override
	public String[] getdepartmentOfIMES() {
		return dataBase.getdepartmentOfFEM();
	}

	@Override
	public String[] getdepartmentOfIMOP() {
		return dataBase.getdepartmentOfFKTI();
	}

	@Override
	public String[] getdepartmentOfIPTS() {
		return dataBase.getdepartmentOfFEA();
	}

	@Override
	public String[] getSubjects(String institute) {
		return dataBase.getSubjects(institute);
	}

}
