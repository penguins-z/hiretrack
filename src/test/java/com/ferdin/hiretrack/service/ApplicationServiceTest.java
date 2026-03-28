package com.ferdin.hiretrack.service;

import com.ferdin.hiretrack.dto.ApplicationRequestDTO;
import com.ferdin.hiretrack.dto.ApplicationResponseDTO;
import com.ferdin.hiretrack.entity.Application;
import com.ferdin.hiretrack.entity.ApplicationStatus;
import com.ferdin.hiretrack.entity.Company;
import com.ferdin.hiretrack.entity.User;
import com.ferdin.hiretrack.exception.ResourceNotFoundException;
import com.ferdin.hiretrack.repository.ApplicationRepository;
import com.ferdin.hiretrack.repository.CompanyRepository;
import com.ferdin.hiretrack.repository.UserRepository;
import com.ferdin.hiretrack.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.module.ResolutionException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ApplicationService applicationService;

    private User testUser;
    private Company testCompany;
    private Application testApplication;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test123@example.com");
        testUser.setPassword("hashedpassword");

        testCompany = new Company();
        testCompany.setId(1L);
        testCompany.setName("Test Company");
        testCompany.setIndustry("Technology");

        testApplication = new Application();
        testApplication.setId(1L);
        testApplication.setJobTitle("Test Job Title");
        testApplication.setStatus(ApplicationStatus.SAVED);
        testApplication.setUser(testUser);
        testApplication.setCompany(testCompany);
    }

    @Test
    void createApplication_success() {
        ApplicationRequestDTO requestDTO = new ApplicationRequestDTO();
        requestDTO.setCompanyId(testCompany.getId());
        requestDTO.setJobTitle("Test Job Title");

        when(securityUtils.getCurrentUserId()).thenReturn(testUser.getId());
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        ApplicationResponseDTO result = applicationService.createApplication(requestDTO);
        assertNotNull(result);
        assertEquals("Test Job Title", result.getJobTitle());
        assertEquals("Test Company", result.getCompanyName());
        assertEquals(ApplicationStatus.SAVED, result.getStatus());
        verify(applicationRepository, times(1)).save(any(Application.class));

    }

    @Test
    void getApplicationById_NotFound_ThrowsException() {
        when(securityUtils.getCurrentUserId()).thenReturn(testUser.getId());
        when(applicationRepository.findById(9999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> applicationService.getApplicationById(9999L));
    }

    @Test
    void getApplicationById_WrongUser_ThrowsException() {
        User differentUser = new User();
        differentUser.setId(2L);
        testApplication.setUser(differentUser);

        when(securityUtils.getCurrentUserId()).thenReturn(testUser.getId());
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(testApplication));

        assertThrows(ResourceNotFoundException.class, () ->
                applicationService.getApplicationById(1L));
    }

    @Test
    void deleteApplication_Success() {
        when(securityUtils.getCurrentUserId()).thenReturn(testUser.getId());
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(testApplication));

        applicationService.deleteApplication(1L);
        verify(applicationRepository, times(1)).delete(testApplication);
    }
}