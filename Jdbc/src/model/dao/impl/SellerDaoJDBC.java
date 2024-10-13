package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

    //Connection to the DB, its done on the DaoFactory class
    private Connection conn;


    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

        //Prepared statement - prepares the SQL statement to be executed
        PreparedStatement st = null;

        try {
            //SQL creation, ALWAYS needs a space in the end of each line
            st = conn.prepareStatement(
                "INSERT INTO seller "
                +"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                +"VALUES "
                +"(?, ?, ?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS //Returns the generated key (Id)
            );
            //Sets used to replace the "?" in the SQL statement
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            //Executes the SQL statement
            //executeUpdate() - always used to insert, update or delete and returns the number of rows affected
            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                //Gets the generated key (Id) and sets it to the object
                //ResultSet - always used to get the return of the SQL statement
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    //1 = first column
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbException("No Rows affected");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;

        try {

            st = conn.prepareStatement(
                "UPDATE seller "
                +"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                +"WHERE Id = ?"
            );

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            st.executeUpdate();


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try {

            st = conn.prepareStatement(
               "DELETE FROM seller "
                +"WHERE Id = ?"
            );

            st.setInt(1, id);

            int rows = st.executeUpdate();

            if(rows == 0){
                throw new DbException("Id doesnt exist.");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement st = null;
        //ResultSet - always used to get the return of the SQL statement
        ResultSet rs = null;

        try{

            st = conn.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                + "FROM seller INNER JOIN department "
                + "ON seller.DepartmentId = department.Id "
                + "WHERE seller.Id = ?"
            );
    
            st.setInt(1,id);
            rs = st.executeQuery();

            //If the result set has a next value, it means that the Id exists
            if(rs.next()){
                Department dep = intantiateDepartment(rs);
                Seller obj = intantiateSeller(rs,dep);
                return obj;
            }

            return null;

        }catch(SQLException e){
            throw new DbException(e.getMessage());

        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        
    }

    private Seller intantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        //Strings will always be the name of the column in the DB
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setDepartment(dep);

        return obj;
    }

    private Department intantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString(("DepName")));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{

            st = conn.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                +"FROM seller INNER JOIN department "
                +"ON seller.DepartmentId = department.Id "
                +"ORDER BY Name"
            );

            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            //While used as it can have more than one result to form a list
            while(rs.next()){

                //Checks if the department already exists in the map
                //null = doesnt exist else it already exists and will be used normally rather than created in the IF
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null){
                    dep = intantiateDepartment(rs);  
                    map.put(rs.getInt("DepartmentId"), dep);
                    
                }
                Seller obj = intantiateSeller(rs,dep);
                list.add(obj);
            }

            return list;

        }catch(SQLException e){
            throw new DbException(e.getMessage());

        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        
        PreparedStatement st = null;
        ResultSet rs = null;

        try{

            st = conn.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                +"FROM seller INNER JOIN department "
                +"ON seller.DepartmentId = department.Id "
                +"WHERE DepartmentId = ? "
                +"ORDER BY Name"
            );
    
            st.setInt(1,department.getId());
            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while(rs.next()){

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null){
                    dep = intantiateDepartment(rs);  
                    map.put(rs.getInt("DepartmentId"), dep);
                    
                }
                Seller obj = intantiateSeller(rs,dep);
                list.add(obj);
            }

            return list;

        }catch(SQLException e){
            throw new DbException(e.getMessage());

        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

}
