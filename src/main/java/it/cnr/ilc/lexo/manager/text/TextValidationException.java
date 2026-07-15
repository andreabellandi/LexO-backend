package it.cnr.ilc.lexo.manager.text;

import it.cnr.ilc.lexo.manager.text.model.ValidationIssue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextValidationException extends Exception {

    private final List<ValidationIssue> issues;

    public TextValidationException(String message, List<ValidationIssue> issues) {
        super(message);
        this.issues = issues == null
                ? Collections.<ValidationIssue>emptyList()
                : Collections.unmodifiableList(new ArrayList<ValidationIssue>(issues));
    }

    public List<ValidationIssue> getIssues() {
        return issues;
    }
}
