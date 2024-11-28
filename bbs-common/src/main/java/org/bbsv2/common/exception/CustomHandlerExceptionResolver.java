package org.bbsv2.common.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(-1) // 设置更高优先级
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {

  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    // 日志记录
    ex.printStackTrace(); // 打印异常堆栈信息
    System.out.println("捕获的异常类型：" + ex.getClass().getName());
    System.out.println("返回的状态码：" + response.getStatus());

    response.setContentType("application/json;charset=UTF-8"); // 设置返回内容为 JSON
    try {
      // 强制返回 HTTP 状态码 200
      response.setStatus(HttpStatus.OK.value());

      if (ex instanceof CustomException) {
        CustomException customException = (CustomException) ex;
        response.getWriter().write(
                String.format("{\"code\":\"%s\",\"msg\":\"%s\"}", customException.getCode(), customException.getMsg())
        );
      } else {
        // 处理其他非自定义异常
        response.getWriter().write(
                String.format("{\"code\":\"500\",\"msg\":\"%s\"}", "Internal Server Error")
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new ModelAndView(); // 返回空视图，表示异常已处理
  }
}
