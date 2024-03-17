package com.pontefelveteli.service;

import com.pontefelveteli.domain.Address;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.AddressInfo;
import com.pontefelveteli.dto.CreateAddressCommand;
import com.pontefelveteli.dto.CreateAppUserCommand;
import com.pontefelveteli.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public List<AddressInfo> saveAddress(AppUser appUSer, List<CreateAddressCommand> createAddressCommandList) {
        List<Address> addressList = new ArrayList<>();
        List<AddressInfo> addressInfoList = new ArrayList<>();
        createAddressCommandList.forEach(createAddressCommand -> {
            Address address = modelMapper.map(createAddressCommand, Address.class);
            addressList.add(address);
            address.setUser(appUSer);
            addressRepository.save(address);
        });

        addressList.forEach(address -> {
            AddressInfo addressInfo = modelMapper.map(address, AddressInfo.class);
            addressInfoList.add(addressInfo);
        });
        return addressInfoList;
    }
}
