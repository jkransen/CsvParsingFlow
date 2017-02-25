package nl.kransen.mapheadtail

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.{Source => IoSource}

class HeadTailFlowSpec extends FlatSpec {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  "HeadTailFlow" should "swallow first element" in {
    val lineStream = IoSource.fromFile("src/test/resources/withheader.csv").getLines().toStream

    val flow = Source[String](lineStream)

    def parseLine(str: String) = str.split(",")
    def combiner(header: Array[String], record: Array[String]) = header.zip(record).toMap

    val subject = new HeadTailMap(parseLine)(combiner)

    val resultFuture = flow.via(subject).runFold(List[Map[String, String]]())((list: List[Map[String, String]], next: Map[String, String]) => list :+ next)

    val result = Await.result(resultFuture, 1 second)

    assert(result.size == 2)
    val line1 = result(0)
    assert("\"aap\"".equals(line1.get("\"first\"").get))
    assert("\"noot\"".equals(line1.get("\"second\"").get))
    assert("\"mies\"".equals(line1.get("\"third\"").get))
    val line2 = result(1)
    assert("\"wim\"".equals(line2.get("\"first\"").get))
    assert("\"zus\"".equals(line2.get("\"second\"").get))
    assert("\"jet\"".equals(line2.get("\"third\"").get))
  }

}
