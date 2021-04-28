package it.cnr.ilc.lexo.service.helper.external;

import it.cnr.ilc.lexo.service.data.FilterData.Filter;
import it.cnr.ilc.lexo.service.data.FilterData.FilterField;
import it.cnr.ilc.lexo.service.data.FilterData.FilterId;
import it.cnr.ilc.lexo.service.data.FilterData.FilterList;
import java.util.Collection;

/**
 *
 * @author andreabellandi
 */
public abstract class ExternalFilter {

    public abstract String getField();

    public abstract Collection<Long> getValues(FilterField filterField);

    public Filter process(Filter filter) {
        if (filter instanceof FilterField) {
            if (((FilterField) filter).getField().equals(getField())) {
                FilterId filterId = new FilterId();
                filterId.getValues().addAll(getValues((FilterField) filter));
                return filterId;
            } else {
                FilterField filterField = new FilterField();
                filterField.setField(((FilterField) filter).getField());
                filterField.setValue(((FilterField) filter).getValue());
                filterField.setMatch(((FilterField) filter).getMatch());
                filterField.getMatchOptions().addAll(((FilterField) filter).getMatchOptions());
                return filterField;
            }
        } else if (filter instanceof FilterList) {
            FilterList newFilterList = new FilterList();
            newFilterList.setOperator(((FilterList) filter).getOperator());
            ((FilterList) filter).getFilters().stream().map(f -> process(f)).forEach(f -> newFilterList.getFilters().add(f));
            return newFilterList;
        } else if (filter instanceof FilterId) {
            FilterId filterId = new FilterId();
            filterId.getValues().addAll(((FilterId) filter).getValues());
            return filterId;
        } else {
            return null;
        }
    }

}
