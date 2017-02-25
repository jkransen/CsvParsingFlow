package nl.kransen.mapheadtail

import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

class HeadTailMap[A, C, B](convertLine: A => C)(combine: (C, C) => B) extends GraphStage[FlowShape[A, B]] {

  val in = Inlet[A]("Map.in")
  val out = Outlet[B]("Map.out")

  override val shape = FlowShape.of(in, out)

  override def createLogic(attr: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {
      var header: Option[C] = None
      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val line = convertLine(grab(in))
          header match {
            case Some(hdr) =>
              push(out, combine(hdr, line))
            case None =>
              header = Some(line)
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