package it.cnr.ilc.lexo.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.HitsDataList;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 *
 * @author andreabellandi
 * @param <D>
 */
public abstract class Helper<D extends Data> {

    private int totalHits;

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public static boolean parseBoolean(String content) throws HelperException {
        if ("true".equals(content.toLowerCase())) {
            return true;
        } else if ("false".equals(content.toLowerCase())) {
            return false;
        } else {
            throw new HelperException("parse error");
        }
    }

    protected final ObjectMapper objectMapper = new ObjectMapper();

    {
        String dateFormat = LexOProperties.getProperty("helper.jsonDateFormat", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        if (dateFormat != null && !dateFormat.isEmpty()) {
            objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        }
        if ("true".equals(LexOProperties.getProperty("helper.jsonPrettyPrint", "false"))) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    public abstract Class<D> getDataClass();

    public D fromJson(String json) throws HelperException {
        try {
            return objectMapper.readValue(json, getDataClass());
        } catch (JsonProcessingException ex) {
            throw new HelperException("parse error");
        }
    }

    public String toJson(D data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toJson(List<D> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toJson(HitsDataList<D> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toJson(Map<String, List<D>> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> List<T> listFromJson(Class<T> clazz, String json) throws HelperException {
        try {
            return objectMapper.readerForListOf(clazz).readValue(json);
        } catch (JsonProcessingException ex) {
            throw new HelperException("parse error");
        }
    }

    public Map<String, Object> mapFromJson(String json) throws HelperException {
        try {
            return objectMapper.readerForMapOf(Object.class).readValue(json);
        } catch (JsonProcessingException ex) {
            throw new HelperException("parse error");
        }
    }
    
}
