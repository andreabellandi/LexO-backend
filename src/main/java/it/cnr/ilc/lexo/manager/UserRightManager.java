/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author andreabellandi
 */
public class UserRightManager implements Manager, Cached {

    // Note: This class is "project-specific". It implements the authorization policy
    
    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public enum Operation {
        READ, WRITE, LOCK;
    }
    
    public boolean checkPermission(Operation op, String roles) {
        if (op.equals(Operation.LOCK)) {
            return StringUtils.containsIgnoreCase("admin", roles) || StringUtils.containsIgnoreCase("reviewer", roles);
        } else {
            if (op.equals(Operation.WRITE)) {
                return StringUtils.containsIgnoreCase("admin", roles) || StringUtils.containsIgnoreCase("reviewer", roles)
                        || StringUtils.containsIgnoreCase("editor", roles);
            } else {
                if (op.equals(Operation.READ)) {
                    return true;
                }
            }
        }
        return false;
    }

}
