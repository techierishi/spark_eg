# spark_eg

compile using :
mvn package

to push it to spark-submit:
spark-submit --class org.apache.spark.examples.WordCount --master local[4] spark-examples-1.0.jar ../words.txt
