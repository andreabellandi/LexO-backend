package it.cnr.ilc.lexo.service.data.text.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.ilc.lexo.manager.text.model.ValidationIssue;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Persistent metadata returned by the text corpus endpoints. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextRecord implements Data {

    public String fileId;
    public String documentUri;
    public String segmentationMethod;
    public Boolean frontMatterPresent;
    public String originalFileName;
    public String conlluFileName;
    public String originalPath;
    public String canonicalPath;
    public String conlluPath;
    public String nifPath;
    public String metadataPath;
    public String createdAt;
    public String completedAt;
    public Integer headingCount;
    public Integer paragraphCount;
    public Integer sentenceCount;
    public Integer tokenCount;
    public Map<String, String> metadata = new LinkedHashMap<String, String>();
    public List<ValidationIssue> warnings = new ArrayList<ValidationIssue>();

    public TextRecord() {
    }
}
