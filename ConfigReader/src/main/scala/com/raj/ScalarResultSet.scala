package com.raj

import com.datastax.oss.driver.api.core.{CqlIdentifier, ProtocolVersion}
import com.datastax.oss.driver.api.core.`type`.DataType
import com.datastax.oss.driver.api.core.`type`.codec.registry.CodecRegistry
import com.datastax.oss.driver.api.core.cql.{ColumnDefinitions, ResultSet, Row}
import com.datastax.oss.driver.api.core.detach.AttachmentPoint

import java.nio.ByteBuffer

trait ScalarResultSet[T] extends Iterator[T]

trait Mapper{
  def mapper[T](f:ScalarRow => Option[T]):ScalarResultSet[T]
}

class ScalarResultSetWrapper(scalarResultSet: ResultSet) extends ScalarResultSet[ScalarRow] with Mapper {
  self =>
  override def hasNext: Boolean = scalarResultSet.iterator().hasNext

  override def next(): ScalarRow = new ScalarRow(scalarResultSet.iterator().next())

  override def mapper[T](f: ScalarRow => Option[T]): ScalarResultSet[T] = new ScalarResultSet[T]{
    val newIt = self.flatMap(f)
    override def hasNext: Boolean = newIt.hasNext
    override def next(): T = newIt.next()
  }
}

class ScalarRow(row: Row) extends Row{

  override def getString(i: Int): String = super.getString(i)
  override def getString(name: String): String = super.getString(name)

  override def getInt(i: Int): Int = super.getInt(i)
  override def getInt(name: String): Int = super.getInt(name)

  override def getColumnDefinitions: ColumnDefinitions = {
    row.getColumnDefinitions
  }

  override def firstIndexOf(name: String): Int = {
    row.firstIndexOf(name)
  }

  override def getType(name: String): DataType = {
    row.getType(name)
  }

  override def getBytesUnsafe(i: Int): ByteBuffer = {
    row.getBytesUnsafe(i)
  }

  override def firstIndexOf(id: CqlIdentifier): Int = {
    row.firstIndexOf(id)
  }

  override def getType(id: CqlIdentifier): DataType = {
    row.getType(id)
  }

  override def size(): Int = {
    row.size()
  }

  override def getType(i: Int): DataType = {
    row.getType(i)
  }

  override def codecRegistry(): CodecRegistry = {
    row.codecRegistry()
  }

  override def protocolVersion(): ProtocolVersion = {
    row.protocolVersion()
  }

  override def isDetached: Boolean = {
    row.isDetached
  }

  override def attach(attachmentPoint: AttachmentPoint): Unit = {
    row.attach(attachmentPoint)
  }
}
