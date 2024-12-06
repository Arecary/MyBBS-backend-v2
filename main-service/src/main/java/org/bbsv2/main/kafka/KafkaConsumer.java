package org.bbsv2.main.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bbsv2.main.service.BlogService;
import org.bbsv2.main.service.CommentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.annotation.Resource;

@Service
public class KafkaConsumer {


  @Resource
  private BlogService blogService;

  @Resource
  private CommentService commentService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(topics = "user-delete-topic", groupId = "main-service-group")
  public void handleUserDeleteMessage(String message) {
    try {
      // 将消息转换为 Map
      Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);

      // 获取 userId
      Integer userId = (Integer) messageMap.get("userId");

      if (userId != null) {
        System.out.println("Processing deletion for userId: " + userId);

        // 删除用户的博客及其博客下的所有评论
        blogService.deleteBlogsAndCommentsByUserId(userId);

        // 删除用户的评论
        commentService.deleteByUserId(userId);

        System.out.println("Successfully deleted all blogs, comments, and associated data for userId: " + userId);
      } else {
        System.err.println("Invalid user-delete message: userId is null.");
      }
    } catch (Exception e) {
      System.err.println("Error processing Kafka message: " + e.getMessage());
    }
  }

}
