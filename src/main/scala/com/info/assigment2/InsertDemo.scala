package com.info.day9

import slick.jdbc.MySQLProfile.api._

object InsertDemo {

  def main(args: Array[String]): Unit = {

    var emp: EmployeeRow = EmployeeRow(3002, "Ravi", "Male", 78000)
    var emp2: EmployeeRow = EmployeeRow(3002, "Ravi", "Male", 78000)
    var emp3: EmployeeRow = EmployeeRow(3002, "Ravi", "Male", 78000)

    var dao: EmployeeDAO = new EmployeeDAO()

    //dao.createEmployeeWithPlus(emp)
    //dao.createListofEmployees(List(emp, emp2, emp3))
    //dao.deleteEmployee(3001)

    dao.getAllEmployee()

    /*println("Press any key to exit")
    System.in.read()
    print(result)*/

  }

}
