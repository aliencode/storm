package cn.gov.bjsat.dexc.storm.demo.bolt;


import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cn.gov.bjsat.dexc.storm.demo.utils.ApplicationContextHolder;
import kafka.javaapi.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by grf11_000 on 2015/11/30.
 */

public class WordCountBolt extends BaseBasicBolt {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, Integer> counterMap;

    //所有此基于此Bolt的Task公用变量
    private static transient Producer producer;

    //此Task全局变量
    private String temp;


    /**
     * 这个类将被实例化一次在所有的Task中

     */
    public WordCountBolt() {
        logger.debug("初始化WordCountBolt");
        producer = ApplicationContextHolder.getBean("producer");
    }

    /**
     * 在Task启动的时候调用一次prepare，之后的数据处理直接进入execute方法
     * 在这里可以做一些线程安全的初始化工作。
     * @param stormConf
     * @param context
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        this.counterMap = new HashMap<String, Integer>();
        logger.debug("WordCountBolt一些准备工作");
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {

        //读取消息流中第0个索引字段的内容
        // 多个字段参考 WordSplitBolt.declareOutputFields() 方法说明
        String word = input.getString(0);

        Integer count = counterMap.get(word);
        if (count == null){
            count = 0;
        }
        count++;
        counterMap.put(word, count);

        //在这里可以通过 producer 发送数据到 kafka 等其它业务逻辑
        producer.close();
        logger.debug("本Blot处理结果打印: {}", this.counterMap);

    }


    /**
     * 不需要输出，没有指定字段，execute 方法中也不需要 emit
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

}
