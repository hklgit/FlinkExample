package duoyi;

import akka.remote.transport.ProtocolStateActor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import scala.Tuple2;

import java.util.Properties;

/**
 * kafkaSource
 *
 * Created by   keven han  on  2020/02/22
 */
public class KeyedState {

    public static void main(String[] args) throws Exception {
        //获取Flink的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //checkpoint配置
        env.enableCheckpointing(5000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //这里设置的是 当程序被干掉的时候，我们重启的时候会不会把之前的checkpoint的数据干掉，这个设置的是保留
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        //设置statebackend

        //env.setStateBackend(new RocksDBStateBackend("hdfs://hadoop100:9000/flink/checkpoints",true));


        String topic = "test1204";
        Properties prop = new Properties();
        prop.setProperty("bootstrap.servers","192.168.2.140:9092");
        prop.setProperty("group.id","test1204");

        FlinkKafkaConsumer011<String> myConsumer = new FlinkKafkaConsumer011<>(topic, new SimpleStringSchema(), prop);

        myConsumer.setStartFromGroupOffsets();//默认消费策略

        DataStreamSource<String> text = env.addSource(myConsumer);
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne = text.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String value) throws Exception {
                return Tuple2.apply(value, 1);
            }
        });

        //我们实现一个有状态的累加的比较简单的办法。
//        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = wordAndOne.keyBy(0).sum(1);

        //接下来我们自己来实现一个基于状态管理的达到相同累加效果的。
        wordAndOne.map(new RichMapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>>() {

            //如果没有的话就是啥都没有一开始，如果之前有的话那就是按照 valueDescriptor的名字来找到 对应的值
            private transient ValueState<Tuple2<String, Integer>> state;
            @Override
            public void open(Configuration parameters) throws Exception {
                //初始化或者恢复状态

                //名称以及数据的类型
                ValueStateDescriptor<Tuple2<String, Integer>> valueDescriptor = new ValueStateDescriptor("my-state",
//                        TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {}
                                Types.TUPLE(Types.STRING,Types.INT)
                );
                state = getRuntimeContext().getState(valueDescriptor);
            };

            @Override
            public Tuple2<String, Integer> map(Tuple2<String, Integer> value) throws Exception {

                String word = value._1;
                Integer count = value._2;

                Tuple2<String, Integer> historyKV = state.value();

                if( historyKV != null ){
                    Integer hisV = historyKV._2;
                    //如果是之前有的，那么我们就直接把当前值和历史值做一个累加
                    hisV += count;
                    state.update(historyKV);
                    return historyKV;
                }else{
                    state.update(value);
                    return  value;
                }

            }
        });


        text.print().setParallelism(1);




        env.execute("StreamingFromCollection");


    }
}
