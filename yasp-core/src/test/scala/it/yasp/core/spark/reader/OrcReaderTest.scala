package it.yasp.core.spark.reader

import it.yasp.core.spark.model.Source.Orc
import it.yasp.core.spark.reader.Reader.OrcReader
import it.yasp.testkit.{SparkTestSuite, TestUtils}
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.DataTypes.StringType
import org.apache.spark.sql.types.{StructField, StructType}
import org.scalatest.DoNotDiscover
import org.scalatest.funsuite.AnyFunSuite

@DoNotDiscover
class OrcReaderTest extends AnyFunSuite with SparkTestSuite {

  private val workspace = "yasp-core/src/test/resources/OrcReaderTest"

  override protected def beforeAll(): Unit = {
    TestUtils.cleanFolder(workspace)
    super.beforeAll()
  }

  override protected def afterAll(): Unit = {
    TestUtils.cleanFolder(workspace)
    super.afterAll()
  }

  test("read") {
    val expected = spark
      .createDataset(Seq(Row("d", "e", "f", "g")))(
        RowEncoder(
          StructType(
            Seq(
              StructField("h0", StringType, nullable = true),
              StructField("h1", StringType, nullable = true),
              StructField("h2", StringType, nullable = true),
              StructField("h3", StringType, nullable = true)
            )
          )
        )
      )

    expected.write.orc(s"$workspace/orcData/")

    val actual = new OrcReader(spark).read(Orc(s"$workspace/orcData/"))
    assertDatasetEquals(actual, expected)
  }

}
