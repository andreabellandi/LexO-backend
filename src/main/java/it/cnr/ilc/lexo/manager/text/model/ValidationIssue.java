package it.cnr.ilc.lexo.manager.text.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/** A structured validation problem that can be serialized in a job response. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationIssue {

    public Integer line;
    public Integer column;
    public String code;
    public String message;

    public ValidationIssue() {
    }

    public ValidationIssue(Integer line, Integer column, String code, String message) {
        this.line = line;
        this.column = column;
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        if (line != null) {
            out.append("line ").append(line);
            if (column != null) {
                out.append(':').append(column);
            }
            out.append(' ');
        }
        if (code != null) {
            out.append('[').append(code).append("] ");
        }
        return out.append(message == null ? "" : message).toString();
    }
}
