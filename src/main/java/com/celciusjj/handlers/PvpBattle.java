package com.celciusjj.handlers;

import java.util.UUID;


public class PvpBattle {
    private UUID retador;
    private UUID target;
    private int id;
    private EntityParticles banner;

    public PvpBattle(UUID retador, UUID target, int id, EntityParticles banner) {
        this.retador = retador;
        this.target = target;
        this.id = id;
        this.banner = banner;
    }


    public UUID getRetador() {
        return retador;
    }

    public void setRetador(UUID retador) {
        this.retador = retador;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EntityParticles getBanner() {
        return banner;
    }

    public void setBanner(EntityParticles banner) {
        this.banner = banner;
    }

}
