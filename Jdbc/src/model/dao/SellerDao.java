package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;
//Class for instancing the JDBC classes with the DB connection. only for this.
public interface SellerDao {

    void insert(Seller obj);
    void update(Seller obj);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);

}
