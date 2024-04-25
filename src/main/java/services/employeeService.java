package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.employee;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class employeeService implements empService<employee> {
    private Connection connection;
    public employeeService() {
        this.connection = MyDB.getInstance().getConnection();
    }

    @Override
    public  void add(employee employee) throws SQLException {
        String sql = "INSERT INTO employee (idep,nom, email, salaire) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employee.getIdep());
            statement.setString(2, employee.getNom());
            statement.setString(3, employee.getEmail());
            statement.setInt(4, employee.getSalaire());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }













    @Override
    public void update(employee employee) throws SQLException {
        String sql = "update employee set nom = ?,idep = ? , email = ?, salaire = ? where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, employee.getNom());
            preparedStatement.setInt(2, employee.getIdep());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setDouble(4, employee.getSalaire());

            preparedStatement.setInt(5, employee.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(int id) throws SQLException {
        Statement ste = connection.createStatement();
        String query = "delete from employee where id = '" + id + "'";
        ste.executeUpdate(query);
    }

    @Override
    public List<employee> getAll() throws SQLException {
        return List.of();
    }


    public List<employee> getAllEmployees() throws SQLException {
        List<employee> employees = new ArrayList<>();

        String sql = "SELECT * FROM employee";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                employee employee = new employee();
                // Assuming your Employee class has appropriate setters
                employee.setId(resultSet.getInt("id"));
                employee.setNom(resultSet.getString("nom"));
                employee.setEmail(resultSet.getString("email"));
                employee.setSalaire(resultSet.getInt("salaire"));
                // You might also want to fetch the department information here
                // For example:
                // employee.setDepartment(fetchDepartment(resultSet.getInt("idep")));

                employees.add(employee);
            }
        } catch (SQLException e) {
            // Handle SQLException
            throw new SQLException("Error occurred while fetching employees.", e);
        }

        return employees;
    }

    public ObservableList<employee> getEmployeeList() throws SQLException {

        ObservableList<employee> employeeList = FXCollections.observableArrayList();

        List <employee> emp = new ArrayList<>();
        Statement stm = connection.createStatement();
        String query = "select * from employee";
        ResultSet rs;
        rs = stm.executeQuery(query);
        while (rs.next()) {
            employee r = new employee();
            r.setIdep(rs.getInt("idep"));
            r.setId(rs.getInt("id"));
            r.setSalaire((int) rs.getFloat("salaire"));
            r.setNom(rs.getString("nom"));
            r.setEmail(rs.getString("email"));

            employeeList.add(r);
        }
        return employeeList;

    }

    @Override
    public employee getById(int idemployee) throws SQLException {
        String sql = "SELECT `idep`, `nom`,`email`,`salaire` FROM `employee` WHERE `id` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idemployee);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int idep = resultSet.getInt("idep");

            String nom = resultSet.getString("nom");
            String email = resultSet.getString("email");
            int salaire = resultSet.getInt("salaire");


            return new employee(idemployee, idep,nom,email,salaire);
        } else {
            // Handle the case when no matching record is found
            return null;
        }

    }

}
