package lord.dev.security.jwt;

import io.jsonwebtoken.*;;
import lord.dev.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JWTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JWTokenProvider.class);

    @Value("${jwt.secret.key.access}")
    private  String  JWT_ACCESS_SECRET;

    @Value("${jwt.secret.key.refresh}")
    private String JWT_REFRESH_SECRET;

    @Value("${jwt.secret.expiration.access}")
    private int ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.secret.expiration.refresh}")
    private long REFRESH_TOKEN_EXPIRATION;

    public String[] generateJwtTokens(User userPrincipal) {
        return new String[]{
                generateAccessToken(userPrincipal),
                generateRefreshToken(userPrincipal)
        };
    }

    public String generateAccessToken(User user) {
        String subj = user.getEmail() == null ? user.getUsername() : user.getEmail();
        return "Bearer " +  Jwts.builder()
                .setSubject(subj).claim("roles",user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JWT_ACCESS_SECRET)
                .compact();
    }

    public String generateRefreshToken(User user){
        String subj = user.getEmail() == null ? user.getUsername() : user.getEmail();
        return "Bearer " + Jwts.builder()
                .setSubject(subj).claim("roles",user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JWT_REFRESH_SECRET)
                .compact();
    }

    public Jws<Claims> validateJwtAccessToken(String authToken) {
        try {
            return Jwts.parser().setSigningKey(JWT_ACCESS_SECRET).parseClaimsJws(authToken);
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return null;
    }

    public Jws<Claims> validateJwtRefreshToken(String authToken) {
        try {
            return Jwts.parser().setSigningKey(JWT_REFRESH_SECRET).parseClaimsJws(authToken);
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return null;
    }


}
