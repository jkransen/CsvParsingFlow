package nl.kransen.mapheadtail

import akka.stream.FlowShape
import akka.stream.stage.GraphStage
import com.opencsv.CSVParser

object CsvZippingMap {
  def create(): GraphStage[FlowShape[String, Map[String, String]]] = {
    val csvParser = new CSVParser()
    def parseLine(line: String): Array[String] = {
      csvParser.parseLine(line)
    }
    def combiner(header: Array[String], record: Array[String]) = header.zip(record).toMap
    new HeadTailMap(parseLine)(combiner)
  }
}
