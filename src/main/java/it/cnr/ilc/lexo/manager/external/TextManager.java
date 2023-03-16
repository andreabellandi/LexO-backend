/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager.external;

import it.cnr.ilc.lexo.manager.Cached;
import it.cnr.ilc.lexo.manager.Manager;
import javax.ws.rs.client.Client;

/**
 *
 * @author andreabellandi
 */
public class TextManager implements Manager, Cached {

//    private final Client client;
//    private String key;
    
    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
//
//    public TalmudSuggestionSource() {
//        try {
//            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                }
//            }};
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, trustAllCerts, new SecureRandom());
//            client = ClientBuilder.newBuilder().sslContext(sslContext).build();
//        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    @Override
//    public String getName() {
//        return "Talmud";
//    }
//
//    @Override
//    public List<Map<String, Object>> getSuggestions(Translation translation, Map<String, String> parameters) throws ManagerException {
//        parameters.put("sentence", translation.getOriginal());
//        List<SuggestionData> suggestions = getSuggestions(parameters);
//        List<Map<String, Object>> records = new ArrayList<>();
//        Map<String, Object> record;
//        for (SuggestionData suggestion : suggestions) {
//            record = new HashMap<>();
//            record.put("id", suggestion.getId());
//            record.put("index", suggestion.getIndex());
//            record.put("hash", suggestion.getHash());
//            record.put("original", suggestion.getOriginal());
//            record.put("translation", suggestion.getTranslation());
//            record.put("page", suggestion.getPage());
//            record.put("paragraph", suggestion.getParagraph());
//            record.put("username", suggestion.getUsername());
//            record.put("account_type", suggestion.getAccountType());
//            record.put("locked", suggestion.getLocked());
//            record.put("time", suggestion.getTime());
//            record.put("rank", suggestion.getRank());
//            record.put("document", suggestion.getDocument());
//            record.put("source", getName());
//            records.add(record);
//        }
//        return records;
//    }
//
//    private synchronized WebTarget connect() throws ManagerException {
//        WebTarget webTarget = client.target(TalmudProperties.getProperty("talmudSuggestionSource.url"));
//        if (key == null) {
//            String username = TalmudProperties.getProperty("talmudSuggestionSource.username");
//            String password = TalmudProperties.getProperty("talmudSuggestionSource.password");
//            AuthenticationData authenticationData = new AuthenticationData();
//            authenticationData.setUsername(username);
//            authenticationData.setPassword(password);
//
//            Response response = webTarget
//                    .path("service/authentication/authenticate")
//                    .queryParam("onlyKey", "true")
//                    .request()
//                    .post(Entity.json(authenticationData));
//            if (response.getStatus() != Status.OK.getStatusCode()) {
//                throw new ManagerException("retrieve external suggestions error: " + response.getStatusInfo().getReasonPhrase());
//            }
//            key = response.readEntity(String.class);
//        }
//        return webTarget;
//    }
//
//    private List<SuggestionData> getSuggestions(Map<String, String> parameters) throws ManagerException {
//        WebTarget webTarget = connect();
//        ObjectMapper objectMapper = Helper.getObjectMapper();
//        try {
//            FilterData<SuggestionData> filterData = new FilterData<>();
//            filterData.setParameters(parameters);
//            filterData.setReload(Boolean.TRUE);
//            String json = objectMapper.writeValueAsString(filterData);
//            Response response = webTarget
//                    .path("service/suggestion/suggestions")
//                    .queryParam("key", key)
//                    .request()
//                    .post(Entity.entity(json, MediaType.TEXT_PLAIN));
//            if (response.getStatus() == Status.UNAUTHORIZED.getStatusCode()) {
//                key = null;
//                return getSuggestions(parameters);
//            }
//            if (response.getStatus() != Status.OK.getStatusCode()) {
//                throw new ManagerException("retrieve external suggestions error: " + response.getStatusInfo().getReasonPhrase());
//            }
//            json = response.readEntity(String.class);
//            filterData = objectMapper.readValue(json, new TypeReference<FilterData<SuggestionData>>() {
//            });
//            return filterData.getData();
//        } catch (JsonProcessingException ex) {
//            throw new ManagerException("retrieve external suggestions error: parse response error");
//        }
//    }


    
}
