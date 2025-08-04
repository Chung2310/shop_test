package com.example.shop.dto.mapper;

import com.example.shop.dto.ContactDTO;
import com.example.shop.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ContactMapper {

    @Mapping(source = "user", target = "userDTO")
    @Mapping(source = "userContact", target = "userContactDTO")
    ContactDTO toContactDTO(Contact contact);
    Contact toEntity(ContactDTO contactDTO);

    List<ContactDTO> toContactDTOs(List<Contact> contacts);
    List<Contact> toEntities(List<ContactDTO> contactDTOs);
}
