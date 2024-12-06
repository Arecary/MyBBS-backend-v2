package org.bbsv2.account.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KafkaTopicConfig {

  @Bean
  public NewTopic userDeleteTopic() {
    return new NewTopic("user-delete-topic", 3, (short) 1); // 3 分区，1 副本
  }
}
