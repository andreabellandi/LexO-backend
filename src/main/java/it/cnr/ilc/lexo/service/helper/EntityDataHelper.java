package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.hibernate.entity.SuperEntity;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.FilterData;
import it.cnr.ilc.lexo.service.data.FilterData.Filter;
import it.cnr.ilc.lexo.service.helper.external.ExternalFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author andreabellandi
 * @param <D>
 * @param <E>
 */
public abstract class EntityDataHelper<D extends Data, E extends SuperEntity> extends Helper<D> {

    public abstract void fillData(D data, E entity);

    public abstract void fillData(D data, Map<String, Object> record);

    public abstract void fillEntity(E entity, D data) throws HelperException, ManagerException;

    protected boolean errorOnChekFieldNotFound = true;

    protected Comparator<D> getDefaultComparator() {
        return (d1, d2) -> 0;
    }

    public D newData(E entity) {
        try {
            D data = getDataClass().getDeclaredConstructor().newInstance();
            fillData(data, entity);
            return data;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    public D newData(Map<String, Object> record) {
        try {
            D data = getDataClass().getDeclaredConstructor().newInstance();
            fillData(data, record);
            return data;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<D> newDataList(List<E> entities) {
        return entities.stream()
                .map(e -> newData(e))
                .collect(Collectors.toList());
    }

    public List<D> newDataList(List<E> entities, Comparator<? super D> comparator) {
        return entities.stream()
                .map(e -> newData(e))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<D> newDataList(List<E> entities, FilterData<D> filterData) throws HelperException {
        try {
            return entities.stream()
                    .map(e -> newData(e))
                    .filter(FilterHelper.getPredicate(getProcessedFilter(filterData.getFilter())))
                    .sorted(FilterHelper.getComparator(filterData.getOrderBy()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new HelperException(e.getMessage());
        }
    }

    public List<D> newDataListSql(List<Map<String, Object>> records) throws HelperException {
        try {
            return records.stream()
                    .map(r -> newData(r))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new HelperException(e.getMessage());
        }
    }

    public List<D> newDataListSql(List<Map<String, Object>> records, Comparator<? super D> comparator) throws HelperException {
        try {
            return records.stream()
                    .map(r -> newData(r))
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new HelperException(e.getMessage());
        }
    }

    public List<D> newDataListSql(List<Map<String, Object>> records, FilterData<D> filterData) throws HelperException {
        try {
            return records.stream()
                    .map(r -> newData(r))
                    .filter(FilterHelper.getPredicate(getProcessedFilter(filterData.getFilter())))
                    .sorted(FilterHelper.getComparator(filterData.getOrderBy()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new HelperException(e.getMessage());
        }
    }

    protected final List<ExternalFilter> externFilters = new ArrayList<>();

    private Filter getProcessedFilter(Filter filter) {
        filter = preProcessFilter(filter);
        for (ExternalFilter externalFilter : externFilters) {
            filter = externalFilter.process(filter);
        }
        return filter;
    }

    protected Filter preProcessFilter(Filter filter) {
        return filter;
    }

}
