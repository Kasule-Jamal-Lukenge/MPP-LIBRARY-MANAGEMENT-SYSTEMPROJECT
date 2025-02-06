package org.miu.mppproject.librarysystem.libraryapp.backend.data.entities;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseEntity implements Serializable {

    private final String id;

    public BaseEntity(String prefix) {
        this.id = prefix + UUID.randomUUID(); // Auto-generate a unique ID
    }

    public String getId() {
        return id;
    }
}