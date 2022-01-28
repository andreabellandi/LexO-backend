/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.zotero;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.manager.Cached;
import it.cnr.ilc.lexo.manager.Manager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author andreabellandi
 */
public class ZoteroClient implements Manager, Cached {

    private final String baseUrl = LexOProperties.getProperty("zotero.service.url");
    private final String libray = LexOProperties.getProperty("zotero.service.library");
    private final String prefix = LexOProperties.getProperty("zotero.service.requestPrefix");
    private final String version = LexOProperties.getProperty("zotero.service.version");

    private URL url;
    private HttpURLConnection conn;

    public URL getUrl() {
        return url;
    }

    public void setUrl(String itemKey) {
        try {
            this.url = new URL(baseUrl + "/" + prefix + "/" + libray + "/items/" + itemKey + "?v=" + version);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ZoteroClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HttpURLConnection getConn() {
        return conn;
    }

    public void setConn() {
        try {
            this.conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
        } catch (IOException ex) {
            Logger.getLogger(ZoteroClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        conn.disconnect();
    }

    public ZoteroClient() {
    }

    public JsonNode getItem() throws RuntimeException {
        JsonNode rootNode = null;
        try {
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            ObjectMapper mapper = new ObjectMapper();
            rootNode = mapper.readTree(br.lines().collect(Collectors.joining()));
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return rootNode;
    }

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
