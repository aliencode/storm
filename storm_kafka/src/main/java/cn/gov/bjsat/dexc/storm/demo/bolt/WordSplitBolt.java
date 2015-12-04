package cn.gov.bjsat.dexc.storm.demo.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by grf11_000 on 2015/11/30.
 */

public class WordSplitBolt extends BaseBasicBolt {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {

        //读取第0个字段的内容，更多参考 WordCountBolt
        String line = input.getString(0);

        String[] words = line.split(" ");
        for (String word : words) {
            word = word.trim();
            if (StringUtils.isNotBlank(word)) {
                word = word.toLowerCase();

                //发射数据，参考 下面 declareOutputFields
                collector.emit(new Values(word));
            }
        }
    }


    /**
     * 字段输出的字段申明
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //消息流分组，可以理解为一条数据的几个字段
        // 可以申明为 declarer.declare(new Fields("word", "id"));  对就的emit应为： collector.emit(new Values(word, "id1"));
        declarer.declare(new Fields("word"));
    }

}
