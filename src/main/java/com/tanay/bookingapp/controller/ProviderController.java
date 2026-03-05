package com.tanay.bookingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanay.bookingapp.dto.ProviderRequestDTO;
import com.tanay.bookingapp.entity.Provider;
import com.tanay.bookingapp.repository.ProviderRepository;

@RestController
@RequestMapping("/api/provider")
public class ProviderController {

    @Autowired
    private ProviderRepository providerRepository;

    @PostMapping("/register")
    public String registerProvider(@RequestBody ProviderRequestDTO dto) {

        Provider provider = new Provider();

        provider.setName(dto.getName());
        provider.setEmail(dto.getEmail());
        provider.setPhone(dto.getPhone());
        provider.setCategory(dto.getCategory());
        provider.setExperience(dto.getExperience());

        providerRepository.save(provider);

        return "Provider registered successfully. Waiting for admin approval.";
    }
}