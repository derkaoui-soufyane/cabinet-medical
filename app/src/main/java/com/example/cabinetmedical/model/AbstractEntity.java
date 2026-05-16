package com.example.cabinetmedical.model;

import androidx.room.PrimaryKey;

public abstract class AbstractEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long createdAt;
    private long updatedAt;

    public AbstractEntity() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters et Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}