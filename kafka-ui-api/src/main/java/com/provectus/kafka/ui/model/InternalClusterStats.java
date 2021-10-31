package com.provectus.kafka.ui.model;

import com.provectus.kafka.ui.service.MetricsCache;
import com.provectus.kafka.ui.util.ClusterUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class InternalClusterStats {
  private String name;
  private ServerStatusDTO status;
  private Integer topicCount;
  private Integer brokerCount;
  private Integer zooKeeperStatus;
  private Integer activeControllers;
  private Integer onlinePartitionCount;
  private Integer offlinePartitionCount;
  private Integer inSyncReplicasCount;
  private Integer outOfSyncReplicasCount;
  private Integer underReplicatedPartitionCount;
  private List<BrokerDiskUsageDTO> diskUsage;
  private String version;
  private List<Feature> features;
  private BigDecimal bytesInPerSec;
  private BigDecimal bytesOutPerSec;

  public InternalClusterStats(KafkaCluster cluster, MetricsCache.Metrics metrics) {
    name = cluster.getName();
    status = metrics.getStatus();
    topicCount = metrics.getTopicDescriptions().size();
    brokerCount = metrics.getClusterDescription().getNodes().size();
    zooKeeperStatus = ClusterUtil.convertToIntServerStatus(metrics.getZkStatus().getStatus());
    activeControllers = metrics.getClusterDescription().getController() != null ? 1 : 0;
    version = metrics.getVersion();

    if (metrics.getLogDirInfo() != null) {
      diskUsage = metrics.getLogDirInfo().getBrokerStats().entrySet().stream()
          .map(e -> new BrokerDiskUsageDTO()
              .brokerId(e.getKey())
              .segmentSize(e.getValue().getSegmentSize())
              .segmentCount(e.getValue().getSegmentsCount()))
          .collect(Collectors.toList());
    }

    features = metrics.getFeatures();

    bytesInPerSec = metrics.getJmxMetrics().getBytesInPerSec().values().stream()
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    bytesOutPerSec = metrics.getJmxMetrics().getBytesOutPerSec().values().stream()
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    var partitionsStats = new PartitionsStats(metrics.getTopicDescriptions().values());
    onlinePartitionCount = partitionsStats.getOnlinePartitionCount();
    offlinePartitionCount = partitionsStats.getOfflinePartitionCount();
    inSyncReplicasCount = partitionsStats.getInSyncReplicasCount();
    outOfSyncReplicasCount = partitionsStats.getOutOfSyncReplicasCount();
    underReplicatedPartitionCount = partitionsStats.getUnderReplicatedPartitionCount();
  }

}