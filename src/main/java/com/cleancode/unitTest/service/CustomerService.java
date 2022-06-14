package com.cleancode.unitTest.service;

import com.cleancode.unitTest.entity.Customer;
import com.cleancode.unitTest.exception.CustomerDoNotExistException;
import com.cleancode.unitTest.exception.ItemNotValidException;
import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Integer getCustomerId(Integer userId) {
        Optional<Customer> customerOptional = customerRepository.findById(userId);
        if (customerOptional.isEmpty()) {
            throw new CustomerDoNotExistException();
        }
        return customerOptional.get().getId();
    }

    public void isValidUser(Integer userId) {
        Optional<Customer> customerOptional = customerRepository.findById(userId);
        if (customerOptional.isEmpty()) {
            throw new CustomerDoNotExistException();
        }
    }

    public Customer getCustomer(Integer userId) {
        Optional<Customer> customerOptional = customerRepository.findById(userId);
        if (customerOptional.isEmpty()) {
            throw new CustomerDoNotExistException();
        }
        return customerOptional.get();
    }
}
