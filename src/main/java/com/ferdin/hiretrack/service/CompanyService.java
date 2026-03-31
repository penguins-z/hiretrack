package com.ferdin.hiretrack.service;

import com.ferdin.hiretrack.dto.CompanyRequestDTO;
import com.ferdin.hiretrack.dto.CompanyResponseDTO;
import com.ferdin.hiretrack.entity.Company;
import com.ferdin.hiretrack.exception.ResourceNotFoundException;
import com.ferdin.hiretrack.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyResponseDTO createCompany(CompanyRequestDTO requestDTO) {
        Company company = new Company();
        company.setName(toTitleCase(requestDTO.getName()));
        company.setIndustry(requestDTO.getIndustry());
        company.setWebsite(requestDTO.getWebsite());
        company.setEmail(requestDTO.getEmail());
        company.setLocation(requestDTO.getLocation());
        return toResponseDTO(companyRepository.save(company));
    }

    public List<CompanyResponseDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CompanyResponseDTO getCompanyById(Long id) {
        return toResponseDTO(findCompanyById(id));
    }

    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO requestDTO) {
        Company existing = findCompanyById(id);
        existing.setName(toTitleCase(requestDTO.getName()));
        existing.setIndustry(requestDTO.getIndustry());
        existing.setWebsite(requestDTO.getWebsite());
        existing.setEmail(requestDTO.getEmail());
        existing.setLocation(requestDTO.getLocation());
        return toResponseDTO(companyRepository.save(existing));
    }

    public void deleteCompany(Long id) {
        Company existing = findCompanyById(id);
        companyRepository.delete(existing);
    }

    public List<CompanyResponseDTO> searchCompanies(String name) {
        return companyRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Company findCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No Company with ID : " + id + " exists"));
    }

    private CompanyResponseDTO toResponseDTO(Company company) {
        CompanyResponseDTO dto = new CompanyResponseDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setIndustry(company.getIndustry());
        dto.setWebsite(company.getWebsite());
        dto.setEmail(company.getEmail());
        dto.setLocation(company.getLocation());
        return dto;
    }

    private String toTitleCase(String input) {
        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }
}