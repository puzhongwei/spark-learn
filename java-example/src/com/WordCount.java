package com;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
public class WordCount{
public static void main( String[] args )
{
	 String readme="/usr/local/spark/test.txt";
	//String readme="/usr/local/spark/test";
    SparkConf conf=new SparkConf().setAppName("tiger's first spark app").setMaster("local");
    JavaSparkContext sc =new JavaSparkContext(conf);
    JavaRDD<String> logData=sc.textFile(readme).cache();
    ///////////////////////////////////////////////////////////
    JavaRDD<String> words =  logData.flatMap( s -> Arrays.asList(s.split(" ")).iterator()); //切割
	JavaPairRDD<String, Integer> pairs = words.mapToPair(s -> new Tuple2<String, Integer>(s, 1));//将每一个单词，映射为(单词, 1)的这种格式
    // 因为只有这样，后面才能根据单词作为key，来进行每个单词的出现次数的累加
	JavaPairRDD<String, Integer> counts = pairs.reduceByKey((a, b) -> a + b);
	//如果要实现按单词出现的次数从高低排名，1，首先要tuple的key value值，然后再按key排序，然后再交换过来
	JavaPairRDD<Integer, String> tmp = counts.mapToPair(s->new Tuple2<Integer, String>(s._2,s._1)).sortByKey(false);
	JavaPairRDD<String, Integer> result = tmp.mapToPair(s->new Tuple2<String, Integer>(s._2,s._1));
	result.foreach(a -> System.out.println(a._1+"=>"+a._2()));
	sc.stop();
    
}
}
//http://www.myexception.cn/program/2092601.html  example
