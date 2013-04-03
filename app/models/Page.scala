package models

import collection.mutable.ArrayBuffer

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-10-31
 * Time: 上午11:33
 * ***********************
 * description:用于类的说明
 */
/*
* items: 返回当前页的list数据
* page:当前页
* pageSize: 每页显示的条数
* totalRows:
* */
case class Page[A](items: List[A], currentPage: Int, totalPages: Int) {
  lazy val prev = Option(currentPage - 1).filter(_ > 0)
  lazy val next = Option(currentPage + 1).filter(_ <= totalPages)
  var buf = new ArrayBuffer[Int]()
  /*判断当前页是否小于totalPages*/
  if(currentPage<=totalPages){
    if(currentPage-3 > 0) buf +=currentPage-3
    if(currentPage-2 > 0) buf+=currentPage-2
    if(currentPage-1 > 0)buf+=currentPage-1
    buf+=currentPage
    if(currentPage+1 <= totalPages) buf+=currentPage+1
    if(currentPage+2 <= totalPages) buf+=currentPage+2
    if(currentPage+3 <= totalPages) buf+=currentPage+3
  }  else buf +=totalPages


  /*显示的页数*/
  val nums =buf.toArray
  

}
