package co.com.inventory.r2dbc.auth.mapper;

import co.com.inventory.model.user.Person;
import co.com.inventory.r2dbc.auth.data.PersonData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PersonMapper {

    public PersonData buildPersonData(Person person) {
        return PersonData.builder()
                .names(person.getNames())
                .surnames(person.getSurnames())
                .address(person.getAddress())
                .cellPhoneNumber(person.getCellPhoneNumber())
                .email(person.getEmail())
                .build();
    }

    public Person buildPerson(PersonData personData) {
        return Person.builder()
                .id(personData.getId())
                .names(personData.getNames())
                .surnames(personData.getSurnames())
                .address(personData.getAddress())
                .cellPhoneNumber(personData.getCellPhoneNumber())
                .email(personData.getEmail())
                .build();
    }
}
