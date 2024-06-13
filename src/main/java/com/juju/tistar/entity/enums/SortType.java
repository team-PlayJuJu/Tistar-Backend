package com.juju.tistar.entity.enums;

public enum SortType {
    HEARTS("hearts"),
    LATEST("latests"),
    OLDEST("oldests"),
    DEFAULT("default");

    private final String sortType;

    SortType(String sortType) {
        this.sortType = sortType;
    }

    public final String getSortType() {
        return sortType;
    }
}
