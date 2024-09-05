package com.sparta.nbcampnewsfeed.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nbcampnewsfeed.ApiPayload.ApiResponse;
import com.sparta.nbcampnewsfeed.ApiPayload.Code.Status.ErrorStatus;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {
    private final JwtUtil jwtUtil;
    private final Pattern authPattern = Pattern.compile("^/auth.*");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        // `/auth`로 시작하는 URL은 필터를 통과하지 않도록 설정
        if (authPattern.matcher(url).matches()) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpRequest.getHeader("Authorization");

        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            // 토큰이 없는 경우 400을 반환합니다.
            jwtExceptionHandler(httpResponse, ErrorStatus._NOT_FOUND_TOKEN);
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(jwt);

            // 사용자 정보를 ArgumentResolver 로 넘기기 위해 HttpServletRequest 에 세팅
            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
            httpRequest.setAttribute("email", claims.get("email", String.class));

            chain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._UNAUTHORIZED_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._UNAUTHORIZED_EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._BAD_REQUEST_UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._BAD_REQUEST_ILLEGAL_TOKEN);
        } catch (Exception e) {
            log.error("JWT 토큰 검증 중 오류가 발생했습니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._UNAUTHORIZED_TOKEN);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public void jwtExceptionHandler(HttpServletResponse response, ErrorStatus errorStatus) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(Integer.parseInt(errorStatus.getCode()));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            ApiResponse<String> responseMessage = ApiResponse.onFailure(errorStatus);
            response.getWriter().write(mapper.writeValueAsString(responseMessage));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
