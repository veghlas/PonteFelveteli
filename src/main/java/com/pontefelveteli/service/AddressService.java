package com.pontefelveteli.service;

import com.pontefelveteli.domain.Address;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.AddressInfo;
import com.pontefelveteli.dto.UpdateAddressCommand;
import com.pontefelveteli.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

//    public List<AddressInfo> saveAddress(AppUser appUSer, List<CreateAddressCommand> createAddressCommandList) {
//        List<Address> addressList = new ArrayList<>();
//        createAddressCommandList.forEach(createAddressCommand -> {
//            Address address = modelMapper.map(createAddressCommand, Address.class);
//            address.setUser(appUSer);
//            addressList.add(address);
//            addressRepository.save(address);
//        });
//        return mapAddresListToAddresInfoList(addressList);
//    }

    public List<AddressInfo> mapAddresListToAddresInfoList(List<Address> addressList) {
        List<AddressInfo> addressInfoList = new ArrayList<>();
        addressList.forEach(address -> {
            AddressInfo addressInfo = modelMapper.map(address, AddressInfo.class);
            addressInfoList.add(addressInfo);
        });
        return addressInfoList;
    }


    private List<Address> findAddressesByName(String name) {
        return addressRepository.findByName(name);
    }

    private Address findAddressById(String name, Integer addressId) {
        Optional<Address> addressOptional = addressRepository.findByNameAndId(name, addressId);
        Address address = addressOptional.orElse(null);
        return address;
    }

    public List<Address> updateAddress(AppUser appUserToUpdate, UpdateAddressCommand updateAddressCommand) {
        Address addressById = findAddressById(appUserToUpdate.getName(), updateAddressCommand.getId());
        if (addressById == null) {
            addressById = new Address();
        }

        addressById.setCity(updateAddressCommand.getCity());
        addressById.setStreet(updateAddressCommand.getStreet());
        addressById.setZipCode(updateAddressCommand.getZipCode());
        addressById.setHouseNumber(updateAddressCommand.getHouseNumber());
        addressById.setUser(appUserToUpdate);
        addressRepository.save(addressById);
        return findAddressesByName(appUserToUpdate.getName());
    }
}
