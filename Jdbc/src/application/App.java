package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
public class App {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.creatSellerDao();
        
        System.out.println("##### FIND BY ID #####");
        Seller seller = sellerDao.findById(3);
        
        System.out.println(seller);

        System.out.println("\n##### FIND BY DEPARTMENT #####");

        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);

        for(Seller obj : list){
            System.out.println(obj);
        }

        System.out.println("\n##### FIND ALL #####");

        list = sellerDao.findAll();
        for(Seller obj : list){
            System.out.println(obj);
        }

        
        System.out.println("\n##### INSERT #####");

        Seller newSeller = new Seller(null, "greg", "greg@hotmail.com", new Date(), 4000.0, department);

        sellerDao.insert(newSeller);
        System.out.println("Inserted. new id = " + newSeller.getId());

        System.out.println("\n##### UPDATE #####");

        seller = sellerDao.findById(1);

        seller.setName("martha");
        sellerDao.update(seller);

        System.out.println("\nUpdate done");

        System.out.println("\n##### DELETE #####");

        sellerDao.deleteById(13);

        System.out.println("\nDelete done");

    }
}
