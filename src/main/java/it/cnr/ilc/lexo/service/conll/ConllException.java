/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.conll;

/**
 *
 * @author andreabellandi
 */
public class ConllException extends RuntimeException {

   public ConllException() {
    }

    public ConllException(String message) {
        super(message);
    }

    public ConllException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConllException(Throwable cause) {
        super(cause);
    }

    public ConllException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
