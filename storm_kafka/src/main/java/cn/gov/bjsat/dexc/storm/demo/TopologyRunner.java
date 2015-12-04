package cn.gov.bjsat.dexc.storm.demo;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by grf11_000 on 2015/12/1.
 */
public class TopologyRunner {

    public static void main(String[] args){
        Logger logger = LoggerFactory.getLogger("WordCountTopologyRunner");

        //初始化Spring上下文
        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-stormKafka.xml");
        WordCountTopology topology = (WordCountTopology) applicationContext.getBean("wordCountTopology");

        //配置Storm Topology
        Config topologyConfig = topology.getTopologyConfig();
        //topologyConfig.put(Config.NIMBUS_HOST, args[0]);

        try {
            StormSubmitter.submitTopology(WordCountTopology.class.getSimpleName(), topologyConfig, topology.getBuilder(5, 2, 2).createTopology());
        } catch (AlreadyAliveException e) {
            logger.error("启动失败：{}", e.getMessage());
            e.printStackTrace();
        } catch (InvalidTopologyException e) {
            logger.error("启动失败：{}" , e.getMessage());
            e.printStackTrace();
        }

    }

}
