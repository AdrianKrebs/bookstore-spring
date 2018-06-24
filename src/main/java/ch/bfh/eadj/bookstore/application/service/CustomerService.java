package ch.bfh.eadj.bookstore.application.service;

import ch.bfh.eadj.bookstore.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.bookstore.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.bookstore.application.exception.InvalidPasswordException;
import ch.bfh.eadj.bookstore.persistence.dto.CustomerInfo;
import ch.bfh.eadj.bookstore.persistence.entity.Customer;
import ch.bfh.eadj.bookstore.persistence.entity.Login;
import ch.bfh.eadj.bookstore.persistence.enumeration.UserGroup;
import ch.bfh.eadj.bookstore.persistence.repository.CustomerRepository;
import ch.bfh.eadj.bookstore.persistence.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoginRepository loginRepository;

    public Long registerCustomer(Customer customer, String password) throws EmailAlreadyUsedException {

        Login login = loginRepository.findByUsername(customer.getEmail());
        if (login == null) {
            Login newLogin = new Login();
            customerRepository.save(customer);
            newLogin.setGroup(UserGroup.CUSTOMER);
            newLogin.setPassword(password);
            newLogin.setUsername(customer.getEmail());
            loginRepository.save(newLogin);
            return customer.getNr();
        } else {
            throw new EmailAlreadyUsedException();
        }
    }

    public Long authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException {
        Login login = loginRepository.findByUsername(email);

        if (login != null) {
                if (password.equals(login.getPassword())) {
                    return login.getNr();
                } else {
                    throw new InvalidPasswordException();
                }
        } else {
            throw new CustomerNotFoundException();
        }
    }

    public Customer findCustomer(Long nr) throws CustomerNotFoundException {

        Customer customer = customerRepository.findByNr(nr);

        if (customer != null) {
            return customer;
        } else {
            throw new CustomerNotFoundException();
        }
    }

    public List<CustomerInfo> searchCustomers(String name) {

        List<CustomerInfo> customerInfoList = customerRepository.findByName(name);

        if (!customerInfoList.isEmpty()) {
            return customerInfoList;
        } else {
            return Collections.emptyList();
        }
    }

    public void updateCustomer(Customer customer) throws CustomerNotFoundException, EmailAlreadyUsedException {

        Customer customerDb = customerRepository.findByNr(customer.getNr());

        if (customerDb == null) {
            throw new CustomerNotFoundException();
        }
        if (!customerDb.getEmail().equals(customer.getEmail())) {
            // check if new Email allready is in use
            Login login = loginRepository.findByUsername(customer.getEmail());
            if (login != null) {
                throw new EmailAlreadyUsedException();
            }
        }
        Login login = loginRepository.findByUsername(customerDb.getEmail());
        if (login != null) {
            loginRepository.save(login);
            customerRepository.save(customer);
//        } else {
//            throw new IllegalStateException("Failed to update login and customer. No unique login found for customer.");
        }
    }

    public void changePassword(String email, String password) throws CustomerNotFoundException {
        Login login = loginRepository.findByUsername(email);

        if (login != null) {
            login.setPassword(password);
            loginRepository.save(login);
        } else {
            throw new CustomerNotFoundException();
        }
    }


    public void removeCustomer(Customer customer) throws CustomerNotFoundException {
        Login login = loginRepository.findByNr(customer.getNr());
        if (login != null) {
            loginRepository.delete(login);
            customerRepository.delete(customer);
        } else {
            throw new CustomerNotFoundException();
        }
    }
}

