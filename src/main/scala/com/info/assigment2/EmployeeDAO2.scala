package com.info.day9

import scala.concurrent.ExecutionContext.Implicits.global

import slick.jdbc.MySQLProfile.api._
import scala.concurrent.Future
import java.sql.Date

class EmployeeDAO2 {
  val db = Database.forURL(
    "jdbc:mysql://localhost:3306/mydb?user=root&password=root",
    driver = "com.mysql.jdbc.Driver"
  )

  //Table Mapper definition to map table column to Scala Rows/Instances
  private class EmployeeTable(tag: Tag)
      extends Table[EmployeeRow](tag, "Employee") {
    def employeeId = column[Int]("EMPLOYEEID", O.PrimaryKey, O.AutoInc)

    //other Columns
    def employeeName = column[String]("EMPLOYEENAME")
    def gender = column[String]("GENDER")
    def salary = column[Double]("SALARY")

    def * =
      (employeeId, employeeName, gender, salary) <> ((EmployeeRow.apply _).tupled, EmployeeRow.unapply)

  }

  private val employees = TableQuery[EmployeeTable]

  //Streams come for PUB SUB Model hence ignore the same
  def getAllEmployees(): Future[Seq[EmployeeRow]] = {
    //var action: DBIOAction[Seq[EmployeeRow], NoStream, Nothing] = employees.result;
    //var action2 = employees.result;

    //Step1:Selection, it is required to select all hence nothing done

    //Step2: Creating DBIO
    var action1: DBIO[Seq[EmployeeRow]] = employees.result

    //Step3: Executing the DBIO to get the Future
    db.run(action1)
  }

  def getAllEmployeesUsingForComperhension(): Future[Seq[EmployeeRow]] = {
    var comp = for (emprow <- employees) yield emprow

    var action = comp.result //var action:DBIO[Seq[EmployeeRow]] = comp.result
    printingQueryForAction(action.statements)

    db.run(action)
  }

  def findEmployeeById(employeeId: Int): Future[Seq[EmployeeRow]] = {

    var comp = for (emprow <- employees if emprow.employeeId === employeeId)
      yield emprow

    var action = comp.take(1).result
    printingQueryForAction(action.statements)

    db.run(action)
  }

  def getAllEmployeesSelectedColumnsUsingComp()
    : Future[Seq[(Int, String, Double)]] = {
    var comp = for (emprow <- employees)
      yield (emprow.employeeId, emprow.employeeName, emprow.salary)

    var action = comp.result;

    printingQueryForAction(action.statements)
    db.run(action)
  }

  def getAllEmployeesSelectedColumnsUsingLambads()
    : Future[Seq[(Int, String, Double)]] = {
    var comp =
      employees.map(row => (row.employeeId, row.employeeName, row.salary))

    var action = comp.result; //DBIO[Seq[(Int, String, Double)]]
    printingQueryForAction(action.statements)

    //Step3: Executing the DBIO to get the Future
    var ret: Future[Seq[(Int, String, Double)]] = db.run(action)
    return ret
  }

  //Sorting and
  def getAllEmployeesSalBetweenAndOrderBy(
    lowerBound: Double,
    upperBound: Double,
    order: String
  ): Future[Seq[EmployeeRow]] = {

    if (order == "desc") {
      var action = employees
        .filter(emp => emp.salary >= lowerBound && emp.salary <= upperBound)
        .sortBy(emp => emp.salary.desc.nullsFirst)
        .result
      action.statements.foreach(println(_))
      db.run(action)
    } else {
      db.run(
        employees
          .filter(emp => emp.salary >= lowerBound && emp.salary <= upperBound)
          .sortBy(emp => emp.salary.asc.nullsLast)
          .result
      )
    }
  }

  def printingQueryForAction(st: Iterable[Any]) {
    st.foreach(x => println(x))
  }
}
//employeeId:Int, employeeName: String, gender: String,dateOfbirth: Date,salary: Double,departmentCode:String
