package com.workshopjavafxjdbc.service;

import com.workshopjavafxjdbc.dao.DaoFactory;
import com.workshopjavafxjdbc.dao.SellerDao;
import com.workshopjavafxjdbc.model.Seller;

import java.util.List;

public class SellerService {

    private SellerDao sellerDao = DaoFactory.createSellerDao();

    public List<Seller> findAll() {
        return sellerDao.findAll();
    }

    public void saveOrUpdate(Seller seller) {
        if (seller.getId() == null) {
            sellerDao.insert(seller);
        } else {
            sellerDao.update(seller);
        }
    }

    public void remove(Seller seller) {
        sellerDao.deleteById(seller.getId());
    }
}
