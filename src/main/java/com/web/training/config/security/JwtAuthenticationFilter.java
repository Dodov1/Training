package com.web.training.config.security;

import com.google.gson.Gson;
import com.web.training.exceptions.ExceptionErrorModel;
import com.web.training.models.security.LoginUserSecurityModel;
import com.web.training.models.security.UserDetailsModified;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.web.training.config.appConstants.AppConstants.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/login");
    }


    @SneakyThrows
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain, Authentication authentication) {
        UserDetailsModified user = (UserDetailsModified) authentication.getPrincipal();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        if (!user.isVerified()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ExceptionErrorModel apiError = new ExceptionErrorModel(FORBIDDEN);
            apiError.setMessage(USER_NOT_VERIFIED);
            response.getWriter().println(new Gson().toJson(apiError));
            return;
        }
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        String token = Jwts.builder()
                .signWith(signatureAlgorithm, signingKey)
                .setHeaderParam("typ", "jwt")
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        response.addHeader("userId", user.getId().toString());
        response.addHeader("userRoles", String.valueOf(user.getAuthorities()));

        LoginUserSecurityModel loginUserSecurityModel = new LoginUserSecurityModel();
        loginUserSecurityModel.setAuthorities(user.getAuthorities());
        loginUserSecurityModel.setUserId(user.getId());
        if (user.getTrainer() != null) {
            response.addHeader("trainerId", user.getTrainer().getId().toString());
            loginUserSecurityModel.setTrainerId(user.getTrainer().getId());
        }
        response.getWriter().println(new Gson().toJson(loginUserSecurityModel));
        response.getWriter().flush();
        response.getWriter().close();
    }


    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String authTokenHeader = request.getHeader("Authorization");
        String token = authTokenHeader.split(" ")[1];

        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decodedToken = new String(decodedBytes);

        String username = decodedToken.split(":")[0];
        String password = decodedToken.split(":")[1];
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
