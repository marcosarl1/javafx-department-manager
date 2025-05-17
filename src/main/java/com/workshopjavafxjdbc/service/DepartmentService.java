package com.workshopjavafxjdbc.service;

import com.workshopjavafxjdbc.dao.DaoFactory;
import com.workshopjavafxjdbc.dao.DepartmentDao;
import com.workshopjavafxjdbc.model.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    public void saveOrUpdate(Department department) {
        if (department.getId() == null) {
            departmentDao.insert(department);
        } else {
            departmentDao.update(department);
        }
    }

    public void remove(Department department) {
        departmentDao.deleteById(department.getId());
    }
}
