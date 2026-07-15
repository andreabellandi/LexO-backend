package it.cnr.ilc.lexo.manager.text;

import it.cnr.ilc.lexo.manager.text.model.ValidationIssue;
import java.util.List;

public final class ControlledCommonMarkException extends TextValidationException {
    public ControlledCommonMarkException(List<ValidationIssue> issues) {
        super("Invalid controlled CommonMark document", issues);
    }
}
