package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.hibernate.entity.Account;
import it.cnr.ilc.lexo.hibernate.entity.Accountable;
import it.cnr.ilc.lexo.util.StringUtil;

/**
 *
 * @author andreabellandi
 */
public final class AccessManager implements Manager {

    public enum Topic {
        SYSTEM,
        TYPES,
        ACCOUNT,
    }

    public enum Type {
        ADMINISTRATOR,
        EDITOR,
        VIEWER
    }

    public enum Permission {
        READ,
        WRITE,
        LOCK,
    }

    public enum Level {
        NONE,
        MINE,
        FULL,
    }

    private static final Level[][][] ACCESSES = new Level[Topic.values().length][Type.values().length][Permission.values().length];

    static {
        ACCESSES[Topic.SYSTEM.ordinal()][Type.ADMINISTRATOR.ordinal()][Permission.READ.ordinal()] = Level.NONE;
        ACCESSES[Topic.SYSTEM.ordinal()][Type.ADMINISTRATOR.ordinal()][Permission.WRITE.ordinal()] = Level.NONE;
        ACCESSES[Topic.SYSTEM.ordinal()][Type.EDITOR.ordinal()][Permission.READ.ordinal()] = Level.NONE;
        ACCESSES[Topic.SYSTEM.ordinal()][Type.EDITOR.ordinal()][Permission.WRITE.ordinal()] = Level.NONE;
        ACCESSES[Topic.SYSTEM.ordinal()][Type.VIEWER.ordinal()][Permission.READ.ordinal()] = Level.NONE;
        ACCESSES[Topic.SYSTEM.ordinal()][Type.VIEWER.ordinal()][Permission.WRITE.ordinal()] = Level.NONE;

        ACCESSES[Topic.TYPES.ordinal()][Type.ADMINISTRATOR.ordinal()][Permission.READ.ordinal()] = Level.FULL;
        ACCESSES[Topic.TYPES.ordinal()][Type.ADMINISTRATOR.ordinal()][Permission.WRITE.ordinal()] = Level.FULL;
        ACCESSES[Topic.TYPES.ordinal()][Type.EDITOR.ordinal()][Permission.READ.ordinal()] = Level.FULL;
        ACCESSES[Topic.TYPES.ordinal()][Type.EDITOR.ordinal()][Permission.WRITE.ordinal()] = Level.NONE;
        ACCESSES[Topic.TYPES.ordinal()][Type.VIEWER.ordinal()][Permission.READ.ordinal()] = Level.FULL;
        ACCESSES[Topic.TYPES.ordinal()][Type.VIEWER.ordinal()][Permission.WRITE.ordinal()] = Level.NONE;

        ACCESSES[Topic.ACCOUNT.ordinal()][Type.ADMINISTRATOR.ordinal()][Permission.READ.ordinal()] = Level.FULL;
        ACCESSES[Topic.ACCOUNT.ordinal()][Type.ADMINISTRATOR.ordinal()][Permission.WRITE.ordinal()] = Level.FULL;
        ACCESSES[Topic.ACCOUNT.ordinal()][Type.ADMINISTRATOR.ordinal()][Permission.LOCK.ordinal()] = Level.FULL;
        ACCESSES[Topic.ACCOUNT.ordinal()][Type.EDITOR.ordinal()][Permission.READ.ordinal()] = Level.MINE;
        ACCESSES[Topic.ACCOUNT.ordinal()][Type.EDITOR.ordinal()][Permission.WRITE.ordinal()] = Level.MINE;
        ACCESSES[Topic.ACCOUNT.ordinal()][Type.EDITOR.ordinal()][Permission.LOCK.ordinal()] = Level.MINE;
        ACCESSES[Topic.ACCOUNT.ordinal()][Type.VIEWER.ordinal()][Permission.READ.ordinal()] = Level.NONE;
        ACCESSES[Topic.ACCOUNT.ordinal()][Type.VIEWER.ordinal()][Permission.WRITE.ordinal()] = Level.NONE;
        ACCESSES[Topic.ACCOUNT.ordinal()][Type.VIEWER.ordinal()][Permission.LOCK.ordinal()] = Level.NONE;

    }

    AccessManager() {
    }

    public boolean hasPermission(Account account, Topic topic, Permission permission) {
        Level level = ACCESSES[topic.ordinal()][account.getType().getPosition() - 1][permission.ordinal()];
        if (Level.FULL.equals(level)) {
            return true;
        } else {
            return account.getSettings().containsKey(StringUtil.upperCaseToCamelCase(topic.name()) + "Manager");
        }
    }

    public boolean hasPermissionOnAccountable(Account account, Topic topic, Permission permission, Accountable accountable) {
        if (account.getSettings().containsKey(StringUtil.upperCaseToCamelCase(topic.name()) + "Manager")) {
            return true;
        } else {
            Level level = ACCESSES[topic.ordinal()][account.getType().getPosition() - 1][permission.ordinal()];
            if (null == level) {
                return false;
            } else switch (level) {
                case FULL:
                    return true;
                case MINE:
                    return accountable.account().getId().equals(account.getId());
                default:
                    return false;
            }
        }
    }

//    public boolean hasPermissionOnResourceable(AccountEntity account, Topic topic, Permission permission, Resourceable resourceable) {
//        if (account.getSettings().containsKey(StringUtil.upperCaseToCamelCase(topic.name()) + "Manager")) {
//            return true;
//        } else {
//            Level level = ACCESSES[topic.ordinal()][account.getType().getPosition() - 1][permission.ordinal()];
//            if (null == level) {
//                return false;
//            } else switch (level) {
//                case FULL:
//                    return true;
//                case MINE:
//                    return resourceable.match(account);
//                default:
//                    return false;
//            }
//        }
//    }

//    public static interface Accountable {
//
//        public boolean match(AccountEntity account);
//    }
//
//    public static interface Resourceable {
//
//        public boolean match(AccountEntity account);
//    }
}
