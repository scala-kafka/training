package com.info.day9

import slick.jdbc.MySQLProfile.api._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class EmployeeDAO {
  var driverName: String = "com.mysql.cj.jdbc.Driver"
  //jdbc:mysql://localhost:3306/mydb?user=root&password=root&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
  var url: String = "jdbc:mysql://localhost:3306/mydb?user=root&password=root&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
  val db = Database.forURL(url, driver = driverName)

  // creating mapping class
  private class EmployeeTable(tag: Tag)
      extends Table[EmployeeRow](tag, "Employee") {
    def employeeId = column[Int]("EMPLOYEEID", O.PrimaryKey, O.AutoInc)

    def employeeName = column[String]("EMPLOYEENAME")
    def gender = column[String]("GENDER")
    def salary = column[Double]("salary")

    def * =
      (employeeId, employeeName, gender, salary) <> ((EmployeeRow.apply _).tupled, EmployeeRow.unapply)
  }

  // creating instance of mapping class
  private val employees = TableQuery[EmployeeTable]

  def createEmployee(emp: EmployeeRow): Future[Int] = {
    var action = employees.insertOrUpdate(emp) // generating insert query
    var result: Future[Int] = db.run(action) // executing insert query
    return result
  }

  def createEmployeeWithPlus(emp: EmployeeRow): Future[Int] = {
    val action = employees += emp;
    action.statements.foreach(println(_))
    val result = db.run(action)
    result
  }

  def createListofEmployees(emps: List[EmployeeRow]): Unit = {
    var action = employees ++= (emps)
    action.statements.foreach(println(_))
    db.run(action)
  }

  def deleteEmployee(empId: Int, emp: EmployeeRow): Unit = {
    var x = employees.filter(rows => rows.employeeId === empId)
    println(x)
    var action = x.delete
    action.statements.foreach(println(_))
    db.run(action)

  }

  def getAllEmployee(): Future[Any] = {
    var action = employees.result
    action.statements.foreach(println(_))
    db.run(action)
  }

  /**
    *Get Top-5 highest Paid Employees from database employee table
    *
    */
  def topEmployees(num: Int): Future[Any] = {
    val action =
      employees.sortBy(emp => emp.salary.desc.nullsLast).take(num).result
    return db.run(action)
  }

  /**
    *. Get All Employees Where Name Contains v or V from database employee table
    */
  def getEmployeeStartsWith(str: String): Future[Any] = {
    val action = employees
      .filter(
        emp =>
          emp.employeeName.startsWith(str) || emp.employeeName.startsWith(str)
      )
      .result
//    action.statements.foreach(println(_))
    db.run(action)
  }

  /**
    *Get a max salary and also an Employee whose salary is Max
    * (if need write two methods. One is to get max salary and
    * another is to get Employee row
    */
  def getMaxSalEmp(): Future[Any] = {
    val action =
      employees.sortBy(emp => emp.salary.desc.nullsLast).take(1).result
    return db.run(action)
  }

  /**
    *Get a min salary and also an Employee whose salary is Min
    * (if need write two methods.
    * One is to get min salary and another is to get Employee row)
    */
  def getMinSalEmp(): Future[Any] = {
    val action =
      employees.sortBy(emp => emp.salary.asc.nullsLast).take(1).result
    return db.run(action)
  }

  /**
    * Get an average salary of employees. (Double)
    */
  def getAvgSal(): Unit = {

    val action = employees.map(x => x.salary).result
      db.run(action).onComplete({
      case Success(x:Vector[Double])=> println(x.reduce(_+_)/x.length)
      case Failure(err)=>
        println("error inside getAvgSal")

    })
  }


  def getEmployeesSal_>(sal: Double): Future[Any] = {
    val action = employees
      .filter(emp => emp.salary > sal)
      .result
    return db.run(action)
  }

  /**
   * Get all employees whose salary is more than the average salary of employees
   */
  def getGT_Avg_Sal(): Unit = {
    val action = employees.map(x => x.salary).result
    db.run(action).onComplete({
      case Success(x:Vector[Double])=> {
        println("Avg sal ",x.reduce(_+_)/x.length)
        getEmployeesSal_>(x.reduce(_+_)/x.length)
      }
      case Failure(err)=>
        println("error inside getAvgSal")

    })
  }

}
