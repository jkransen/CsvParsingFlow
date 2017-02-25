package nl.kransen.mapheadtail

import akka.stream.scaladsl.{Sink, Source}
import org.scalatest._

import scala.io.{Source => IoSource}

class HeadTailFlowSpec extends FlatSpec {

  "HeadTailFlow" should "swallow first element" in {
    val lineStream = IoSource.fromFile("src/test/resources/withheader.csv").getLines().toStream

    val flow = Source[String](lineStream).toMat(Sink.fold(List[String]())((list: List[String], next: String) => list :+ next))

    flow.run()
  }
}
