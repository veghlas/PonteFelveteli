package com.pontefelveteli.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "app_user")
@NoArgsConstructor
@Getter
@Setter
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_seq")
    @SequenceGenerator(schema="public", name="app_user_seq", sequenceName="public.app_user_seq", allocationSize=1)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "name_of_mother")
    private String nameOfMother;

    @Column(name = "social_security_number", unique = true)
    private Integer socialSecurityNumber;

    @Column(name = "tax_number", unique = true)
    private Integer taxNumber;

    @Column(name = "email_address", unique = true)
    private String email;

    @ElementCollection
    @CollectionTable(name = "phone_numbers")
    @Column(name = "phone_number", unique = true)
    private List<String> phoneNumbers;

    @OneToMany(mappedBy = "user")
    private List<Address> addressList;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "roles")
    private List<Role> roles;
}
