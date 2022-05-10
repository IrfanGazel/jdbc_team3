package jdbc_review;

import java.sql.*;
import java.util.Scanner;

public class Main {

   public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
      try{
         int choice = 0;
         //calling the student class
         student s = new student();
         //using the do while loop for repeating the menu
         do{
            System.out.println("Press 1 for Add new Student");
            System.out.println("Press 2 for getting the specific student");
            System.out.println("Press 3 for updating the student email");
            System.out.println("Press 4 for deleting the student record");
            System.out.println("Press 5 for exit the menu");

            //taking input for Menu
            Scanner choose = new Scanner(System.in);
            choice = choose.nextInt();
            //choosing the menu
            switch (choice){
               case 1:
                  s.getStudentDetails();
                  s.insertStudent();
                  break;
               case 2:
                  //getting the data
                  s.searchStduent();
                  break;
               case 3:
                  //updating the data
                  s.updateStudent();
               case 4:
                  //deleting the data
                  s.deleteStudent();
                  break;
               case 5:
                  break;
               default:
                  System.out.println("Please enter the correct Menu choice");

            }
         }while(choice!=5);

      }catch(Exception e){
         e.printStackTrace();
      }
   }
}

//make a student class that will get all the functionalities of student
class student{
   private String name;
   private String email;
   private String gender;
   private int phone;

   //function that ask student to enter his details
   public void getStudentDetails(){
      Scanner input = new Scanner(System.in);
      System.out.println("Enter Your Name: ");
      name = input.nextLine();
      System.out.println("Enter Your Email: ");
      email = input.nextLine();
      System.out.println("Enter Your Gender: ");
      gender = input.nextLine();
      System.out.println("Enter Your Phone: ");
      phone = input.nextInt();
   }

   //function that save the student record into mysql database
   public void insertStudent() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
      //we need to call my jdbc connection function/constructor
      dbmsconnection dbmsconnection = new dbmsconnection("jdbc:mysql://localhost:3306/jdbc_team3?SSL=false","root","123456");
      //executing the connection
      Connection con = dbmsconnection.getConnection();
      //writing the mysql query for inserting the data into student table
      String sql = "insert into student(name,email,gender,phone) values(?,?,?,?);";
      //use the PreparedStatemnt for taking the input into each column
      PreparedStatement stmt = con.prepareStatement(sql);
      stmt.setString(1,name);
      stmt.setString(2,email);
      stmt.setString(3,gender);
      stmt.setInt(4,phone);

      //execute your preparedStatement
      int i = stmt.executeUpdate();
      System.out.println("Student record has been saved successfully!");

   }

   //making a function that search/fetch the student data via his name
   public void searchStduent() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
      //we need to call my jdbc connection function/constructor
      dbmsconnection dbmsconnection = new dbmsconnection("jdbc:mysql://localhost:3306/jdbc_team3?SSL=false","root","123456");
      //executing the connection
      Connection con = dbmsconnection.getConnection();

      System.out.println("Enter Student Email:");
      Scanner input = new Scanner(System.in);
      //taking the email input from the student
      String inputEmail = input.nextLine();

      //write my mysql query for getting the student through his/her email
      String sql = "select * from student where email=?";
      //prepared Statement
      PreparedStatement stmt = con.prepareStatement(sql);
      stmt.setString(1,inputEmail);

      //ResultSet that finally holds our result
      ResultSet rs = stmt.executeQuery();
      if(rs.next() == false){
         System.out.println("No email found in the database record");
      }else{
         System.out.println(rs.getInt(1));
         System.out.println(rs.getString(2));
         System.out.println(rs.getString(3));
         System.out.println(rs.getString(4));
         System.out.println(rs.getInt(5));
      }
   }

   //function for deleting the student record specifically
   public void deleteStudent() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
      //we need to call my jdbc connection function/constructor
      dbmsconnection dbmsconnection = new dbmsconnection("jdbc:mysql://localhost:3306/jdbc_team3?SSL=false","root","123456");
      //executing the connection
      Connection con = dbmsconnection.getConnection();

      System.out.println("Enter the student email to delete his/her record");
      Scanner emailin = new Scanner(System.in);
      String inputEmail = emailin.nextLine();

      //writing the sql query for deleting the student record
      String sql = "delete from student where email=?";

      //using the preparedStatement
      PreparedStatement stmt = con.prepareStatement(sql);
      stmt.setString(1,inputEmail);

      int i = stmt.executeUpdate();
      if(i>0){
         System.out.println("Student Record Has Been deleted successfully!");
      }else{
         System.out.println("There is no any record of this Email in the database");
      }
   }

   //function for updating the student name with the help of his/her email
   public void updateStudent(){

   }

}
//make a class that contains the JDB connection
class dbmsconnection{
   String url;
   String username;
   String password;

   //make a constructor that defines my above 3 variables
   public dbmsconnection(String url,String username,String password){
      this.url = url;
      this.username = username;
      this.password = password;
   }

   //need to make a function that calls the JDBC driver
   public Connection getConnection() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
      Connection con = null;
      //calling my jdbc driver
      Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
      con = DriverManager.getConnection(url,username,password);
      System.out.println("Database connection has been successfully established!");
      return con;
   }

}