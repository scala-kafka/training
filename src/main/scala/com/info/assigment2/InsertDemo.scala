package com.info.day9

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.MySQLProfile.api._

object InsertDemo {

  def main(args: Array[String]): Unit = {

    var emp: EmployeeRow = EmployeeRow(3006, "geethu", "Female", 20000)
    var emp2: EmployeeRow = EmployeeRow(3007, "lins", "Male", 15300)
    var emp3: EmployeeRow = EmployeeRow(3008, "raju", "Female", 12000)

    var dao: EmployeeDAO = new EmployeeDAO()

//    dao.createEmployeeWithPlus(emp).onComplete({
//            case Success(x)=>print(x)
//            case Failure(err)=>println(err)
//          })
//    dao.createListofEmployees(List(emp, emp2, emp3))
    //dao.deleteEmployee(3001)

//    dao.getAllEmployee().onComplete({
//      case Success(x)=>print(x)
//      case Failure(err)=>println(err)
//    })

//    dao.topEmployees(5).onComplete({
//      case Success(x)=>print(x)
//      case Failure(err)=>println(err)
//    })

//    dao.getEmployeeStartsWith("g").onComplete({
//      case Success(x)=>print(x)
//      case Failure(err)=>println(err)
//    })

//    dao.getMaxSalEmp().onComplete({
//      case Success(x)=>print(x)
//      case Failure(err)=>println(err)
//    })
//
//    dao.getMinSalEmp().onComplete({
//      case Success(x)=>print(x)
//      case Failure(err)=>println(err)
//    })

        dao.getAvgSal()
//          .onComplete({
//          case Success(x)=>print(x)
//          case Failure(err)=>println(err)
//        })

    println("Press any key to eyxit")
    System.in.read()
//    print(result)

  }

}
