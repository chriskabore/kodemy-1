package com.bt.dev.kodemy.users.model;

import com.bt.dev.kodemy.users.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class UnitTests {

    @Test
    public void checkThatConstructorsWork(){

        //User constructor

        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setUsername("JohnDoe");
        testUser.setEnabled(true);
        testUser.setNote("some dummy text");
        testUser.setGender(Gender.MALE);
        testUser.setSecured(false);

        // ContactInfo constructor
        ContactInfos contactInfos = new ContactInfos();
        contactInfos.setEmail("johndoe@beogotech.com");
        contactInfos.setPhone("+3531122334455");
        contactInfos.setFacebook("facebook/johndoe");
        contactInfos.setLinkedin("linkedIn");
        contactInfos.setSkype("skype");
        contactInfos.setWebsite("www.johndoe.me");

        testUser.setContactInfos(contactInfos);

        // dates
        LocalDateTime dateCreated = LocalDateTime.of(2021,12,24,13,45);
        LocalDateTime dateUpdated = LocalDateTime.of(2022,2,24,15,55);
        LocalDateTime dateOfLastLogin = LocalDateTime.of(2022,3,31,12,55);
        LocalDate birthDate = LocalDate.of(1985,12,8);

        testUser.setBirthDate(birthDate);
        testUser.setDateCreated(dateCreated);
        testUser.setDateUpdated(dateUpdated);
        testUser.setDateOfLastLogin(dateOfLastLogin);

        // Address Info
        AddressInfos addressInfos = new AddressInfos();
        addressInfos.setAddress("22 Downing street");
        addressInfos.setAddress2("Pissy, secteur 26");
        addressInfos.setCity("Ouagadougou");
        addressInfos.setCountry("Gondwana City");
        addressInfos.setZipCode("11BP2345");

        testUser.setAddressInfos(addressInfos);

        // Roles
        Role roleUser = new Role("USER");
        Role roleAdmin = new Role("ADMINISTRATOR");

        // Permissions

        Permission p1 = new Permission(1L, "LOGIN", true, "Login");
        Permission p2 = new Permission(2L, "VIEW_PROFILE", true, "View Profile");
        Permission p3 = new Permission(3L, "ADMIN_STATISTICS", false, "View statistical graphs");
        Permission p4 = new Permission(4L, "ADMIN_PROFILES", true, "Manage users");

        roleUser.getPermissions().add(p1);
        roleUser.getPermissions().add(p2);

        roleAdmin.getPermissions().add(p3);
        roleAdmin.getPermissions().add(p4);

        testUser.addRole(roleUser);
        testUser.addRole(roleAdmin);

        // Testing constructors

        assertEquals(testUser.getUserId(),1L);
        assertEquals(testUser.getUsername(),"JohnDoe");
        assertEquals(testUser.getFirstName(),"John");
        assertEquals(testUser.isEnabled(),true);
        assertEquals(testUser.isSecured(),false);

        // Contact Info
        ContactInfos actualContactInfo = testUser.getContactInfos();
       assertNotNull(actualContactInfo);

        assertEquals(testUser.getContactInfos().getEmail(),"johndoe@beogotech.com");

        // Dates
        assertEquals(dateCreated, testUser.getDateCreated());
        assertEquals(dateUpdated, testUser.getDateUpdated());
        assertEquals(dateOfLastLogin, testUser.getDateOfLastLogin());

        // roles
        assertNotNull(testUser.getRoles());

        Set<Role> roles = testUser.getRoles();

        assertTrue(roles.contains(roleUser));
        assertTrue(roles.contains(roleAdmin));
        assertEquals(2, testUser.getRoles().size());
        assertTrue(testUser.getRoles().contains(roleUser));
        assertTrue(testUser.getRoles().contains(roleAdmin));

        // Permissions
        assertEquals(4, testUser.getPermissions().size());


    }
}
