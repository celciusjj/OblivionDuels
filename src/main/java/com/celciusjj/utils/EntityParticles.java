package com.celciusjj.utils;

import org.bukkit.entity.Entity;

public class EntityParticles {

    private int taskIdParticles;
    private Entity entity;

    public EntityParticles(Entity entity, int taskId) {
        this.entity = entity;
        this.taskIdParticles = taskId;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public int getTaskIdParticles() {
        return taskIdParticles;
    }

    public void setTaskIdParticles(int taskIdParticles) {
        this.taskIdParticles = taskIdParticles;
    }

}
