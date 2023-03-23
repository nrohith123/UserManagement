package com.usermanagement.dao;

import java.sql.*;
import java.util.*;
import com.usermanagement.bean.User;
public class UserDao {
	
	private String jdbcURL = "jdbc:/mysql://localhost:3306/userdb?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "Rohith@555";
	private String jdbcDriver = "com.mysql.jdbc.Driver";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users"+" (name,email,country) VALUES"+"(?,?,?);";
	private static final String SELECT_USERBY_ID = "select id,name,email,country from users where id=?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_ALL_USERS = "delete from users where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name =?,email=?,country=?,where id =?;";
	
	public UserDao() {
		
		
	}
	
	
	protected Connection getConnection() {
		
		Connection connection = null;
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
	public void insertUser(User user) throws SQLException {
		System.out.println("INSERT_USERS_SQL");
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
				preparedStatement.setString(1,user.getName());
				preparedStatement.setString(2,user.getEmail());
				preparedStatement.setString(3, user.getCountry());
				System.out.println(preparedStatement);
				preparedStatement.executeUpdate();
				}catch(SQLException e) {
					printSQLException(e);
				}
				
	}

	
	
	public User selectUser(int id) {
		User user = null;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERBY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id,name,email,country);
			}
		}catch(Exception e) {
			printSQLException(e);
		}
		return user;
	}
	
	
	
	public List<User> selectAllUsers() {
		List<User> users = new ArrayList<>();
		try(Connection connection  = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
				System.out.println(preparedStatement);
				ResultSet rs= preparedStatement.executeQuery();
				
				while(rs.next()) {
					int id = rs.getInt("id");
					String name = rs.getString("name");
					String email = rs.getString("email");
					String country = rs.getString("country");
					users.add(new User(id,name,email,country));
				}
		}
		catch(Exception e) {
			printSQLException(e);
		}
		return users;
				
	}
	
	
	private void printSQLException(Exception e) {
		
		//for(Throwable er : e) {
			if(e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: "+((SQLException) e).getSQLState());
				System.err.println("Error Code: "+((SQLException) e).getErrorCode());
				System.err.println("Message: "+e.getMessage());
				Throwable t = e.getCause();
				while(t!=null) {
					System.out.println("Cause: "+t);
					t = t.getCause();
				}
			//}
		
	}
	}

	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try(Connection connection= getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			System.out.println("updated User:"+statement);
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());
			
			rowUpdated = statement.executeUpdate() >0;
		}
		return rowUpdated;
	}
	
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try(Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_ALL_USERS);) {
				statement.setInt(1, id);
				rowDeleted = statement.executeUpdate()>0;
	}
	return rowDeleted;
	}
	
	
	
	
	
}
