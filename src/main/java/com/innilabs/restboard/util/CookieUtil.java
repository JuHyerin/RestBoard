package com.innilabs.restboard.util;

import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.SerializationUtils;

public class CookieUtil {
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(StringUtil.equal(cookie.getName(), name)){
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/"); //Cookie.path("쿠키를 물고 갈 url")
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals(name)){
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    //Cookie -> String
    public static String serialize(Object object){ 
        return Base64.getUrlEncoder()
                    .encodeToString(SerializationUtils.serialize(object));
    }


    //Cookie(oauth2_auth_request) -> Class
    public static <T> T deserialize(Cookie cookie, Class<T> typeClass){
        return typeClass.cast(SerializationUtils.deserialize( 
                                Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    
}   