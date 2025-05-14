package com.workshopjavafxjdbc.dao;

import com.workshopjavafxjdbc.model.Department;
import com.workshopjavafxjdbc.model.Seller;

import java.util.List;

public interface SellerDao {

    void insert(Seller seller);
    void update(Seller seller);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);
}
