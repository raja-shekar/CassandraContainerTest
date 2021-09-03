
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import com.datastax.oss.driver.internal.core.metadata.DefaultEndPoint
import com.dimafeng.testcontainers.{CassandraContainer, Container, ForAllTestContainer}
import com.raj.{ScalarResultSet, ScalarResultSetWrapper}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.net.InetSocketAddress

case class Customer(id: Int, name: String)

class CassandraRowSpec extends AnyFlatSpec with ForAllTestContainer with Matchers {
  override val container: CassandraContainer = CassandraContainer()

  container.start()

  val session = CqlSession.builder()
    .addContactEndPoint(new DefaultEndPoint(InetSocketAddress.createUnresolved(
      container.cassandraContainer.getContainerIpAddress,
      container.cassandraContainer.getFirstMappedPort.intValue()
    ))).withLocalDatacenter("datacenter1").build()
  session.execute("CREATE KEYSPACE IF NOT EXISTS gb WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }")
  session.execute("CREATE TABLE IF NOT EXISTS gb.test(id int, name text, PRIMARY KEY(id) )")

  val stmt = session.prepare(s"INSERT INTO gb.test(id, name) VALUES(?,?)")
  for (i <- 1 to 100) {
    session.execute(stmt.bind().setInt("id", i).setString("name", s"test $i"))
  }

  val s = SimpleStatement.newInstance("select id, name from gb.test").setPageSize(10)

  "All Customers" should "be printed" in {
    val result = new ScalarResultSetWrapper(session.execute(s))
    val res: ScalarResultSet[Customer] = result.mapper(row => Option(Customer(row.getInt("id"), row.getString("name"))))
    var counter = 0
    while(res.hasNext) {
      val customer = res.next()
//      println(s"${customer.id} and name ${customer.name}")
      counter += 1
    }
     counter should be >= 100
  }

  "Cassandra container" should "be started" in {
    val result = new ScalarResultSetWrapper(session.execute(s))
    var counter = 0
    while (result.hasNext) {
      result.next()
      counter += 1
    }
    counter should be >= 100
  }

}
//
//class CassandraSpec extends AnyFlatSpec with ForAllTestContainer with Matchers {
//  override val container: CassandraContainer = CassandraContainer()
//
//  container.start()
//
//  "Cassandra container" should "be started" in {
//    val session = CqlSession.builder()
//      .addContactEndPoint(new DefaultEndPoint(InetSocketAddress.createUnresolved(
//        container.cassandraContainer.getContainerIpAddress,
//        container.cassandraContainer.getFirstMappedPort.intValue()
//      ))).withLocalDatacenter("datacenter1").build()
//    session.execute("CREATE KEYSPACE IF NOT EXISTS gb WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }")
//    session.execute("CREATE TABLE IF NOT EXISTS gb.test(id int, name text, PRIMARY KEY(id) )")
//    val stmt = session.prepare(s"INSERT INTO gb.test(id, name) VALUES(?,?)")
//
//
//    for (i <- 1 to 100) {
//      session.execute(stmt.bind().setInt("id", i).setString("name", s"test $i"))
//    }
//
//    val s = SimpleStatement.newInstance("select id, name from gb.test").setPageSize(10)
//
//    val result = new ScalarResultSetWrapper(session.execute(s))
//    var counter = 0
//    while (result.hasNext) {
//      result.next()
//      counter += 1
//    }
//    counter should be >= 100
//
//    //    val rs = session.execute("select release_version from system.local")
//    //
//    //    val scalarRS = new ScalarResultSetWrapper(rs)
//    //    val row = scalarRS.next()
//    //    val test = scalarRS.mapper(row => Option(Version(row.getString("release_version"))))
//    //    test.next().version
//    //    row.getString(1)
//    //    row.getString("release_version").length should be > 0
//  }
//}
