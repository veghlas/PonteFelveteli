package com.pontefelveteli.service;

import com.pontefelveteli.domain.Address;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.AddressInfo;
import com.pontefelveteli.dto.CreateAddressCommand;
import com.pontefelveteli.dto.UpdateAddressCommand;
import com.pontefelveteli.exception.AddressNotFoundException;
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

    public List<AddressInfo> saveAddress(AppUser appUSer, List<CreateAddressCommand> createAddressCommandList) {
        List<Address> addressList = new ArrayList<>();
        createAddressCommandList.forEach(createAddressCommand -> {
            Address address = modelMapper.map(createAddressCommand, Address.class);
            address.setUser(appUSer);
            addressList.add(address);
            addressRepository.save(address);
        });
        return mapAddresListToAddresInfoList(addressList);
    }

    public List<AddressInfo> mapAddresListToAddresInfoList(List<Address> addressList) {
        List<AddressInfo> addressInfoList = new ArrayList<>();
        addressList.forEach(address -> {
            AddressInfo addressInfo = modelMapper.map(address, AddressInfo.class);
            addressInfoList.add(addressInfo);
        });
        return addressInfoList;
    }



    private List<Address> findAddressesByUser(String email) {
        return addressRepository.findByEmail(email);
    }

    private Address findAddressesById(String email, Integer addressId) {
        Optional<Address> addressOptional = addressRepository.findByIdAndMail(email, addressId);
        if (addressOptional.isEmpty()) {
            throw new AddressNotFoundException(addressId, email);
        }
        return addressOptional.get();
    }

    public Address updateAddress(AppUser appUserToUpdate, UpdateAddressCommand updateAddressCommand) {
        Address addressById = findAddressesById(appUserToUpdate.getEmail(), updateAddressCommand.getId());
        addressById.setCity(updateAddressCommand.getCity());
        addressById.setStreet(updateAddressCommand.getStreet());
        addressById.setZipCode(updateAddressCommand.getZipCode());
        addressById.setHouseNumber(updateAddressCommand.getHouseNumber());
        addressRepository.save(addressById);
        return addressById;
    }
}
