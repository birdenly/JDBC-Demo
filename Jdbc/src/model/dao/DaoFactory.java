package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;
//Class for instancing the JDBC classes with the DB connection. only for this.
public class DaoFactory {
    
    public static SellerDao creatSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao creatDepartmentDao(){
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
