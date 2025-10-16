/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

/**
 *
 * @author andreabellandi
 */
@FunctionalInterface
public interface ProgressListener {

    void onProgress(int percent);
}
