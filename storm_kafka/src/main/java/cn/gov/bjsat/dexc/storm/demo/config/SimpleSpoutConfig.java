package cn.gov.bjsat.dexc.storm.demo.config;

import backtype.storm.spout.SchemeAsMultiScheme;
import storm.kafka.BrokerHosts;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;

import java.util.List;

/**
 * Created by grf11_000 on 2015/11/30.
 */
public class SimpleSpoutConfig extends SpoutConfig {
    public SimpleSpoutConfig(BrokerHosts hosts, String topic, String zkRoot, String id) {
        super(hosts, topic, zkRoot, id);
        this.scheme = new SchemeAsMultiScheme(new StringScheme());
        this.forceFromStart = false;
        this.zkPort = 2181;
    }

    public void setZkServers(List<String> servers){
        this.zkServers = servers;
    }
}
