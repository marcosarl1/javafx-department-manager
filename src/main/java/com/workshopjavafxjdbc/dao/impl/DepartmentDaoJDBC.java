package com.workshopjavafxjdbc.dao.impl;

import com.workshopjavafxjdbc.dao.DepartmentDao;
import com.workshopjavafxjdbc.exception.DbException;
import com.workshopjavafxjdbc.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        try(PreparedStatement st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)")) {
            st.setString(1, department.getName());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Department department) {
        try(PreparedStatement st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?")) {
            st.setString(1, department.getName());
            st.setInt(2, department.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        try(PreparedStatement st = conn.prepareStatement("DELETE FROM department WHERE Id = ?")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        try(PreparedStatement st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?")) {
            st.setInt(1, id);
            try(ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Department department = new Department();
                    department.setId(rs.getInt("Id"));
                    department.setName(rs.getString("Name"));
                    return department;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        try(PreparedStatement st = conn.prepareStatement("SELECT * FROM department ORDER BY Name"); ResultSet rs = st.executeQuery()) {
            List<Department> departments = new ArrayList<>();
            while (rs.next()) {
                Department department = new Department();
                department.setId(rs.getInt("Id"));
                department.setName(rs.getString("Name"));
                departments.add(department);
            }
            return departments;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
