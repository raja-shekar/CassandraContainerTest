package com.raj

import com.datastax.dse.driver.internal.core.insights.schema.PoolSizeByHostDistance
import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.{CqlSession, ProtocolVersion}
import com.datastax.oss.driver.api.core.config.{DefaultDriverOption, DriverConfig, DriverConfigLoader, DriverExecutionProfile, OptionsMap, TypedDriverOption}
import com.datastax.oss.driver.api.core.cql.Statement
import com.datastax.oss.protocol.internal.ProtocolConstants.{ConsistencyLevel, DataType}
import com.typesafe.config.{ConfigFactory, ConfigList}

import java.io.File
import java.lang.Boolean._
import java.net.InetSocketAddress
import java.time.Duration
import java.util
import java.util.Collections
import scala.jdk.CollectionConverters._


object TestConfig extends App {

  val configFile = "/Users/raj/WORK/ConfigReader/src/universal/conf/application.conf"

//  val configRead = ConfigFactory.parseFileAnySyntax(new File(configFile))
//
//  val config = configRead.getConfig("testConfig")
//
//  val configObject = config.getObject("mps-enabled-services")
//
//  val mpsEnabledService: Map[String, List[String]] = configObject.asScala.map {
//    case (name, obj: ConfigList ) =>
//      name -> obj.unwrapped().asScala.map(_.toString).toList
//  }.toMap
//
//  println(s"Final Map = $mpsEnabledService")

  val cassOptions = new OptionsMap()

 // cassOptions.put(TypedDriverOption.CONFIG_RELOAD_INTERVAL, Duration.ofMinutes(1))
  cassOptions.put(TypedDriverOption.CONTACT_POINTS, List("127.0.0.1:9042").asJava)
 // cassOptions.put(TypedDriverOption.APPLICATION_NAME, "LCP")
 // cassOptions.put(TypedDriverOption.SESSION_KEYSPACE, "KeyspaceName")

  //cassCluster.cassConnectTimeout
  cassOptions.put(TypedDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(30000))

  //cassCluster.cassConsistencyLevel
  cassOptions.put(TypedDriverOption.REQUEST_CONSISTENCY, "ONE")
  //Fetch size or page size not sure.
  cassOptions.put(TypedDriverOption.REQUEST_PAGE_SIZE, int2Integer(5000))
  cassOptions.put(TypedDriverOption.PROTOCOL_VERSION, ProtocolVersion.V4.name())

  cassOptions.put(TypedDriverOption.LOAD_BALANCING_POLICY_CLASS, "DefaultLoadBalancingPolicy")
  cassOptions.put(TypedDriverOption.RETRY_POLICY_CLASS, "DefaultRetryPolicy")
  cassOptions.put(TypedDriverOption.RECONNECTION_POLICY_CLASS, "ExponentialReconnectionPolicy")
  cassOptions.put(TypedDriverOption.RECONNECTION_BASE_DELAY, Duration.ofSeconds(1))
  cassOptions.put(TypedDriverOption.RECONNECTION_MAX_DELAY, Duration.ofSeconds(60))

  cassOptions.put(TypedDriverOption.SOCKET_KEEP_ALIVE, boolean2Boolean(true))
  //cassCluster.cassTcpNoDelay
  cassOptions.put(TypedDriverOption.SOCKET_TCP_NODELAY, boolean2Boolean(true))
  //The timeout to use when establishing driver connections.
  cassOptions.put(TypedDriverOption.CONNECTION_CONNECT_TIMEOUT, Duration.ofMillis(10))

  //cassCluster.cassRemoteMaxRequestsPerConnection or cassCluster.cassLocalMaxRequestsPerConnection
  cassOptions.put(TypedDriverOption.CONNECTION_MAX_REQUESTS, int2Integer(1))
  //cassCluster.cassLocalMaxConnectionsPerHost
  cassOptions.put(TypedDriverOption.CONNECTION_POOL_LOCAL_SIZE, int2Integer(1))
  //cassCluster.cassRemoteMaxConnectionsPerHost
  cassOptions.put(TypedDriverOption.CONNECTION_POOL_REMOTE_SIZE, int2Integer(1))
  //cassCluster.cassMaxSchemaAgreementWaitInSeconds
  cassOptions.put(TypedDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofSeconds(1))

  //The maximum number of live sessions that are allowed to coexist in a given VM
  cassOptions.put(TypedDriverOption.SESSION_LEAK_THRESHOLD, int2Integer(4))



  val session = CqlSession
    .builder()
    .withConfigLoader(DriverConfigLoader.fromMap(cassOptions))
    .build()

}
