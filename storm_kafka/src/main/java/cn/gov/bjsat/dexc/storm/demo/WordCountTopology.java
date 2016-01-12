package cn.gov.bjsat.dexc.storm.demo;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import cn.gov.bjsat.dexc.storm.demo.bolt.WordCountBolt;
import cn.gov.bjsat.dexc.storm.demo.bolt.WordSplitBolt;
import kafka.javaapi.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import storm.kafka.KafkaSpout;

/**
 * Created by grf11_000 on 2015/11/30.
 */

public class WordCountTopology {

    private TopologyBuilder builder;

    @Autowired
    private KafkaSpout kafkaSpout;

    @Autowired
    private Producer producer;

    @Autowired
    private Config topologyConfig;


    public WordCountTopology() {
        builder = new TopologyBuilder();
    }


    /**
     * 设计 Storm Topology
     *
     * @param spoutNum 数据源Spout并发数
     * @param splitNum 分词Bolt并发数
     * @param countNum 统计Bolt并发数
     * @return
     */
    public TopologyBuilder getBuilder(Integer spoutNum, Integer splitNum, Integer countNum) {

        //指定Spout，此处为 Kafka，id为 kafka_sentence_reader，下面的Bolt会指定此名为数据源
        builder.setSpout("kafka_sentence_reader", kafkaSpout, spoutNum);

        //第一个 Bolt ，ID为 word_split，使用 shuffleGrouping 随机发送数据到所指定 splitNum 数中的任意一个 word_split，基本是平均的。
        //每个 word_split 或者 Bolt 称为一个 Task
        builder.setBolt("word_split", new WordSplitBolt(), splitNum).shuffleGrouping("kafka_sentence_reader");

        //第二个Bolt ，ID为 word_count，单词记数，使用 fieldsGrouping 分组， 将相同单词发送到所指定 countNum 数中固定的 word_count中
        //如遇到 the 这个单词始终发送到 第一个 word_count 中，也就是其中的一个 Task。
        // 此处指定使用 word_split 中的 word 字段进行分组，同样可以指定其它字段，但要在 word_split Bolt中申明。
        builder.setBolt("word_count", new WordCountBolt(), countNum).fieldsGrouping("word_split", new Fields("word"));


        return builder;
    }


    public Config getTopologyConfig() {
        return topologyConfig;
    }

}
