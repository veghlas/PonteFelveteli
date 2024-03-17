package com.pontefelveteli.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@NoArgsConstructor
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addresses_seq")
    @SequenceGenerator(name="addresses_seq", sequenceName="addresses_seq", allocationSize=1)
    private Integer id;

    @Column(name = "zip_code")
    private Integer zipCode;

    @Column
    private String city;

    @Column
    private String street;

    //Ez string ha véletlenül van pl. 2/A vagy hasonló házszám.
    @Column(name = "house_number")
    private String houseNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}
