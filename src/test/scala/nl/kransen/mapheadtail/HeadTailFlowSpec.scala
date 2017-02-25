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

    val flow = Source[String](lineStream) // .toMat(Sink.fold()

    def f(header: String, str: String): String = header + str

    val subject = new HeadTailMap(f)

    val resultFuture = flow.via(subject).runFold(List[String]())((list: List[String], next: String) => list :+ next)

    val result = Await.result(resultFuture, 1 second)

    assert(result.size == 1)
    val head = result.head
    println(s"head: $head")
    assert(""""first","second","third""aap","noot","mies"""".equals(head))
  }
}
