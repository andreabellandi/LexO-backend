package it.cnr.ilc.lexo.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cnr.ilc.lexo.hibernate.entity.Account;
import it.cnr.ilc.lexo.service.data.AuthenticationData;
import it.cnr.ilc.lexo.service.data.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author andreabellandi
 */
public class AuthenticationHelper extends Helper<AuthenticationData> {

    @Override
    public Class<AuthenticationData> getDataClass() {
        return AuthenticationData.class;
    }

    public void fillData(AuthenticationData data, Account entity, HttpServletRequest request) {
        data.setId(entity.getId());
        data.setType(entity.getType().getName());
        data.setUsername(entity.getUsername());
        data.setPassword(entity.getPassword());
        data.setName(entity.getName());
        data.setEmail(entity.getEmail());
        data.setEnabled(entity.getEnabled());
        data.setSettings(new HashMap(entity.getSettings()));
        data.setUserAgent(request.getHeader("user-agent"));
        data.setRemoteAddress(request.getRemoteAddr());
    }

    public List<Session> createSessionData(List<AuthenticationData> authenticationDatas) {
        Session sessionData;
        List<Session> sessionDatas = new ArrayList<>(authenticationDatas.size());
        for (AuthenticationData authenticationData : authenticationDatas) {
            sessionData = new Session();
            sessionData.setId(authenticationData.getId());
            sessionData.setUsername(authenticationData.getUsername());
            sessionData.setKey(authenticationData.getKey());
            sessionData.setRemoteAddress(authenticationData.getRemoteAddress());
            sessionData.setUserAgent(authenticationData.getUserAgent());
            sessionData.setLoginTime(authenticationData.getLoginTime());
            sessionData.setLastActionTime(authenticationData.getLastActionTime());
            sessionDatas.add(sessionData);
        }
        return sessionDatas;
    }

    public String sessionsToJson(List<Session> sessionDatas) {
        try {
            return objectMapper.writeValueAsString(sessionDatas);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
