package com.university.reminderapp.controller.web;

import com.university.reminderapp.dto.request.CompanyRequest;
import com.university.reminderapp.dto.request.UniversityAdminRequest;
import com.university.reminderapp.dto.request.UniversityAdminUpdateRequest;
import com.university.reminderapp.dto.request.UniversityRequest;
import com.university.reminderapp.model.Company;
import com.university.reminderapp.model.University;
import com.university.reminderapp.model.User;
import com.university.reminderapp.service.CompanyService;
import com.university.reminderapp.service.UniversityService;
import com.university.reminderapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/companies")
public class CompanyAdminController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "admin/company/company-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new CompanyRequest());
        return "admin/company/company-form";
    }

    @PostMapping("/create")
    public String createCompany(@Valid @ModelAttribute("company") CompanyRequest request,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/company/company-form";
        }

        User currentUser = userService.getCurrentUser();
        companyService.createCompany(request, currentUser);

        redirectAttributes.addFlashAttribute("success", "Company created successfully");
        return "redirect:/admin/companies";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Company company = companyService.getCompanyById(id);
        CompanyRequest request = new CompanyRequest();
        request.setName(company.getName());
        request.setDescription(company.getDescription());
        request.setStatus(company.getStatus());

        model.addAttribute("company", request);
        model.addAttribute("companyId", id);

        return "admin/company/company-form";
    }

    @PostMapping("/edit/{id}")
    public String updateCompany(@PathVariable Long id,
                                @Valid @ModelAttribute("company") CompanyRequest request,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/company/company-form";
        }

        User currentUser = userService.getCurrentUser();
        companyService.updateCompany(id, request, currentUser);

        redirectAttributes.addFlashAttribute("success", "Company updated successfully");
        return "redirect:/admin/companies";
    }

    @PostMapping("/{id}/deactivate")
    public String deactivateCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        companyService.deactivateCompany(id, currentUser);

        redirectAttributes.addFlashAttribute("success", "Company deactivated successfully");
        return "redirect:/admin/companies";
    }

    @PostMapping("/{id}/activate")
    public String activateCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        companyService.activateCompany(id, currentUser);

        redirectAttributes.addFlashAttribute("success", "Company activated successfully");
        return "redirect:/admin/companies";
    }

    @PostMapping("/{id}/delete")
    public String deleteCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        companyService.deleteCompany(id, currentUser);

        redirectAttributes.addFlashAttribute("success", "Company deleted successfully");
        return "redirect:/admin/companies";
    }

    // University management within the company
    @GetMapping("/{companyId}/universities")
    public String listUniversities(@PathVariable Long companyId, Model model) {
        model.addAttribute("company", companyService.getCompanyById(companyId));
        model.addAttribute("universities", universityService.getUniversitiesByCompany(companyId));
        return "admin/university/university-list";
    }

    @GetMapping("/{companyId}/universities/create")
    public String showCreateUniversityForm(@PathVariable Long companyId, Model model) {
        UniversityRequest university = new UniversityRequest();
        university.setCompanyId(companyId);

        model.addAttribute("university", university);
        model.addAttribute("company", companyService.getCompanyById(companyId));

        return "admin/university/university-form";
    }

    @PostMapping("/{companyId}/universities/create")
    public String createUniversity(@PathVariable Long companyId,
                                   @Valid @ModelAttribute("university") UniversityRequest request,
                                   BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/university/university-form";
        }

        request.setCompanyId(companyId);
        User currentUser = userService.getCurrentUser();
        universityService.createUniversity(request, currentUser);

        redirectAttributes.addFlashAttribute("success", "University created successfully");
        return "redirect:/admin/companies/" + companyId + "/universities";
    }

    @GetMapping("/{companyId}/universities/{id}/admins")
    public String listUniversityAdmins(@PathVariable Long companyId,
                                       @PathVariable Long id,
                                       Model model) {
        model.addAttribute("company", companyService.getCompanyById(companyId));
        model.addAttribute("university", universityService.getUniversityById(id));
        model.addAttribute("admins", userService.getUsersByUniversityAndRole(id, "UNIVERSITY_ADMIN"));
        return "admin/university/university-admin-list";
    }

    @GetMapping("/{companyId}/universities/{id}/admins/create")
    public String showCreateUniversityAdminForm(@PathVariable Long companyId,
                                                @PathVariable Long id, Model model) {
        UniversityAdminRequest admin = new UniversityAdminRequest();
        admin.setUniversityId(id);

        model.addAttribute("admin", admin);
        model.addAttribute("university", universityService.getUniversityById(id));
        model.addAttribute("company", companyService.getCompanyById(companyId));
        model.addAttribute("adminId", null);

        return "admin/university/admin-form";
    }

    @PostMapping("/{companyId}/universities/{id}/admins/create")
    public String createUniversityAdmin(@PathVariable Long companyId, @PathVariable Long id,
                                        @Valid @ModelAttribute("admin") UniversityAdminRequest request,
                                        BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/university/admin-form";
        }

        request.setUniversityId(id);
        User currentUser = userService.getCurrentUser();
        userService.createUniversityAdmin(request, currentUser);

        redirectAttributes.addFlashAttribute("success", "University admin created successfully");
        return "redirect:/admin/companies/" + companyId + "/universities/" + id + "/admins";
    }

    @GetMapping("/{companyId}/universities/{universityId}/admins/{adminId}/edit")
    public String showEditUniversityAdminForm(@PathVariable Long companyId,
                                              @PathVariable Long universityId,
                                              @PathVariable Long adminId,
                                              Model model) {
        User admin = userService.getUserById(adminId);

        UniversityAdminUpdateRequest request = new UniversityAdminUpdateRequest();
        request.setUniversityId(universityId);
        request.setEmail(admin.getEmail());
        request.setFirstName(admin.getFirstName());
        request.setLastName(admin.getLastName());

        model.addAttribute("admin", request);
        model.addAttribute("adminId", adminId);
        model.addAttribute("university", universityService.getUniversityById(universityId));
        model.addAttribute("company", companyService.getCompanyById(companyId));

        return "admin/university/admin-form";
    }

    @PostMapping("/{companyId}/universities/{universityId}/admins/{adminId}/edit")
    public String updateUniversityAdmin(@PathVariable Long companyId,
                                        @PathVariable Long universityId,
                                        @PathVariable Long adminId,
                                        @Valid @ModelAttribute("admin") UniversityAdminUpdateRequest request,
                                        BindingResult result,
                                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/university/admin-form";
        }

        User currentUser = userService.getCurrentUser();
        userService.updateUniversityAdmin(adminId, request, currentUser);

        redirectAttributes.addFlashAttribute("success", "University admin updated successfully");
        return "redirect:/admin/companies/" + companyId + "/universities/" + universityId + "/admins";
    }

    @PostMapping("/{companyId}/universities/{universityId}/admins/{adminId}/delete")
    public String deleteUniversityAdmin(@PathVariable Long companyId,
                                        @PathVariable Long universityId,
                                        @PathVariable Long adminId,
                                        RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        userService.deleteUser(adminId, currentUser);

        redirectAttributes.addFlashAttribute("success", "University admin deleted successfully");
        return "redirect:/admin/companies/" + companyId + "/universities/" + universityId + "/admins";
    }

    @GetMapping("/{companyId}/universities/{id}/edit")
    public String showEditUniversityForm(@PathVariable Long companyId,
                                         @PathVariable Long id, Model model) {
        University university = universityService.getUniversityById(id);

        UniversityRequest request = new UniversityRequest();
        request.setCompanyId(university.getCompany().getId());
        request.setName(university.getName());
        request.setDescription(university.getDescription());
        request.setAddress(university.getAddress());
        request.setContactEmail(university.getContactEmail());
        request.setContactPhone(university.getContactPhone());
        request.setStatus(university.getStatus());

        model.addAttribute("university", request);
        model.addAttribute("universityId", id);
        model.addAttribute("company", companyService.getCompanyById(companyId));

        return "admin/university/university-form";
    }

    @PostMapping("/{companyId}/universities/{id}/edit")
    public String updateUniversity(@PathVariable Long companyId,
                                   @PathVariable Long id,
                                   @Valid @ModelAttribute("university") UniversityRequest request,
                                   BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/university/university-form";
        }

        User currentUser = userService.getCurrentUser();
        universityService.updateUniversity(id, request, currentUser);

        redirectAttributes.addFlashAttribute("success", "University updated successfully");
        return "redirect:/admin/companies/" + companyId + "/universities";
    }

    @PostMapping("/{companyId}/universities/{id}/deactivate")
    public String deactivateUniversity(@PathVariable Long companyId,
                                       @PathVariable Long id,
                                       RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        universityService.deactivateUniversity(id, currentUser);

        redirectAttributes.addFlashAttribute("success", "University deactivated successfully");
        return "redirect:/admin/companies/" + companyId + "/universities";
    }

    @PostMapping("/{companyId}/universities/{id}/delete")
    public String deleteUniversity(@PathVariable Long companyId,
                                   @PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        universityService.deleteUniversity(id);

        redirectAttributes.addFlashAttribute("success", "University deleted successfully");
        return "redirect:/admin/companies/" + companyId + "/universities";
    }

}
