import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.internal.core.metadata.DefaultEndPoint
import com.dimafeng.testcontainers.{CassandraContainer, ForAllTestContainer}
import com.raj.ScalarResultSet
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.net.InetSocketAddress

class CassandraSpec extends AnyFlatSpec with ForAllTestContainer with Matchers{
  override val container: CassandraContainer = CassandraContainer()

  container.start()

  "Cassandra container" should "be started" in {
    val session = CqlSession.builder()
      .addContactEndPoint(new DefaultEndPoint(InetSocketAddress.createUnresolved(
        container.cassandraContainer.getContainerIpAddress,
        container.cassandraContainer.getFirstMappedPort.intValue()
      ))).withLocalDatacenter("datacenter1").build()

    val rs = session.execute("select release_version from system.local")

    val scalarRS = new ScalarResultSet(rs)
    val row = scalarRS.next()

    row.getString("release_version").length should be > 0
  }
}
