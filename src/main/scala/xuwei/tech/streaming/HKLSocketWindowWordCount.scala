package xuwei.tech.streaming

import java.security.Policy.Parameters

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * Created by Kerven on 2019/6/20.
  *
  * 　　　　　　　   ┏┓　   ┏┓+ +
  * 　　　　　　　┏┛┻━━━┛┻┓ + +
  * 　　　　　　　┃　　　　　　　┃
  * 　　　　　　　┃　　　━　　　┃ ++ + + +
  * 　　　　　　 ████━████ ┃+
  * 　　　　　　　┃　　　　　　　┃ +
  * 　　　　　　　┃　　　┻　　　┃
  * 　　　　　　　┃　　　　　　　┃ + +
  * 　　　　　　　┗━┓　　　┏━┛
  * 　　　　　　　　　┃　　　┃
  * 　　　　　　　　　┃　　　┃ + + + +
  * 　　　　　　　　　┃　　　┃			
  * 　　　　　　　　　┃　　　┃ +			
  * 　　　　　　　　　┃　　　┃
  * 　　　　　　　　　┃　　　┃　　+
  * 　　　　　　　　　┃　 　　┗━━━┓ + +
  * 　　　　　　　　　┃ 　　　　　　　┣┓
  * 　　　　　　　　　┃ 　　　　　　　┏┛
  * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
  * 　　　　　　　　　　┃┫┫　┃┫┫
  * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
  *
  * Don't bibi , show me the code
  *
  *
  *入门程序 wordcount
  *
  * nc -l 9000
  * 直接走起
  *
  */
object HKLSocketWindowWordCount {

  case class WordCount(word:String,count:Int)

  def main(args: Array[String]): Unit = {

    // first step get the env
    val env = StreamExecutionEnvironment.getExecutionEnvironment


//    ParameterTool.fromArgs(args).getInt("port")

    // import this is important for implict transformation
    import org.apache.flink.api.scala._

   val ds = env.socketTextStream("192.168.0.159",9000)


    // keyby 逻辑上将一个流分成不相交的分区，每个分区包含相同键的元素。在内部，这是通过散列分区来实现的
    val result = ds.flatMap(data => data.split("\\s"))
      .map(word => WordCount(word, 1))
      .keyBy("word")
      .timeWindow(Time.seconds(5), Time.seconds(1))
      .sum("count")

    result.print().setParallelism(1)

    env.execute("HKLSocketWindowWordCount")













  }


}
