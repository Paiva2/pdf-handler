package com.root.signaturehandler.infra.models.enums;

import lombok.Getter;

@Getter
public enum DocumentsOrderBy {
    DESC("desc"), ASC("asc");

    private final String orderBy;

    DocumentsOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
