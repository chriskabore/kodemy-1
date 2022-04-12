package com.bt.dev.kodemy.users.services;

import com.bt.dev.kodemy.users.exceptions.*;
import com.bt.dev.kodemy.users.model.*;
import com.bt.dev.kodemy.users.repositories.AddressInfosRepository;
import com.bt.dev.kodemy.users.repositories.ContactInfosRepository;
import com.bt.dev.kodemy.users.repositories.RoleRepository;
import com.bt.dev.kodemy.users.repositories.UserRepository;
import com.bt.dev.kodemy.users.validators.EmailValidator;
import com.bt.dev.kodemy.users.validators.PasswordValidator;
import com.bt.dev.kodemy.users.validators.PhoneValidator;
import dtos.requests.CreateOrUpdateUserDTO;
import dtos.requests.RegisterUserAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.base.Strings;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UserService {


    private UserRepository userRepository;

    private ContactInfosRepository contactInfosRepository;

    private AddressInfosRepository addressInfosRepository;

    private RoleRepository roleRepository;

    @Value("${kodemy.security.password.salt}")
    private String salt;

    private PasswordValidator passwordValidator;
    private EmailValidator emailValidator;
    private PhoneValidator phoneValidator;

    @Autowired
    public UserService(UserRepository userRepository,ContactInfosRepository contactInfosRepository,
                       AddressInfosRepository addressInfosRepository,RoleRepository roleRepository ) {
        this.userRepository = userRepository;
        this.contactInfosRepository = contactInfosRepository;
        this.addressInfosRepository = addressInfosRepository;
        this.roleRepository = roleRepository;
        passwordValidator = new PasswordValidator();
        emailValidator = new EmailValidator();
        phoneValidator = new PhoneValidator();
    }

    public UserService() {
        passwordValidator = new PasswordValidator();
        emailValidator = new EmailValidator();
        phoneValidator = new PhoneValidator();
    }

    public User getUserById(Long id) {
        if (id == null) {
            throw new InvalidUserIdentifierException("User Id cannot be null");
        }
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new UserNotFoundException(String.format("User not found for Id = %s", id));
    }

    public User getUserByUsername(String username) {
        if (username == null) {
            throw new InvalidUsernameException("username cannot be null");
        }
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }else{
            throw new UserNotFoundException(String.format("User not found for Username = %s", username));
        }
    }

    public User getUserByEmail(String email) {
        if (email == null) {
            throw new InvalidEmailException("email cannot be null");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }else{
            throw new UserNotFoundException(String.format("User not found for email address = %s", email));
        }
    }


    // check if the username has not been registered
    public void checkIfUsernameNotUsed(String username) {
        User userByUsername = getUserByUsername(username);
        if (userByUsername != null) {
            String msg = String.format("The username %s it's already in use from another user with ID = %s",
                    userByUsername.getUsername(), userByUsername.getUserId());
            log.error(msg);
            throw new InvalidUserDataException(msg);
        }
    }

    // check if the email has not been registered
    public void checkIfEmailNotUsed(String email) {
        User userByEmail = getUserByEmail(email);
        if (userByEmail != null) {
            String msg = String.format("The email %s it's already in use from another user with ID = %s",
                    userByEmail.getContactInfos().getEmail(), userByEmail.getUserId());
            log.error(msg);
            throw new InvalidUserDataException(String.format("This email %s it's already in use.",
                    userByEmail.getContactInfos().getEmail()));
        }
    }

    // add contactInfos on user
    public void addUserContactInfosOnUser(User user, ContactInfos contactInfos) {
        contactInfos.setUser(user);
        user.setContactInfos(contactInfos);

        log.debug(String.format("Contact information set on User %s .", user.getUserId()));
    }

    // add address info on user
    public void addUserAddressInfosOnUser(User user, AddressInfos addressInfos) {
        addressInfos.setUser(user);
        user.setAddressInfos(addressInfos);

        log.debug(String.format("Address information set on User %s .", user.getUserId()));
    }

    public void addUserRole(User user, long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (!optionalRole.isPresent()) {
            throw new RoleNotFoundException("Role cannot be null");
        }
        user.getRoles().add(optionalRole.get());
    }

    public Iterable<User> getUserList() {
        return userRepository.findAll();
    }

    // remove a role on user
    @Transactional
    public User removeRole(Long userId, Long roleId) {
        // check user
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", userId));
        }
        User user = optionalUser.get();

        // check role
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOptional.get();

        user.getRoles().remove(role);
        user.setDateUpdated(LocalDateTime.now());

        userRepository.save(user);
        log.info(String.format("Removed role %s on user id = %s", role.getRoleName(), user.getUserId()));

        return user;
    }


    // add a role on user

    @Transactional
    public User addRole(Long userId, Long roleId) {
        // check user
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", userId));
        }
        User user = optionalUser.get();

        // check role
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOptional.get();

        user.getRoles().add(role);
        user.setDateUpdated(LocalDateTime.now());

        userRepository.save(user);
        log.info(String.format("Added role %s on user id = %s", role.getRoleName(), user.getUserId()));

        return user;
    }

    // Login
    @Transactional
    public User login(String username, String password) {
        if ((Strings.isNullOrEmpty(username)) || (Strings.isNullOrEmpty(password))) {
            throw new InvalidLoginException("Username or Password cannot be null or empty");
        }

        User user = getUserByUsername(username);
        if (user == null) {
            // invalid username
            throw new InvalidLoginException("Invalid username or password");
        }

        log.info(String.format("Login request from %s", username));

        // check the password
        if (EncryptionService.isPasswordValid(password, user.getPassword(), salt)) {
            // check if the user is enabled
            if (!user.isEnabled()) {
                // not enabled
                throw new InvalidLoginException("User is not enabled");
            }

            // update the last login timestamp
            user.setDateOfLastLogin(LocalDateTime.now());
            userRepository.save(user);

            log.info(String.format("Valid login for %s", username));
        } else {
            throw new InvalidLoginException("Invalid username or password");
        }
        return user;
    }


    // delete user

    @Transactional
    public void deleteUserById(Long id) {
        if (id == null) {
            throw new InvalidUserIdentifierException("Id cannot be null");
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", id));
        }

        // only not secured users can be deleted
        User user = userOpt.get();
        if (user.isSecured()) {
            throw new UserIsSecuredException(String.format("User %s is secured and cannot be deleted.", id));
        }

        userRepository.deleteById(id);
        log.info(String.format("User %s has been deleted.", id));
    }

    // update user
    @Transactional
    public User updateUser(Long userId, CreateOrUpdateUserDTO userToUpdate) {
        if (userId == null) {
            throw new InvalidUserIdentifierException("Id cannot be null");
        }
        if (userToUpdate == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(String.format("The user with Id = %s doesn't exists", userId));
        }
        User user = optionalUser.get();

        // check if the username has not been registered
        User userByUsername = getUserByUsername(userToUpdate.getUsername());
        if (userByUsername != null) {
            // check if the user's id is different than the actual user
            if (!user.getUserId().equals(userByUsername.getUserId())) {
                String msg = String.format("The username %s it's already in use from another user with ID = %s",
                        userToUpdate.getUsername(), userByUsername.getUserId());
                log.error(msg);
                throw new InvalidUserDataException(msg);
            }
        }

        passwordValidator.checkPassword(userToUpdate.getPassword());
        emailValidator.checkEmail(userToUpdate.getEmail());
        phoneValidator.checkPhone(userToUpdate.getPhone());

        // check if the new email has not been registered yet
        User userEmail = getUserByEmail(userToUpdate.getEmail());
        if (userEmail != null) {
            // check if the user's email is different than the actual user
            if (!user.getUserId().equals(userEmail.getUserId())) {
                String msg = String.format("The email %s it's already in use from another user with ID = %s",
                        userToUpdate.getEmail(), userEmail.getUserId());
                log.error(msg);
                throw new InvalidUserDataException(msg);
            }
        }

        // update the user
        user.setUsername(userToUpdate.getUsername());

        // using the user's salt to secure the new validated password
        user.setPassword(EncryptionService.encrypt(userToUpdate.getPassword(), salt));
        user.setFirstName(userToUpdate.getFirstName());
        user.setLastName(userToUpdate.getLastName());

        // set gender
        Gender gender = Gender.getValidGender(userToUpdate.getGender());
        user.setGender(gender);

        // date of birth
        user.setBirthDate(userToUpdate.getBirthDate());

        user.setEnabled(userToUpdate.isEnabled());
        user.setNote(userToUpdate.getNote());

        // set contact: entity always present
        ContactInfos contact = user.getContactInfos();
        contact.setEmail(userToUpdate.getEmail());
        contact.setPhone(userToUpdate.getPhone());
        contact.setSkype(userToUpdate.getSkype());
        contact.setFacebook(userToUpdate.getFacebook());
        contact.setLinkedin(userToUpdate.getLinkedin());
        contact.setWebsite(userToUpdate.getWebsite());
        contact.setNote(userToUpdate.getContactNote());

        user.setDateUpdated(LocalDateTime.now());

        // set address
        AddressInfos address = user.getAddressInfos();
        if (address == null) {
            address = new AddressInfos();
        }
        address.setAddress(userToUpdate.getAddress());
        address.setAddress2(userToUpdate.getAddress2());
        address.setCity(userToUpdate.getCity());
        address.setCountry(userToUpdate.getCountry());
        address.setZipCode(userToUpdate.getZipCode());

        addUserAddressInfosOnUser(user, address);

        User userUpdated = userRepository.save(user);
        log.info(String.format("User %s has been updated.", user.getUserId()));

        return userUpdated;
    }


    // Create new User

    @Transactional
    public User createUser(CreateOrUpdateUserDTO createUserDTO) {
        if (createUserDTO == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        checkIfUsernameNotUsed(createUserDTO.getUsername());
        checkIfEmailNotUsed(createUserDTO.getEmail());
        passwordValidator.checkPassword(createUserDTO.getPassword());
        emailValidator.checkEmail(createUserDTO.getEmail());
        phoneValidator.checkPhone(createUserDTO.getPhone());

        // create the user
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setPassword(EncryptionService.encrypt(createUserDTO.getPassword(), salt));

        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());

        // set gender
        Gender gender = Gender.getValidGender(createUserDTO.getGender());
        user.setGender(gender);

        // date of birth
        user.setBirthDate(createUserDTO.getBirthDate());

        user.setEnabled(true);
        user.setSecured(createUserDTO.isSecured());

        user.setNote(createUserDTO.getNote());
        user.setDateCreated(LocalDateTime.now());

        // set default user the role
        addUserRole(user, Role.USER);

        User userCreated = userRepository.save(user);

        // set contact
        ContactInfos contact = new ContactInfos();
        contact.setEmail(createUserDTO.getEmail());
        contact.setPhone(createUserDTO.getPhone());
        contact.setSkype(createUserDTO.getSkype());
        contact.setFacebook(createUserDTO.getFacebook());
        contact.setLinkedin(createUserDTO.getLinkedin());
        contact.setWebsite(createUserDTO.getWebsite());
        contact.setNote(createUserDTO.getContactNote());

        addUserContactInfosOnUser(userCreated, contact);

        // set address
        AddressInfos address = new AddressInfos();
        address.setAddress(createUserDTO.getAddress());
        address.setAddress2(createUserDTO.getAddress2());
        address.setCity(createUserDTO.getCity());
        address.setCountry(createUserDTO.getCountry());
        address.setZipCode(createUserDTO.getZipCode());

        addUserAddressInfosOnUser(userCreated, address);

        userCreated = userRepository.save(userCreated);

        log.info(String.format("User %s has been created.", userCreated.getUserId()));
        return userCreated;
    }

    // Register new user

    @Transactional
    public User registerUserAccount(RegisterUserAccountDTO registerUserAccountDTO) {
        if (registerUserAccountDTO == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        checkIfUsernameNotUsed(registerUserAccountDTO.getUsername());
        passwordValidator.checkPassword(registerUserAccountDTO.getPassword());
        emailValidator.checkEmail(registerUserAccountDTO.getEmail());

        checkIfEmailNotUsed(registerUserAccountDTO.getEmail());

        // create the new user account: not all the user information required
        User user = new User();
        user.setUsername(registerUserAccountDTO.getUsername());
        user.setPassword(EncryptionService.encrypt(registerUserAccountDTO.getPassword(), salt));

        user.setFirstName(registerUserAccountDTO.getFirstName());
        user.setLastName(registerUserAccountDTO.getLastName());
        user.setEnabled(true);
        user.setSecured(false);

        // set gender
        Gender gender = Gender.getValidGender(registerUserAccountDTO.getGender());
        user.setGender(gender);

        addUserRole(user, Role.USER);
        user.setDateCreated(LocalDateTime.now());

        User userCreated = userRepository.save(user);

        // set contact
        ContactInfos contact = new ContactInfos();
        contact.setEmail(registerUserAccountDTO.getEmail());

        addUserContactInfosOnUser(userCreated, contact);

        // set empty address
        addUserAddressInfosOnUser(userCreated, new AddressInfos());

        userCreated = userRepository.save(userCreated);

        log.info(String.format("User %s has been created.", userCreated.getUserId()));
        return userCreated;
    }



}
