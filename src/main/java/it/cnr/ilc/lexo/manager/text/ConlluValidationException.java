package it.cnr.ilc.lexo.manager.text;

import it.cnr.ilc.lexo.manager.text.model.ValidationIssue;
import java.util.List;

public final class ConlluValidationException extends TextValidationException {
    public ConlluValidationException(List<ValidationIssue> issues) {
        super("Invalid CoNLL-U document", issues);
    }
}
