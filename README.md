Spark examples
==============

Simple examples of using the [Apache Spark](http://spark.apache.org/).

Spark submit options:

https://spark.apache.org/docs/latest/submitting-applications.html

To compile :
mvn package

To Run this project :
Got to target folder ->
spark-submit --class org.apache.spark.examples.WordCount --master local[4] spark-examples-1.0.jar ../words.txt




