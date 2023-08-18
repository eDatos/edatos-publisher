package es.gobcan.istac.edatos.publisher.health;

import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.Node;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.config.KafkaInitializeTopics;

@Component
public class KafkaHealthIndicator extends AbstractHealthIndicator {
    private final KafkaInitializeTopics kafkaInitializeTopics;

    public KafkaHealthIndicator(KafkaInitializeTopics kafkaInitializeTopics) {
        this.kafkaInitializeTopics = kafkaInitializeTopics;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        Properties kafkaProperties = kafkaInitializeTopics.getKafkaProperties();
        try (AdminClient adminClient = AdminClient.create(kafkaProperties)) {
            DescribeClusterResult result = adminClient.describeCluster(new DescribeClusterOptions().timeoutMs(3000));
            Node node = result.controller().get();
            builder.withDetail("clusterId", result.clusterId().get())
                   .withDetail("controller.idString", node.idString())
                   .withDetail("controller.host", node.host())
                   .withDetail("controller.port", node.port());
            builder.up();
        } catch (Exception e) {
            builder.withException(e).down();
        }
    }
}
