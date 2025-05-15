package com.workshopjavafxjdbc.dao;

import com.workshopjavafxjdbc.dao.impl.DepartmentDaoJDBC;
import com.workshopjavafxjdbc.dao.impl.SellerDaoJDBC;
import com.workshopjavafxjdbc.db.DB;

public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
