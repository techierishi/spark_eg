/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.examples;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Function2;
import scala.Tuple2;

import java.io.File;
import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class WordCount {
	private static final Pattern SPACE = Pattern.compile(" ");

	public static void main(String[] args) throws Exception {
		System.out.println("main starting");
		if (args.length < 1) {
			System.err.println("Usage: WordCount <file>");
			System.exit(1);
		}

		System.out.println("after getting the file");


		final SparkConf sparkConf = new SparkConf().setAppName("WordCount");
		final JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		final JavaRDD<String> lines = ctx.textFile(args[0], 1);
		System.out.println("Converted to RDD");
		// convert each line into a collection of words
		JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
		@Override
		public Iterator<String> call(String s) throws Exception {
			return Arrays.asList(SPACE.split(s)).iterator();
		}
		});		
		// map each word to a tuple containing the word and the value 1
		JavaPairRDD<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
		public Tuple2<String, Integer> call(String word) { return new Tuple2<>(word, 1); }
		});

		// for all tuples that have the same key (word), perform an aggregation to add the counts
		JavaPairRDD<String, Integer> counts = pairs.reduceByKey(new org.apache.spark.api.java.function.Function2<Integer, Integer, Integer>() {
		@Override
		public Integer call(Integer a, Integer b) throws Exception {
			return a + b;
		}
		});

		final List<Tuple2<String, Integer>> output = counts.collect();
		for (Tuple2 tuple : output) {
			System.out.println(tuple._1() + ": " + tuple._2());
		}
		ctx.stop();
	}
}

