package com.devcambodia.HPK_Booking.utils;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.util.Date;

@MappedSuperclass
@Data
public class AuditEntity {
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_at", insertable = false)
    private Date updatedAt;

    @Column(name = "updated_by", insertable = false)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date(System.currentTimeMillis());
        // Optionally set createdBy/updatedBy here if you have user context
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date(System.currentTimeMillis());
        // Optionally set updatedBy here if you have user context
    }



}
