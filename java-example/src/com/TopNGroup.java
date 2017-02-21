package com;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;
 

public class TopNGroup {

 public static void main(String[] args) {
  // TODO Auto-generated method stub
 SparkConf conf = new SparkConf().setAppName("TopNGroup").setMaster("local");
  
 JavaSparkContext sc = new JavaSparkContext(conf); //其底层实际上就是Scala的SparkContext
    JavaRDD<String> lines = sc.textFile("/usr/local/spark/README.md");
 
    
    JavaPairRDD<String, Integer> pairs = lines.mapToPair(new PairFunction<String, String, Integer>() {
        private static final long serialVersionUID =1L ;
  @Override
  public Tuple2<String, Integer> call(String line) throws Exception {
   // TODO Auto-generated method stub
   String[] splitedLine =line.split(" ");
     System.out.println(splitedLine[0]);
   return new Tuple2<String,Integer>(splitedLine[0],Integer.valueOf(splitedLine[1]));
  }
 });
  
    JavaPairRDD<String, Iterable<Integer>>  groupedPairs =pairs.groupByKey();
  
    JavaPairRDD<String, Iterable<Integer>> top5=groupedPairs.mapToPair(new

PairFunction<Tuple2<String,Iterable<Integer>>, String, Iterable<Integer>>() {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public Tuple2<String, Iterable<Integer>> call(Tuple2<String, Iterable<Integer>> groupedData)

throws Exception {
   // TODO Auto-generated method stub
   
   Integer[] top5=new Integer[5];
   String groupedKey= groupedData._1;
   Iterator<Integer> groupedValue = groupedData._2.iterator();
   
   while(groupedValue.hasNext()){
    Integer value = groupedValue.next();
    
    for (int i =0; i<5; i++){
     if (top5[i] ==null) {
      top5[i] = value ;
      break;
     } else if (value > top5[i]) {
      for (int j = 4; j > i; j--){
       top5[j] = top5[j-1];
      }
      top5[i]=value;
      break;
     }    
     
    }
    
   } 
  
   return new Tuple2<String, Iterable<Integer>>(groupedKey,Arrays.asList(top5));
  }
 }) ;
     
 top5.foreach(new VoidFunction<Tuple2<String,Iterable<Integer>>>() {

  @Override
  public void call(Tuple2<String, Iterable<Integer>> topped) throws Exception {
   // TODO Auto-generated method stub
   System.out.println("Group key :"+ topped._1);
   Iterator<Integer> toppedValue = topped._2.iterator();
   while (toppedValue.hasNext()){
    Integer value =toppedValue.next();
    System.out.println(value);    
   }
   System.out.println("**********************");
  }
 });
 }

}

//http://blog.csdn.net/duan_zhihua/article/details/50812408
