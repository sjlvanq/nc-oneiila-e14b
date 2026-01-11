package com.churncheck.api.domain.attendance;

import java.time.LocalDateTime;
import com.churncheck.api.domain.client.Client;
import jakarta.persistence.*;

@Entity
@Table(name = "attendance")
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @Column(name = "checked_in_at", nullable = false)
    private LocalDateTime checkedInAt;
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Client getClient() {
        return client;
    }
    
    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }
}