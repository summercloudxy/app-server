package com.zgiot.app.server.module.auth.controller.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class MyJwtTokenUtil {

    public static final String KEY_USER_UUID = "uid";
    public static final String KEY_USER_LOGINNAME = "lgn";

    @Value("${auth.jwt.secret}")
    private String secret;

    @Value("${auth.jwt.expiration}")
    private Long expiration;

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public int getClientFromToken(String token) {
        return Integer.parseInt(getClaimFromToken(token, Claims::getAudience));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String generateToken(JwtAuthBean obj) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(KEY_USER_LOGINNAME, obj.getLoginName());
        Date createdDate = new Date();
        Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(obj.getUserUuid())
                .setAudience(String.valueOf(obj.getClientId()))
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public JwtAuthBean getJwtObjFromToken(String token) {
        Claims cs = getAllClaimsFromToken(token);
        JwtAuthBean obj = new JwtAuthBean();
        obj.setClientId(Integer.parseInt(cs.getAudience()));
        obj.setLoginName((String) cs.get(KEY_USER_LOGINNAME));
        obj.setUserUuid(cs.getSubject());
        return obj;
    }

    public Boolean canTokenBeRefreshed(String token) {
        return !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        Date createdDate = new Date();
        Date expirationDate = calculateExpirationDate(createdDate);

        Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean validateToken(String token) {
        return (!isTokenExpired(token));
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
}
