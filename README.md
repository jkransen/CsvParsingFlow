# CsvParsingFlow

A Flow for Akka Streams where the first (header) element is swallowed and zipped to subsequent elements. 

## Incentive

CSV files are perfect matches for Reactive Streams, but the most basic use case using a map function is not applicable. This is due to the fact that map methods output precisely n elements when n go in. CSV files/streams should output precisely n-1 elements, because the first record is the header. Also, traditional map functions do not have state. 

## Solution

This Akka Stream component, a Flow component with one input and one output, is aimed to solve these issues. The first element is swallowed, and joined with all the subsequent elements. The typical use case is CSV files, as stated, but the pattern itself is free from CSV logic and can easily be applied to your own use case. 

## Usage

    val csv = List(""""first","second","third"""", 
                   """"aap","noot","mies"""",
                   """"wim","zus","jet"""")

    val flow = Source[String](csv).via(CsvZippingMap.create())

    flow.runForeach(map => println(map))

This will result in:

    Map(first -> aap, second -> noot, third -> mies)
    Map(first -> wim, second -> zus, third -> jet)

## Customization

If you want another data structure, like a Case Class, this should be 
easy to hook into the code. Just look at the CsvZippingMap and replace it 
with your own implementation.

For now I assumed that the header element is always the same type as subsequent elements (e.g. String),
but even this should be easy to change, by adding an additional type (e.g. H) to the HeadTailMap,
and use it for the header.

