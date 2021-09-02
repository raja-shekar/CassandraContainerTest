package com.raj

import com.datastax.oss.driver.api.core.cql.{ResultSet, Row}

class ScalarResultSet(scalarResultSet: ResultSet) extends Iterator[Row]{
  override def hasNext: Boolean = scalarResultSet.iterator().hasNext
  override def next(): Row = scalarResultSet.iterator().next()
}
