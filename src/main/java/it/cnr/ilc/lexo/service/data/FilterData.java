package it.cnr.ilc.lexo.service.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.cnr.ilc.lexo.service.deserializer.FilterDeserializer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author andreabellandi
 * @param <D>
 */
public class FilterData<D extends Data> implements Data {

    private Filter filter;
    private List<OrderBy> orderBy;
    private Boolean reload;
    private Integer fromRow;
    private Integer toRow;
    private Integer totalCount;
    private List<D> data;

    public FilterData() {
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<OrderBy> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<OrderBy> orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getReload() {
        return reload;
    }

    public void setReload(Boolean reload) {
        this.reload = reload;
    }

    public Integer getFromRow() {
        return fromRow;
    }

    public void setFromRow(Integer fromRow) {
        this.fromRow = fromRow;
    }

    public Integer getToRow() {
        return toRow;
    }

    public void setToRow(Integer toRow) {
        this.toRow = toRow;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<D> getData() {
        return data;
    }

    public void setData(List<D> data) {
        this.data = data;
    }

    public enum Operator {
        AND,
        OR
    }

    public enum Match {
        LIKE,
        NOT_LIKE,
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN,
        LESS_THAN_OR_EQUALS
    }

    public enum MatchOption {
        MATCH_CASE,
        WHOLE_WORD,
        STARTS_WITH,
        ENDS_WITH,
        WHOLE_STRING,
    }

    @JsonDeserialize(using = FilterDeserializer.class)
    public static interface Filter {
    }

    public static class FilterList implements Filter {

        private Operator operator;
        private final List<Filter> filters = new ArrayList<>();

        public Operator getOperator() {
            return operator;
        }

        public void setOperator(Operator operator) {
            this.operator = operator;
        }

        public List<Filter> getFilters() {
            return filters;
        }

    }

    public static class FilterField implements Filter {

        private String field;
        private String value;
        private Match match;
        private final List<MatchOption> matchOptions = new ArrayList<>();

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Match getMatch() {
            return match;
        }

        public void setMatch(Match match) {
            this.match = match;
        }

        public List<MatchOption> getMatchOptions() {
            return matchOptions;
        }

    }

    public static class FilterId implements Filter {

        private final HashSet<Long> values = new HashSet<>();

        public String getField() {
            return "id";
        }

        public HashSet<Long> getValues() {
            return values;
        }

    }

    public enum Direction {
        ASC,
        DESC;
    }

    public static class OrderBy {

        public String field;
        public Direction direction;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

    }

}
