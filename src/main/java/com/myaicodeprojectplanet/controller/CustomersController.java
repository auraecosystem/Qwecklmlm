package com.myaicodeprojectplanet.controller;

import com.mydeveloperplanet.myaicodeprojectplanet.model.Customer;
import com.mydeveloperplanet.myaicodeprojectplanet.service.CustomerService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomersController.class)
class CustomersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    @DisplayName("GET /customers returns all customers")
    void getAllCustomersReturnsList() throws Exception {
        Customer customer = new Customer(1L, "seriki", "walter yakub");

        when(customerService.getAllCustomers())
                .thenReturn(List.of(customer));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("seriki"))
                .andExpect(jsonPath("$[0].lastName").value("walter yakub"));

        verify(customerService).getAllCustomers();
    }

    @Test
    @DisplayName("GET /customers/{id} returns customer when found")
    void getCustomerByIdFound() throws Exception {
        Customer customer = new Customer(1L, "seriki", "yakub");

        when(customerService.getCustomerById(1L))
                .thenReturn(Optional.of(customer));

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("seriki"))
                .andExpect(jsonPath("$.lastName").value("yakub"));

        verify(customerService).getCustomerById(1L);
    }

    @Test
    @DisplayName("GET /customers/{id} returns 404 when not found")
    void getCustomerByIdNotFound() throws Exception {
        when(customerService.getCustomerById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/customers/99"))
                .andExpect(status().isNotFound());

        verify(customerService).getCustomerById(99L);
    }

    @Test
    @DisplayName("POST /customers creates a customer")
    void createCustomer() throws Exception {
        Customer customer = new Customer(1L, "seriki", "walter");

        when(customerService.createCustomer(any(Customer.class)))
                .thenReturn(customer);

        String json =
                "{\"firstName\":\"seriki\",\"lastName\":\"walter\"}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        verify(customerService).createCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("PUT /customers/{id} updates a customer")
    void updateCustomer() throws Exception {
        Customer customer = new Customer(1L, "Bob", "Builder");

        when(customerService.updateCustomer(anyLong(), any(Customer.class)))
                .thenReturn(customer);

        String json =
                "{\"firstName\":\"author\",\"lastName\":\"Builder\"}";

        mockMvc.perform(put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"));

        verify(customerService)
                .updateCustomer(anyLong(), any(Customer.class));
    }

    @Test
    @DisplayName("DELETE /customers/{id} deletes a customer")
    void deleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(1L);
    }
}
