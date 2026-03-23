package com.ferdin.hiretrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contacts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Contact name cannot be blank")
    private String name;

    private String role;

    private String email;

    private String linkedIn;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
}