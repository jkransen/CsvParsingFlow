package nl.kransen.mapheadtail

import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

class HeadTailMap[A, B](f: (A, A) => B) extends GraphStage[FlowShape[A, B]] {

  val in = Inlet[A]("Map.in")
  val out = Outlet[B]("Map.out")

  override val shape = FlowShape.of(in, out)

  override def createLogic(attr: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {
      var header: Option[A] = None
      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          header match {
            case Some(hdr) =>
              push(out, f(hdr, grab(in)))
            case None =>
              header = Some(grab(in))
              pull(in)
          }
        }
      })
      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          pull(in)
        }
      })
    }
}