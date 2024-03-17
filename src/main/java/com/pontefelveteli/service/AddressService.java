package com.pontefelveteli.service;

import com.pontefelveteli.domain.Address;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.AddressInfo;
import com.pontefelveteli.dto.CreateAppUserCommand;
import com.pontefelveteli.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddressService {

    private AddressRepository addressRepository;
    private ModelMapper modelMapper;
    @Autowired
    public AddressService(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    public AddressInfo saveAddress(AppUser appUSer, CreateAppUserCommand createAppUserCommand) {
        Address address = modelMapper.map(createAppUserCommand, Address.class);
        address.setUser(appUSer);
        addressRepository.save(address);
        return modelMapper.map(address, AddressInfo.class);
    }
}
