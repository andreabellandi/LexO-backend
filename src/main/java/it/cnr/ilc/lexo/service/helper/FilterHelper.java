package it.cnr.ilc.lexo.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.FilterData;
import it.cnr.ilc.lexo.service.data.FilterData.Direction;
import it.cnr.ilc.lexo.service.data.FilterData.Filter;
import it.cnr.ilc.lexo.service.data.FilterData.FilterField;
import it.cnr.ilc.lexo.service.data.FilterData.FilterId;
import it.cnr.ilc.lexo.service.data.FilterData.FilterList;
import it.cnr.ilc.lexo.service.data.FilterData.Match;
import it.cnr.ilc.lexo.service.data.FilterData.MatchOption;
import it.cnr.ilc.lexo.service.data.FilterData.Operator;
import it.cnr.ilc.lexo.service.data.FilterData.OrderBy;
import it.cnr.ilc.lexo.util.FilterUtil;
import it.cnr.ilc.lexo.util.ReflectionUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author andreabellandi
 */
public class FilterHelper extends Helper<FilterData> {

    @Override
    public Class<FilterData> getDataClass() {
        return FilterData.class;
    }

    public boolean isModified(FilterData<? extends Data> filterData, FilterData<? extends Data> cacheFilterData) {
        if (cacheFilterData == null
                || filterData.getFilter() == null
                || cacheFilterData.getFilter() == null
                || Boolean.TRUE.equals(filterData.getReload())) {
            return true;
        }
        try {
            String filterDataJson = objectMapper.writeValueAsString(filterData.getFilter());
            String chacheFilterDataJson = objectMapper.writeValueAsString(cacheFilterData.getFilter());
            return !filterDataJson.equals(chacheFilterDataJson);
        } catch (JsonProcessingException ex) {
            return true;
        }
    }

    public <D extends Data> FilterData<D> newRangedFilterData(FilterData<D> filterData) throws HelperException {
        int from = filterData.getFromRow() == null ? 0 : filterData.getFromRow();
        int to = filterData.getToRow() == null ? filterData.getData().size() : Math.min(filterData.getData().size(), filterData.getToRow());
        if ((from < 0 || to > filterData.getData().size() || from > to)) {
            throw new HelperException("out range: result size is " + filterData.getData().size());
        }
        FilterData<D> newFilterData = new FilterData<>();
        newFilterData.setFilter(filterData.getFilter());
        newFilterData.setOrderBy(filterData.getOrderBy());
        newFilterData.setReload(filterData.getReload());
        newFilterData.setTotalCount(filterData.getData().size());
        newFilterData.setFromRow(filterData.getFromRow());
        newFilterData.setToRow(filterData.getToRow());
        try {
            newFilterData.setData(filterData.getData().stream()
                    .skip(from)
                    .limit(to - from)
                    .collect(Collectors.toList()));
            return newFilterData;
        } catch (RuntimeException e) {
            throw new HelperException(e.getMessage());
        }
    }

    protected static Predicate<Data> getPredicate(Filter filter) {
        return getPredicate(filter, false);
    }

    protected static Predicate<Data> getPredicate(Filter filter, boolean skipFieldNotFound) {
        return d -> check(d, filter, skipFieldNotFound);
    }

    private static boolean check(Data data, Filter filter, boolean skipFieldNotFound) {
        if (filter == null) {
            return true;
        }
        if (filter instanceof FilterField) {
            FilterField filterField = (FilterField) filter;
            try {
                Object fieldValue = ReflectionUtil.getValue(data, filterField.getField());
                return match(fieldValue, filterField);
            } catch (ReflectiveOperationException ex) {
                if (!skipFieldNotFound) {
                    throw new RuntimeException("filter error: field not found " + filterField.getField());
                } else {
                    return false;
                }
            }
        } else if (filter instanceof FilterList) {
            FilterList filterList = (FilterList) filter;
            if (filterList.getOperator() == null) {
                throw new RuntimeException("filter error: operand missing");
            }
            if (filterList.getFilters() == null || filterList.getFilters().isEmpty()) {
                return true;
            }
            boolean result = Operator.AND.equals(filterList.getOperator());
            for (Filter subFilter : filterList.getFilters()) {
                if (Operator.AND.equals(filterList.getOperator())) {
                    result &= check(data, subFilter, skipFieldNotFound);
                } else if (Operator.OR.equals(filterList.getOperator())) {
                    result |= check(data, subFilter, skipFieldNotFound);
                } else {
                    throw new RuntimeException("filter error: invalid operand " + filterList.getOperator());
                }
            }
            return result;
        } else if (filter instanceof FilterId) {
            FilterId filterId = (FilterId) filter;
            try {
                Long idValue = (Long) ReflectionUtil.getValue(data, filterId.getField());
                return filterId.getValues().contains(idValue);
            } catch (ReflectiveOperationException | ClassCastException ex) {
                if (!skipFieldNotFound) {
                    throw new RuntimeException("filter error: field not found " + filterId.getField());
                } else {
                    return false;
                }
            }
        } else {
            throw new RuntimeException("filter error: invalid filter " + filter.getClass().getSimpleName());
        }
    }

    private static boolean match(Object fieldValue, FilterField filterField) {
        if (fieldValue == null) {
            return filterField.getValue() == null;
        } else if (filterField.getValue() == null) {
            return false;
        } else if (fieldValue instanceof String) {
            if (Match.EQUALS.equals(filterField.getMatch())) {
                return fieldValue.equals(filterField.getValue());
            } else if (Match.NOT_EQUALS.equals(filterField.getMatch())) {
                return !fieldValue.equals(filterField.getValue());
            } else if (Match.LIKE.equals(filterField.getMatch())) {
                return FilterUtil.match((String) fieldValue, filterField.getValue(),
                        filterField.getMatchOptions().contains(MatchOption.STARTS_WITH),
                        filterField.getMatchOptions().contains(MatchOption.ENDS_WITH),
                        filterField.getMatchOptions().contains(MatchOption.MATCH_CASE),
                        filterField.getMatchOptions().contains(MatchOption.WHOLE_WORD),
                        filterField.getMatchOptions().contains(MatchOption.WHOLE_STRING));
            } else if (Match.NOT_LIKE.equals(filterField.getMatch())) {
                return !FilterUtil.match((String) fieldValue, filterField.getValue(),
                        filterField.getMatchOptions().contains(MatchOption.STARTS_WITH),
                        filterField.getMatchOptions().contains(MatchOption.ENDS_WITH),
                        filterField.getMatchOptions().contains(MatchOption.MATCH_CASE),
                        filterField.getMatchOptions().contains(MatchOption.WHOLE_WORD),
                        filterField.getMatchOptions().contains(MatchOption.WHOLE_STRING));
            } else {
                throw new RuntimeException("filter error: invalid match " + filterField.getMatch());
            }
        } else if (fieldValue instanceof Boolean) {
            if (Match.EQUALS.equals(filterField.getMatch())) {
                return ((Boolean) fieldValue).equals(getBoolean(filterField.getValue()));
            } else if (Match.NOT_EQUALS.equals(filterField.getMatch())) {
                return !((Boolean) fieldValue).equals(getBoolean(filterField.getValue()));
            } else {
                throw new RuntimeException("filter error: invalid match " + filterField.getMatch());
            }
        } else if (fieldValue instanceof Number) {
            if (Match.EQUALS.equals(filterField.getMatch())) {
                return new BigDecimal(fieldValue.toString()).equals(getBigDecimal(filterField.getValue()));
            } else if (Match.NOT_EQUALS.equals(filterField.getMatch())) {
                return !new BigDecimal(fieldValue.toString()).equals(getBigDecimal(filterField.getValue()));
            } else if (Match.GREATER_THAN.equals(filterField.getMatch())) {
                return new BigDecimal(fieldValue.toString()).compareTo(getBigDecimal(filterField.getValue())) > 0;
            } else if (Match.GREATER_THAN_OR_EQUALS.equals(filterField.getMatch())) {
                return new BigDecimal(fieldValue.toString()).compareTo(getBigDecimal(filterField.getValue())) >= 0;
            } else if (Match.LESS_THAN.equals(filterField.getMatch())) {
                return new BigDecimal(fieldValue.toString()).compareTo(getBigDecimal(filterField.getValue())) < 0;
            } else if (Match.LESS_THAN_OR_EQUALS.equals(filterField.getMatch())) {
                return new BigDecimal(fieldValue.toString()).compareTo(getBigDecimal(filterField.getValue())) <= 0;
            } else if (Match.LIKE.equals(filterField.getMatch())) {
                return FilterUtil.match(filterField.getValue(), (Number) fieldValue);
            } else {
                throw new RuntimeException("filter error: invalid match " + filterField.getMatch());
            }
        } else {
            throw new RuntimeException("filter error: invalid data type " + fieldValue.getClass().getSimpleName());
        }
    }

    public static Boolean getBoolean(String value) {
        if (value.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        } else if (value.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        } else {
            throw new RuntimeException("filter error: invalid boolean " + value);
        }
    }

    public static BigDecimal getBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("filter error: invalid number " + value);
        }
    }

    protected static Comparator<Data> getComparator(List<OrderBy> orderBys) {
        return (Data data1, Data data2) -> {
            if (orderBys == null) {
                return 0;
            }
            Comparable value1, value2;
            for (OrderBy orderBy : orderBys) {
                try {
                    value1 = (Comparable) ReflectionUtil.getValue(data1, orderBy.getField());
                    value2 = (Comparable) ReflectionUtil.getValue(data2, orderBy.getField());
                    if (value1 == null && value2 != null) {
                        return orderBy.getDirection().equals(Direction.ASC) ? 1 : -1;
                    } else if (value1 != null && value2 == null) {
                        return orderBy.getDirection().equals(Direction.ASC) ? -1 : 1;
                    } else if (value1 != null && value2 != null) {
                        int compare = value1.compareTo(value2);
                        if (compare != 0) {
                            return orderBy.getDirection().equals(Direction.ASC) ? compare : -compare;
                        }
                    }
                } catch (ReflectiveOperationException ex) {
                    throw new RuntimeException("order error: field not found " + orderBy.getField());
                }
            }
            return 0;
        };
    }

    protected static List<FilterField> getFiltersByField(Filter filter, String field) {
        if (filter instanceof FilterField && ((FilterField) filter).getField().equals(field)) {
            return Arrays.asList((FilterField) filter);
        } else if (filter instanceof FilterList) {
            return ((FilterList) filter).getFilters().stream()
                    .flatMap(f -> getFiltersByField(f, field).stream())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    protected static FilterField getFirstFilterByField(Filter filter, String field) {
        if (filter instanceof FilterField && ((FilterField) filter).getField().equals(field)) {
            return (FilterField) filter;
        } else if (filter instanceof FilterList) {
            return ((FilterList) filter).getFilters().stream()
                    .map(f -> getFirstFilterByField(f, field))
                    .findFirst().orElse(null);
        } else {
            return null;
        }
    }

}
