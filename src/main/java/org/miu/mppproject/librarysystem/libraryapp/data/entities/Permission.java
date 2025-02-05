package org.miu.mppproject.librarysystem.libraryapp.data.entities;

public final class Permission extends BaseEntity {

    private final String actionName;


    public Permission(String actionName) {
        super("PM-");
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }


}

