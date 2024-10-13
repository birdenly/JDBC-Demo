package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmenteApp {
    public static void main(String[] args) {
        DepartmentDao DepartmentDao = DaoFactory.creatDepartmentDao();
        
        System.out.println("##### FIND BY ID #####");
        Department Department = DepartmentDao.findById(3);
        
        System.out.println(Department);

        System.out.println("\n##### FIND ALL #####");

        List<Department> list = DepartmentDao.findAll();
        for(Department obj : list){
            System.out.println(obj);
        }
        
        System.out.println("\n##### INSERT #####");

        Department newDepartment = new Department(null, "jogos2");

        DepartmentDao.insert(newDepartment);

        System.out.println("Inserted. new id = " + newDepartment.getId());

        System.out.println("\n##### UPDATE #####");

        Department = DepartmentDao.findById(7);

        Department.setName("filmes");
        DepartmentDao.update(Department);

        System.out.println("\nUpdate done");

        System.out.println("\n##### DELETE #####");

        DepartmentDao.deleteById(12);

        System.out.println("\nDelete done");

    }
}
