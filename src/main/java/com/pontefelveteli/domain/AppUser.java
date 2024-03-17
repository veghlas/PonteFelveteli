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
    @SequenceGenerator(name="app_user_seq", sequenceName="app_user_seq", allocationSize=1)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "name_of_mother")
    private String nameOfMother;

    @Column(name = "social_security_number")
    private Integer socialSecurityNumber;

    @Column(name = "tax_number")
    private Integer taxNumber;

    @Column(name = "email_address")
    private String email;

    //Postgre engedi, így nem csinálok neki külön entity-t. Szerintem logikailag így jobb lesz.
    @ElementCollection
    @CollectionTable(name = "phone_numbers")
    @Column(name = "phone_number")
    private List<String> phone_numbers;

    @OneToMany(mappedBy = "user")
    private List<Address> addressList;

    @Column
    private Boolean active;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role")
    private List<Role> roles;
}
