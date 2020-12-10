package com.innilabs.restboard.auth;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.exception.BoardException;
import com.innilabs.restboard.util.CookieUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final JwtProvider tokenProvider;

    // @Value("${app.oauth2.authorizedRedirectUris}")
    List<String> authorizedRedirectUris;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 쿠키에 담긴 redirect_uri가 검증된 uri인지 확인 후 token 담아서 uri 반환
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        Optional<String> redirectUri = CookieUtil
                .getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            try {
                throw new BoardException("검증되지 않은 Redirect URI 인증 절차 진행 불가.");
            } catch (BoardException e) {
                logger.error("OAuth2SuccessHandler.determineTargetUrl {}:", e);
            }
        }
        //Authentication my = SecurityContextHolder.getContext().getAuthentication();

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl()); // 없으면 /반환
        // String token = tokenProvider.createOAuth2Token(authentication);
        // String token =
        // tokenProvider.createToken((Account)my.getPrincipal());//createToken의 파라미터를
        // Map으로 바꾸기.
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> accountMap = mapper.convertValue(authentication.getPrincipal(), Map.class); 
        String token = tokenProvider.createOAuth2Token(accountMap);

        //response.setHeader("Bearer ", token);
        return UriComponentsBuilder.fromUriString(targetUrl)
                                    .queryParam("token", token)
                                    .build()
                                    .toUriString();
    }

    //SPRING_SECURITY_LAST_EXCEPTION 세션 삭제
    //redirect_uri, oauth2_auth_request 쿠키 삭제
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    //미리 정의해둔 uri와 일치하는지 여부
    private boolean isAuthorizedRedirectUri(String uri){ 
        URI clientRedirectUri = URI.create(uri);
        this.authorizedRedirectUris.add("http://localhost:8080/login/oauth2/code/google");
        return this.authorizedRedirectUris
                    .stream() 
                    .anyMatch(authorizedRedirectUri->{ //anyMatch : 최소한 한 개의 요소가 주어진 조건에 만족하는지 조사
                        URI authorizedURI = URI.create(authorizedRedirectUri);
                        if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                                && authorizedURI.getPort() == clientRedirectUri.getPort()){
                            return true;        
                        }
                        return false;
                    });
    }
}