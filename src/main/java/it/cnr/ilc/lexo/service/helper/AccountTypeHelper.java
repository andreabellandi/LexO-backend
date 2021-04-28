package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.hibernate.entity.AccountType;
import it.cnr.ilc.lexo.service.data.AccountTypeData;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author oakgen
 */
public class AccountTypeHelper extends EntityDataHelper<AccountTypeData, AccountType> {

    @Override
    public Class<AccountTypeData> getDataClass() {
        return AccountTypeData.class;
    }

    @Override
    public void fillData(AccountTypeData data, AccountType entity) {
        data.setId(entity.getId());
        data.setName(entity.getName());
        data.setPosition(entity.getPosition());
        data.setColor(entity.getColor());
    }

    @Override
    public void fillData(AccountTypeData data, Map<String, Object> record) {
    }

    @Override
    public void fillEntity(AccountType entity, AccountTypeData data) {
    }

    @Override
    public Comparator<AccountTypeData> getDefaultComparator() {
        return (d1, d2) -> d1.getPosition().compareTo(d2.getPosition());
    }

}
