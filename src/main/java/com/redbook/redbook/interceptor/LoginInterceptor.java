package com.redbook.redbook.interceptor;

import com.redbook.redbook.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"msg\":\"未登录\"}");
            writer.flush();
            return false;
        }

        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"msg\":\"登录已过期\"}");
            writer.flush();
            return false;
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        return true;
    }
}
