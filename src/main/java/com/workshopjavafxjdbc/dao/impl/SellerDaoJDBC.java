package com.workshopjavafxjdbc.dao.impl;

import com.workshopjavafxjdbc.dao.SellerDao;
import com.workshopjavafxjdbc.exception.DbException;
import com.workshopjavafxjdbc.model.Department;
import com.workshopjavafxjdbc.model.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?, ?, ?, ?, ?)")) {
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, Date.valueOf(seller.getBirthDate()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Seller seller) {
        try (PreparedStatement st = conn.prepareStatement("UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? WHERE Id = ?")) {
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, Date.valueOf(seller.getBirthDate()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Seller findById(Integer id) {
        try (PreparedStatement st = conn.prepareStatement("SELECT seller.*, department.Name as DepName FROM seller INNER JOIN  department ON seller.DepartmentId = department.id WHERE seller.Id = ?")) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Department dep = instantiateDepartment(rs);
                    return instantiateSeller(rs, dep);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Seller> findAll() {
        try (PreparedStatement st =
                     conn.prepareStatement("SELECT seller.*, department.Name as DepName FROM seller INNER JOIN  department ON seller.DepartmentId = department.id");
             ResultSet rs = st.executeQuery()) {
            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> departments = new HashMap<>();
            while (rs.next()) {
                Department dep = departments.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    departments.put(rs.getInt("DepartmentId"), dep);
                }
                Seller seller = instantiateSeller(rs, dep);
                sellers.add(seller);
            }
            return sellers;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        try (PreparedStatement st = conn.prepareStatement("SELECT seller.*, department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id WHERE DepartmentId = ? ORDER BY Name")) {
            st.setInt(1, department.getId());
            try (ResultSet rs = st.executeQuery()) {
                List<Seller> sellers = new ArrayList<>();
                Map<Integer, Department> departments = new HashMap<>();
                while (rs.next()) {
                    Department dep = departments.get(rs.getInt("DepartmentId"));
                    if (dep == null) {
                        dep = instantiateDepartment(rs);
                        departments.put(rs.getInt("DepartmentId"), dep);
                    }
                    Seller seller = instantiateSeller(rs, dep);
                    sellers.add(seller);
                }
                return sellers;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setBirthDate(rs.getDate("BirthDate").toLocalDate());
        seller.setDepartment(dep);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }
}
