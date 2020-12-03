package com.innilabs.restboard.auth;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innilabs.restboard.entity.Account;
import com.innilabs.restboard.util.StringUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER_STRING = "Authorization";

    public static final String CLAIM_EXPIRATION = "exp";
    public static final String CLAIM_ISSUED_AT = "iat";
    public static final String CLAIM_USER_INFO = "user";

    @Value("${app.jwt.secret}")
    String secret;
    @Value("${app.jwt.expire}") //1000L * 60 * 60 
    long tokenExpire;

    JWSSigner signer;
    JWSVerifier verifier;

    @PostConstruct // 의존성 주입 되자마자 실행됨, 따로 호출 ㄴㄴ
    public void init() throws JOSEException {
        signer = new MACSigner(secret);
        verifier = new MACVerifier(secret);
        tokenExpire *= 1000;
    }

    // HttpHeader로부터 Token 추출
    public String resolveJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_STRING);
        if (!StringUtil.isEmpty(authHeader)) {
            if (authHeader.startsWith(JWT_TOKEN_PREFIX)) {
                authHeader = authHeader.substring(JWT_TOKEN_PREFIX.length());
            }
            return authHeader;
        }
        return null;
    }

    // exp, iat로 유효성 검사
    public boolean validateToken(JWTClaimsSet jwtclaims) {
        /* JWTClaimsSet jwtclaims = parseTokenToCliams(token); // Token -> SignedJWT -> JWTClaimSet */

        Date now = new Date();// 현재시간
        if (jwtclaims.getExpirationTime().before(now)) {
            throw new JwtException("token expire error");
            /* log.error("토큰 유효기간 만료");
            return false; */
        }
        if (jwtclaims.getIssueTime().after(now)) {
            throw new JwtException("message issue at error");
            /* log.error("토큰 발행시각 오류");
            return false; */
        }
        return true;
    }

    // Token을 SignedJWT로 바꾼 후 verify 성공하면 JWTClaimSet 반환
    public JWTClaimsSet parseTokenToCliams(String token) {
        SignedJWT jwt;
        try {
            jwt = SignedJWT.parse(token);
            if (jwt.verify(verifier) == false) {
                throw new JwtException("verify failed");
            }
        } catch (ParseException e) {
            throw new JwtException("token parse error", e);
        } catch (JOSEException e) {
            throw new JwtException("verifier error", e);
        }

        try {
            return jwt.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new JwtException("claimsSet parse error", e);
        }
    }

    // JWTCliamSet에서 사용자 정보 추출하여 토큰 생성
    public Authentication getAuthentication(String token) {
        JWTClaimsSet claims = parseTokenToCliams(token);

        JwtDto jwtDto = null;
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if(validateToken(claims)){
            Map<String, Object> claimsMap = claims.getClaims();
            JSONObject jsonClaim = (JSONObject) claimsMap.get("user");
            ObjectMapper m = new ObjectMapper();
            try { 
                jwtDto = m.readValue(jsonClaim.toJSONString(), JwtDto.class); 
            } catch (IOException e) { 
                log.error("JSON -> JwtDto 오류", e); 
            } 
            jwtDto.getRoles().forEach(e->{authorities.add(new SimpleGrantedAuthority(e));} );
        }
        return new UsernamePasswordAuthenticationToken(jwtDto, null, authorities);
    }

    // Token의 ClaimSet 생성 (Map->JsonObject)
    public String createToken(Account account) {
        Map<String, Object> claims = new HashMap<String, Object>();
        Date now = new Date();
        Date exp = new Date(now.getTime() + this.tokenExpire);
        claims.put(CLAIM_ISSUED_AT, now.getTime() / 1000);
        claims.put(CLAIM_EXPIRATION, exp.getTime() / 1000);

        JwtDto jwtDto = new JwtDto();
        jwtDto.setUsername(account.getUsername());
        jwtDto.setName(account.getName());
        jwtDto.setEmail(account.getEmail());
        jwtDto.setRoles(account.getRoles());
        claims.put("user", jwtDto);

        return buildJwtToken(new JSONObject(claims));
    }

    // Token 생성(JsonObject -> JWTClaimSet)
    public String buildJwtToken(JSONObject jsonObject) throws JwtException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)   
                                        .type(JOSEObjectType.JWT)
                                        .build();
        SignedJWT jwt;
        try {
            JWTClaimsSet claims = JWTClaimsSet.parse(jsonObject);
            jwt = new SignedJWT(header, claims);
            jwt.sign(this.signer);
            return jwt.serialize();
        } catch (ParseException e) {
            throw new JwtException("claimsSet parse error", e);
        } catch (JOSEException e) {
            throw new JwtException("sign error", e);
        }
    }

}