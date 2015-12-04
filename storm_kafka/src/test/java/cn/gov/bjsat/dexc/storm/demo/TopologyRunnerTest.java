package cn.gov.bjsat.dexc.storm.demo;


import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.utils.Utils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by grf11_000 on 2015/12/1.
 */
public class TopologyRunnerTest {



    /**
     * 本机测试
     */
    @Test
    public void topologyTest(){
        Logger logger = LoggerFactory.getLogger("TopologyRunnerTest");

        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-stormKafka.xml");
        WordCountTopology topology = (WordCountTopology) applicationContext.getBean("wordCountTopology");

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology(WordCountTopology.class.getSimpleName(), topology.getTopologyConfig(), topology.getBuilder(5, 2, 2).createTopology());
        Utils.sleep(600000);
        localCluster.shutdown();
    }




    /**
     * 群集测试
     */
    @Test
    public void topologyClusterTest(){
        Logger logger = LoggerFactory.getLogger("TopologyRunnerTest");

        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-stormKafka.xml");
        WordCountTopology topology = (WordCountTopology) applicationContext.getBean("wordCountTopology");
        Config topologyConfig = topology.getTopologyConfig();
        topologyConfig.put(Config.NIMBUS_HOST, "storm1");

        try {
            StormSubmitter.submitTopology(WordCountTopology.class.getSimpleName(), topologyConfig, topology.getBuilder(5, 2, 2).createTopology());
        } catch (AlreadyAliveException e) {
            logger.error("启动失败：{}", e.getMessage());
            e.printStackTrace();
        } catch (InvalidTopologyException e) {
            logger.error("启动失败：{}" , e.getMessage());
            e.printStackTrace();
        }

        Utils.sleep(600000);

    }

}