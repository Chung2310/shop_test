package com.example.shop.mapper;

import com.example.shop.model.contact.ContactDTO;
import com.example.shop.model.contact.Contact;
import com.example.shop.model.contact.ContactRequest;
import com.example.shop.model.contact.ContactResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ContactMapper {

    @Mapping(source = "userEntity", target = "userEntityDTO")
    @Mapping(source = "userEntityContact", target = "userEntityContactDTO")
    ContactDTO toContactDTO(Contact contact);
    Contact toEntity(ContactDTO contactDTO);

    List<ContactDTO> toContactDTOs(List<Contact> contacts);
    List<Contact> toEntities(List<ContactDTO> contactDTOs);

    @Mapping(source = "userId", target = "userEntity.id")
    @Mapping(source = "userContactId", target = "userEntityContact.id")
    Contact toContact(ContactRequest contactRequest);

    @Mapping(source = "userEntity", target = "userEntity")
    @Mapping(source = "userEntityContact", target = "userEntityContact")
    ContactResponse toContact(Contact  contact);

    List<ContactResponse> toContactResponseList(List<Contact> contacts);
}
