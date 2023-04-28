package it.cnr.ilc.lexo.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.AuthenticationData;
import it.cnr.ilc.lexo.service.zotero.ZoteroClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author andreabellandi
 */
public class UserManager implements Manager, Cached {

    private final static String KEYCLOACK_URL = LexOProperties.getProperty("keycloack.url");
    private String publicKey = null;
    private HttpURLConnection conn;
    private URL url;

    private void setConn() {
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
        } catch (IOException ex) {
        }
    }

    private void setUrl() {
        try {
            url = new URL(KEYCLOACK_URL);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ZoteroClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    UserManager() {
            if (publicKey == null) {
                try {
                    JsonNode rootNode = null;
                    setUrl();
                    setConn();
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    ObjectMapper mapper = new ObjectMapper();
                    rootNode = mapper.readTree(br.lines().collect(Collectors.joining()));
                    publicKey = rootNode.get("public_key").textValue();
                } catch (IOException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public AuthenticationData authorize(String token) throws Exception {
        AuthenticationData ad = new AuthenticationData();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pk = kf.generatePublic(keySpec);
        Jws<Claims> jwt = null;
        try {
            jwt = Jwts.parserBuilder()
                    .setSigningKey(pk)
                    .build()
                    .parseClaimsJws(token);
            Claims cl = jwt.getBody();
            ad.setUsername(cl.get("preferred_username").toString());
            ad.setType(((Map) ((Map) cl.get("resource_access")).get(LexOProperties.getProperty("keycloack.client"))).get("roles").toString());
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException e) {
            // if you get error, that means token is invalid. 
            throw new Exception(e);
        }
        return ad;
    }

}
