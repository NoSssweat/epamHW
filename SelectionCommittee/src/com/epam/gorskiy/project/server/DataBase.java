package com.epam.gorskiy.project.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.sql.PreparedStatement;

public class DataBase {

	private static final int NUM_OF_SUBJECT = 3;

	public static enum User {
		NotUser, Entrant, Admin
	}

	private static Logger logger = Logger.getLogger("DataBase");
	private Connection connection = null;
	private Statement statement = null;
	private String dataBaseName = "";
	private final String CLASSNAME = "com.mysql.jdbc.Driver";
	private final String URL = "jdbc:mysql://localhost/?characterEncoding=UTF-8";
	private final String USER = "user";
	private final String PASSWORD = "root";

	public DataBase() {
		getBDConnaction();
		if (createBDUnivercity("University")) {
			useDataBase();
			createTables();
			addSubjects(getSubjects());
			addInstitutes(getInstitutesCreateBase(), getSubjectForFaculty());
			addDepartment(getdepartmentOfGF(), 1);
			addDepartment(getdepartmentOfFIBS(), 2);
			addDepartment(getdepartmentOfFEL(), 3);
			addDepartment(getdepartmentOfFEM(), 4);
			addDepartment(getdepartmentOfFKTI(), 5);
			addDepartment(getdepartmentOfFRT(), 6);
			addDepartment(getdepartmentOfFEA(), 7);
			addNewAdmin(getFirstAdmin());
			addScores(getInstitutesCreateBase(), getPassingScore());
		} else {
			useDataBase();
		}
	}

	public boolean createBDUnivercity(String dBName) {

		dataBaseName = dBName;
		ResultSet resultSet;
		String sql = "SHOW DATABASES LIKE " + "'" + dataBaseName + "'";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				sql = "CREATE DATABASE " + dataBaseName + "  CHARACTER SET 'utf8'";
				statement.executeUpdate(sql);
				return true;
			}
		} catch (SQLException e) {
			logger.info("Error: dataDase " + dataBaseName + " not create! " + e.getMessage());
			return false;
		}
		return false;
	}

	public boolean createTables() {

		ResultSet resultSet;
		String sql = "SHOW TABLES LIKE 'Entrants'";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				sql = "CREATE TABLE Entrants(Surname varchar(40),Name varchar(40),Patronymic varchar(40),Birthday varchar(40),Login varchar(40) PRIMARY KEY,Password varchar(40),"
						+ "Institutes INTEGER, Department INTEGER, Enroll varchar(40))";
				statement.executeUpdate(sql);
			}
			sql = "SHOW TABLES LIKE 'Institutes'";
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				sql = "CREATE TABLE Institutes(id INTEGER AUTO_INCREMENT PRIMARY KEY,Name varchar(100), IDSubject1 INTEGER, IDSubject2 INTEGER, IDSubject3 INTEGER)";
				statement.executeUpdate(sql);
			}
			sql = "SHOW TABLES LIKE 'Department'";
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				sql = "CREATE TABLE Department(id INTEGER AUTO_INCREMENT PRIMARY KEY,Name varchar(100), IdInstitutes INTEGER)";
				statement.executeUpdate(sql);
			}
			sql = "SHOW TABLES LIKE 'Subjects'";
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				sql = "CREATE TABLE Subjects(id INTEGER AUTO_INCREMENT PRIMARY KEY,Name varchar(20))";
				statement.executeUpdate(sql);
			}
			sql = "SHOW TABLES LIKE 'SubjectsResult'";
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				sql = "CREATE TABLE SubjectsResult(Login varchar(40) PRIMARY KEY, Subject1 INTEGER, Subject2 INTEGER, Subject3 INTEGER)";
				statement.executeUpdate(sql);
			}
			sql = "SHOW TABLES LIKE 'Admin'";
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				sql = "CREATE TABLE Admin(Surname varchar(40),Name varchar(40),Patronymic varchar(40),Birthday varchar(40),Login varchar(40) PRIMARY KEY,Password varchar(40))";
				statement.executeUpdate(sql);
			}
			sql = "SHOW TABLES LIKE 'PassingScores'";
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				sql = "CREATE TABLE PassingScores(Scores INTEGER, IdInstitutes INTEGER)";
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			logger.info("Error: tables not create! " + e.getMessage());
			return false;
		}
		return true;
	}

	public void useDataBase() {
		query("USE " + dataBaseName);
	}

	public void addInstitutes(String[] data, String[] subjects) {
		for (int i = 0; i < data.length; i++) {
			String sql = "SELECT id FROM Subjects WHERE Name = ? OR Name = ? OR Name = ?";
			int[] idSubjects = new int[NUM_OF_SUBJECT];
			int k = 0;
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				for (int j = i * NUM_OF_SUBJECT; j < NUM_OF_SUBJECT * (i + 1); j++) {
					preparedStatement.setString(++k, subjects[j]);
				}
				k = 0;
				ResultSet res = preparedStatement.executeQuery();
				while (res.next()) {
					idSubjects[k++] = res.getInt("id");
				}
			} catch (SQLException e) {
				logger.info("Do not select " + subjects + " " + e.getMessage());
			}
			sql = "INSERT INTO Institutes(Name,IDSubject1,IDSubject2,IDSubject3) VALUES (?,?,?,?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, data[i]);
				k = 2;
				for (int idSubject : idSubjects) {
					preparedStatement.setInt(k++, idSubject);
				}
				preparedStatement.execute();
			} catch (SQLException e) {
				logger.info("Do not add data in departament " + data[i] + " " + e.getMessage());
			}
		}
	}

	public String[] getInstitutes() {
		String[] data = null;
		String sql = "SELECT Name FROM Institutes";
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(sql);
			resultSet.last();
			int size = resultSet.getRow();
			resultSet.beforeFirst();
			int i = 0;
			data = new String[size];
			while (resultSet.next()) {
				data[i++] = resultSet.getString("Name");
			}
		} catch (SQLException e) {
			logger.info("Do not select institutes " + e.getMessage());
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.info("Do not close " + e.getMessage());
			}
		}
		return data;
	}

	public String[] getDepartment(String institute) {
		String[] data = null;
		String sql = "SELECT Name FROM Department WHERE IdInstitutes = (SELECT id FROM Institutes WHERE Name = ?)";
		ResultSet resultSet = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, institute);
			resultSet = preparedStatement.executeQuery();
			resultSet.last();
			int size = resultSet.getRow();
			resultSet.beforeFirst();
			int i = 0;
			data = new String[size];
			while (resultSet.next()) {
				data[i++] = resultSet.getString("Name");
			}
		} catch (SQLException e) {
			logger.info("Do not select department from " + institute + " " + e.getMessage());
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.info("Do not close " + e.getMessage());
			}
		}
		return data;
	}

	public String[] getSubjects(String institute) {
		String[] data = null;
		String sql = "SELECT IDSubject1,IDSubject2,IDSubject3 FROM Institutes WHERE Name = ?";
		ResultSet resultSet = null;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, institute);
			resultSet = preparedStatement.executeQuery();
			sql = "SELECT Name FROM Subjects WHERE id = ? OR id = ? OR id = ?";
			preparedStatement = connection.prepareStatement(sql);
			resultSet.next();
			for (int i = 1; i <= NUM_OF_SUBJECT; i++) {
				preparedStatement.setInt(i, resultSet.getInt(i));
			}
			resultSet.close();
			resultSet = preparedStatement.executeQuery();
			data = new String[NUM_OF_SUBJECT];
			int i = 0;
			while (resultSet.next()) {
				data[i++] = resultSet.getString("Name");
			}
		} catch (SQLException e) {
			logger.info("Do not select department from " + institute + " " + e.getMessage());
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.info("Do not close " + e.getMessage());
			}
		}
		return data;
	}

	public void addDepartment(String[] data, int idInstitutes) {

		String sql = "INSERT INTO Department(Name,IdInstitutes) VALUES (?,?)";
		for (int i = 0; i < data.length; i++) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, data[i]);
				preparedStatement.setInt(2, idInstitutes);
				preparedStatement.execute();
			} catch (SQLException e) {
				logger.info("Do not add data in departament " + data[i] + " " + e.getMessage());
			}
		}

	}

	public void addSubjects(String[] data) {

		String sql = "INSERT INTO Subjects(Name) VALUES (?)";
		for (int i = 0; i < data.length; i++) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, data[i]);
				preparedStatement.execute();
			} catch (SQLException e) {
				logger.info("Do not add subject " + data[i] + " " + e.getMessage());
			}
		}

	}

	public void addScores(String[] institutes, int[] scres) {

		String sqlSelect = "SELECT id FROM Institutes WHERE Name = ?";
		String sqlInsert = "INSERT INTO PassingScores(Scores, IdInstitutes) VALUES (?,?)";
		ResultSet resultSet = null;
		for (int i = 0; i < institutes.length; i++) {
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
				preparedStatement.setString(1, institutes[i]);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				preparedStatement = connection.prepareStatement(sqlInsert);
				preparedStatement.setInt(1, scres[i]);
				preparedStatement.setInt(2, resultSet.getInt("id"));
				preparedStatement.execute();
			} catch (SQLException e) {
				logger.info("Do not add score " + institutes[i] + " " + e.getMessage());
			} finally {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.info("Do not close " + e.getMessage());
				}
			}
		}

	}

	public String addNewEntrant(String[] data) {

		ResultSet resultSet = null;
		String sql = "SELECT 1 FROM Admin WHERE Login = ?";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, data[4]);
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				sql = "INSERT INTO Entrants(Surname,Name,Patronymic,Birthday,Login,Password) VALUES (?, ?, ?, ?, ?, ?)";
				preparedStatement = connection.prepareStatement(sql);
				for (int i = 0; i < data.length; i++) {
					preparedStatement.setString(i + 1, data[i]);
				}
				preparedStatement.execute();
				return "Регистрация выполнена успешно";
			}
		} catch (SQLException e) {
			logger.info("Do not add " + data + " " + e.getMessage());
		} finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				logger.info("Do not close " + e.getMessage());
			}
		}
		return "Регистрация не выполнена, такой логин уже существует";
	}

	public boolean deleteEntrant(String login) {
		String sql = "DELETE FROM Entrants WHERE Login = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, login);
			preparedStatement.execute();
		} catch (SQLException e) {
			logger.info("Do not delete " + login + " " + e.getMessage());
			return false;
		}
		sql = "DELETE FROM SubjectsResult WHERE Login = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, login);
			preparedStatement.execute();
		} catch (SQLException e) {
			logger.info("Do not delete " + login + " " + e.getMessage());
			return false;
		}
		return true;
	}

	public String addNewAdmin(String[] data) {

		ResultSet resultSet = null;
		String sql = "SELECT 1 FROM Entrants WHERE Login = ?";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, data[4]);
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				preparedStatement.close();
				sql = "INSERT INTO Admin(Surname,Name,Patronymic,Birthday,Login,Password) VALUES (?, ?, ?, ?, ?, ?)";
				preparedStatement = connection.prepareStatement(sql);
				for (int i = 0; i < data.length; i++) {
					preparedStatement.setString(i + 1, data[i]);
				}
				preparedStatement.execute();
				return "Регистрация выполнена успешно";
			}
		} catch (SQLException e) {
			logger.info("Do not add " + data + " " + e.getMessage());
		} finally {
			try {
				preparedStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				logger.info("Do not close " + e.getMessage());
			}
		}
		return "Регистрация не выполнена, такой логин уже существует";
	}

	public boolean deleteAdmin(String login) {
		String sql = "DELETE FROM Admin WHERE Login = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, login);
			preparedStatement.execute();
		} catch (SQLException e) {
			logger.info("Not delete " + login + " " + e.getMessage());
			return false;
		}
		return true;
	}

	public ResultSet query(String sql) {

		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			logger.info("Error: sql -- " + sql);
		}
		return resultSet;
	}

	private boolean SingIn(String table, String login, String password) {

		boolean isUser = false;
		ResultSet resultSet = null;
		String sql = "SELECT 1 FROM " + table + " WHERE Login = ? and Password = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				isUser = true;
			}
		} catch (SQLException e) {
			logger.info("Not sing in " + e.getMessage());
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.info("Do not close " + e.getMessage());
			}
		}

		return isUser;
	}

	public Integer isSingIn(String login, String password) {
		if (SingIn("Entrants", login, password))
			return 2;
		if (SingIn("Admin", login, password))
			return 1;
		return 0;
	}

	private Connection getBDConnaction() {

		try {
			Class.forName(CLASSNAME);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			logger.info("Do not get connection " + e.getMessage());
		}
		return connection;
	}

	public Connection getConnection() {
		return connection;
	}

	public boolean addNewEntrantApplicationForAdmission(String login, int[] scores, String[] data) {
		String sql = "INSERT INTO SubjectsResult(Login, Subject1, Subject2, Subject3) VALUES (?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, login);
			for (int i = 0; i < scores.length; i++) {
				preparedStatement.setInt(i + 2, scores[i]);
			}
			preparedStatement.execute();
		} catch (SQLException e) {
			logger.info("Do Not write " + data + " " + e.getMessage());
			return false;
		}
		String sqlUp = "UPDATE Entrants SET Institutes = ?, Department = ?, Enroll = 'В очереди' WHERE Login = ?";
		String sqlSelInst = "SELECT id FROM Institutes WHERE Name = ?";
		String sqlSelDep = "SELECT id FROM Department WHERE Name = ?";
		int idInst = -1;
		int idDep = -1;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelInst)) {
			preparedStatement.setString(1, data[0]);
			ResultSet res = preparedStatement.executeQuery();
			res.next();
			idInst = res.getInt("id");
		} catch (SQLException e) {
			logger.info("Do Not write " + data + " " + e.getMessage());
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelDep)) {
			preparedStatement.setString(1, data[1]);
			ResultSet res = preparedStatement.executeQuery();
			res.next();
			idDep = res.getInt("id");
		} catch (SQLException e) {
			logger.info("Do Not write " + data + " " + e.getMessage());
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUp)) {
			preparedStatement.setInt(1, idInst);
			preparedStatement.setInt(2, idDep);
			preparedStatement.setString(3, login);
			preparedStatement.execute();
		} catch (SQLException e) {
			logger.info("Do Not update " + data + " " + e.getMessage());
		}
		return true;
	}

	public List<String> getApplicationEdmission() {
		String sql = "SELECT * FROM Entrants";
		List<String> data = new LinkedList<String>();
		ResultSet resultSet = null;
		ResultSet res = null;
		try {
			resultSet = query(sql);
			PreparedStatement preparedStatement = null;
			while (resultSet.next()) {
				String institute = "";
				String department = "";

				if (resultSet.getInt("Institutes") == 0 || resultSet.getInt("Department") == 0)
					continue;

				sql = "SELECT Name,IDSubject1,IDSubject2,IDSubject3 FROM Institutes WHERE id = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, resultSet.getInt("Institutes"));
				res = preparedStatement.executeQuery();
				res.next();
				institute = res.getString("Name");
				sql = "SELECT Name FROM Subjects WHERE id = ? OR id = ? OR id = ?";
				preparedStatement = connection.prepareStatement(sql);
				for (int i = 1; i <= NUM_OF_SUBJECT; i++) {
					preparedStatement.setInt(i, res.getInt("IDSubject" + i));
				}
				res.close();
				res = preparedStatement.executeQuery();
				String[] subj1Name = new String[NUM_OF_SUBJECT];
				int i = 0;
				while (res.next()) {
					subj1Name[i++] = res.getString("Name");
				}

				res.close();
				sql = "SELECT Name FROM Department WHERE id = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, resultSet.getInt("Department"));
				res = preparedStatement.executeQuery();
				res.next();
				department = res.getString("Name");

				res.close();
				sql = "SELECT * FROM SubjectsResult WHERE Login = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, resultSet.getString("Login"));
				res = preparedStatement.executeQuery();
				res.next();
				i = 1;
				int[] subj = new int[NUM_OF_SUBJECT];
				while (i <= NUM_OF_SUBJECT) {
					subj[i - 1] = res.getInt("Subject" + i);
					i++;
				}

				data.add(resultSet.getString("Login") + ";" + resultSet.getString("Surname") + ";"
						+ resultSet.getString("Name") + ";" + resultSet.getString("Patronymic") + ";"
						+ resultSet.getString("Birthday") + ";" + institute + ";" + department + ";" + subj1Name[0]
						+ "(" + subj[0] + "), " + subj1Name[1] + "(" + subj[1] + "), " + subj1Name[2] + "(" + subj[2]
						+ ")");
			}
		} catch (SQLException e) {
			logger.info("Data don't read " + e.getMessage());
		} finally {
			try {
				resultSet.close();
				res.close();
			} catch (SQLException e) {
				logger.info("Do not close " + e.getMessage());
			}
		}

		return data;
	}

	public boolean enrollTheStudent(List<String> data) {
		for (String elem : data) {
			String sql = "SELECT * FROM SubjectsResult WHERE Login = ?";
			ResultSet resultSet = null;
			int sum = 0;
			int passingScore = 1;
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, elem);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				for (int i = 1; i <= NUM_OF_SUBJECT; i++) {
					sum += resultSet.getInt("Subject" + i);
				}
			} catch (SQLException e) {
				logger.info("Not enter " + data + " " + e.getMessage());
				return false;
			} finally {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.info("Do not close " + e.getMessage());
				}
			}
			sql = "SELECT Scores FROM PassingScores WHERE idInstitutes = (SELECT Institutes FROM Entrants WHERE Login = ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, elem);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				passingScore = resultSet.getInt("Scores");
			} catch (SQLException e) {
				logger.info("Not enter " + data + " " + e.getMessage());
				return false;
			} finally {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.info("Do not close " + e.getMessage());
				}
			}

			if (sum >= passingScore) {
				sql = "UPDATE Entrants SET Enroll = 'Принят' WHERE Login = ?";
			} else {
				sql = "UPDATE Entrants SET Enroll = 'Не принят' WHERE Login = ?";
			}
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, elem);
				preparedStatement.execute();
			} catch (SQLException e) {
				logger.info("Not enter " + data + " " + e.getMessage());
				return false;
			}

		}
		return true;
	}

	public String getDataEntrant(String login) {
		String sql = "SELECT * FROM Entrants WHERE Login = ?";
		ResultSet resultSet = null;
		String data = "";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, login);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			data = resultSet.getString("Surname") + ";" + resultSet.getString("Name") + ";"
					+ resultSet.getString("Patronymic") + ";" + resultSet.getString("Birthday") + ";"
					+ resultSet.getString("Enroll");
		} catch (SQLException e) {
			logger.info("Not read " + login + " " + e.getMessage());
			return null;
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.info("Do not close " + e.getMessage());
			}
		}
		return data;
	}

	private String[] getFirstAdmin() {
		String[] data = { "Admin", "Admin", "Admin", "2015-11-18", "Admin", "Admin" };
		return data;
	}

	private String[] getSubjects() {
		String[] data = { "Русский", "Математика", "Физика", "История", "Обществознание", "Английский" };
		return data;
	}

	private String[] getSubjectForFaculty() {
		String[] data = { "Русский", "История", "Обществознание", "Русский", "Математика", "Физика", "Русский",
				"Математика", "Физика", "Русский", "Математика", "Информатика","Русский", "Математика", "Физика",
				"Русский", "Английский", "Обществознание", "Русский", "Математика", "Физика"};
		return data;
	}

	public String[] getInstitutesCreateBase() {
		String[] data = { "Гумманитарный Факультет", "Радиотехники и коммуникаций", "Факультет электроники",
				"Факультет компьютерныч технологий и информатики",
				"Факультет электротехники и автоматики", "Факультет экономики и менеджмента", 
				"Факультет информационно-измерительных и биотехнических систем" };
		return data;
	}

	public String[] getdepartmentOfFRT() {
		String[] data = { "Радиотехника", "Информационные технологии и системы связи", "Конструирование и технология электронных средств",
				"Радиоэлектронные системы и комплексы" };
		return data;
	}

	public String[] getdepartmentOfGF() {
		String[] data = { "Отечественная история", "Онтология и теория познания",
				"Философия науки и техники", "Социальная философия",
				"Германские языки", "Романо-германские языки", "Русский язык",
				"Социальная структура, социальные институты и процессы", "Теория и философия политики, истории и методологии политической науки" };
		return data;
	}

	public String[] getdepartmentOfFIBS() {
		String[] data = { "Приборы и методы контроля качества и диагностики", "Акустические приборы и системы ",
				"Информационно-измерительная техника и технологии", "Лазерные измерительные и навигационные системы",
				"Приборы и технологии контроля окружающей среды", "Биотехнические и медицинские аппараты и системы" };
		return data;
	}

	public String[] getdepartmentOfFEL() {
		String[] data = { "Электроника и наноэлектроника", "Нанотехнологии и микросистемная техника"};
		return data;
	}

	public String[] getdepartmentOfFEM() {
		String[] data = { "Управление технологическими инновациями ", "Финансовый и антикризисный менеджмент"};
		return data;
	}

	public String[] getdepartmentOfFKTI() {
		String[] data = { "Прикладная математика и информатика", "Информатика и вычислительная техника",
				"Информационные системы и технологии", "Программная инженерия", "Управление в технических системах", "Системный анализ и управление"};
		return data;
	}

	public String[] getdepartmentOfFEA() {
		String[] data = { "Управление в технических системах", "Электроэнергетика и электротехника"};
		return data;
	}

	public int[] getPassingScore() {
		int[] data = { 250, 210, 260, 198, 125, 201, 254 };
		return data;
	}
}