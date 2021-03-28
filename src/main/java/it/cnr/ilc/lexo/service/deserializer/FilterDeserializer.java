package it.cnr.ilc.lexo.service.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.cnr.ilc.lexo.service.data.FilterData.Filter;
import it.cnr.ilc.lexo.service.data.FilterData.FilterField;
import it.cnr.ilc.lexo.service.data.FilterData.FilterList;
import it.cnr.ilc.lexo.service.data.FilterData.Match;
import it.cnr.ilc.lexo.service.data.FilterData.MatchOption;
import it.cnr.ilc.lexo.service.data.FilterData.Operator;
import java.io.IOException;

/**
 *
 * @author andreabellandi
 */
public class FilterDeserializer extends StdDeserializer<Filter> {

    public FilterDeserializer() {
        this(null);
    }

    public FilterDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Filter deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = parser.readValueAsTree();
        return parseField(node);
    }

    private static Filter parseField(JsonNode node) {
        if (node.has("operator")) {
            FilterList filterList = new FilterList();
            filterList.setOperator(Operator.valueOf(node.get("operator").asText()));
            node.get("filters").spliterator().forEachRemaining(n -> {
                filterList.getFilters().add(parseField(n));
            });
            return filterList;
        } else {
            FilterField field = new FilterField();
            field.setField(node.get("field").asText());
            field.setValue(node.get("value").asText());
            field.setMatch(Match.valueOf(node.get("match").asText()));
            node.get("matchOptions").spliterator().forEachRemaining(n -> {
                field.getMatchOptions().add(MatchOption.valueOf(n.asText()));
            });
            return field;
        }
    }

}
