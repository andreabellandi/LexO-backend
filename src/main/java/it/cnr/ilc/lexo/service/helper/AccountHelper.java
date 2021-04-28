package it.cnr.ilc.lexo.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cnr.ilc.lexo.hibernate.entity.Account;
import it.cnr.ilc.lexo.manager.AccountManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.AccountData;
import it.cnr.ilc.lexo.service.data.PasswordData;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author oakgen
 */
public class AccountHelper extends EntityDataHelper<AccountData, Account> {

    private final AccountManager accountManager = ManagerFactory.getManager(AccountManager.class);

    @Override
    public Class<AccountData> getDataClass() {
        return AccountData.class;
    }

    @Override
    public void fillData(AccountData data, Account entity) {
        data.setId(entity.getId());
        data.setType(entity.getType().getName());
        data.setUsername(entity.getUsername());
        data.setPassword(entity.getPassword());
        data.setName(entity.getName());
        data.setEmail(entity.getEmail());
        data.setEnabled(entity.getEnabled());
        data.setSettings(new HashMap(entity.getSettings()));
    }

    @Override
    public void fillData(AccountData data, Map<String, Object> record) {
        data.setId(((Number) record.get("id")).longValue());
        data.setType((String) record.get("type"));
        data.setUsername((String) record.get("username"));
        data.setPassword((String) record.get("password"));
        data.setName((String) record.get("name"));
        data.setEmail((String) record.get("email"));
        data.setEnabled((Boolean) record.get("enabled"));
        data.setSettings(null);
    }

    @Override
    public void fillEntity(Account entity, AccountData data) throws HelperException {
    }

    public void fillAfterCreation(AccountData data, Account entity) {
        data.setId(entity.getId());
        data.setPassword(entity.getPassword());
        entity.setName(data.getName());
        entity.setEmail(data.getEmail());
        entity.setEnabled(data.getEnabled() == null ? Boolean.FALSE : data.getEnabled());
    }

    @Override
    public Comparator<AccountData> getDefaultComparator() {
        return (d1, d2) -> d1.getUsername().compareTo(d2.getUsername());

    }

    public PasswordData fromJsonPassword(String json) throws HelperException {
        try {
            return objectMapper.readValue(json, PasswordData.class);
        } catch (JsonProcessingException ex) {
            throw new HelperException("parsing error");
        }
    }

}
