//package com.university.reminderapp.service;
//
//import com.university.reminderapp.dto.request.UniversityAdminRequest;
//import com.university.reminderapp.dto.request.UserRequest;
//import com.university.reminderapp.exception.BadRequestException;
//import com.university.reminderapp.exception.ResourceNotFoundException;
//import com.university.reminderapp.model.University;
//import com.university.reminderapp.model.User;
//import com.university.reminderapp.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UniversityService universityService;
//
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    public List<User> getUsersByUniversityAndRole(Long universityId, String role) {
//        return userRepository.findByUniversityIdAndRole(universityId, role);
//    }
//
//    public List<User> getStudentsByUniversity(Long universityId) {
//        return userRepository.findByUniversityIdAndRole(universityId, "STUDENT");
//    }
//
//    public List<User> getUniversityAdmins(Long universityId) {
//        return userRepository.findByUniversityIdAndRole(universityId, "UNIVERSITY_ADMIN");
//    }
//
//    public List<User> getStudentsByParent(Long parentId) {
//        return userRepository.findByParentId(parentId);
//    }
//
//    public User getUserById(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
//    }
//
//    public User getUserByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
//    }
//
//    public User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return getUserByEmail(email);
//    }
//
//    public User createUser(UserRequest request, User currentUser) {
//        // Check if email already exists
//        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//        if (existingUser.isPresent()) {
//            throw new BadRequestException("Email already in use");
//        }
//
//        // Get university
//        University university = universityService.getUniversityById(request.getUniversityId());
//
//        // Create new user
//        User user = new User();
//        user.setUniversity(university);
//        user.setEmail(request.getEmail());
//        user.setPassword(request.getPassword()); // In a real app, encrypt this
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setRole(request.getRole());
//        user.setStatus("ACTIVE");
//
//        // Set parent if provided and role is STUDENT
//        if (request.getParentId() != null && request.getRole().equals("STUDENT")) {
//            User parent = getUserById(request.getParentId());
//            if (!parent.getRole().equals("PARENT")) {
//                throw new BadRequestException("Parent ID must refer to a user with PARENT role");
//            }
//            user.setParent(parent);
//        }
//
//        user.setCreatedBy(currentUser);
//        user.setUpdatedBy(currentUser);
//
//        return userRepository.save(user);
//    }
//
//    public User createUniversityAdmin(UniversityAdminRequest request, User currentUser) {
//        // Check if email already exists
//        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//        if (existingUser.isPresent()) {
//            throw new BadRequestException("Email already in use");
//        }
//
//        // Get university
//        University university = universityService.getUniversityById(request.getUniversityId());
//
//        // Create new university admin
//        User admin = new User();
//        admin.setUniversity(university);
//        admin.setEmail(request.getEmail());
//        admin.setPassword(request.getPassword()); // In a real app, encrypt this
//        admin.setFirstName(request.getFirstName());
//        admin.setLastName(request.getLastName());
//        admin.setRole("UNIVERSITY_ADMIN");
//        admin.setStatus("ACTIVE");
//        admin.setCreatedBy(currentUser);
//        admin.setUpdatedBy(currentUser);
//
//        return userRepository.save(admin);
//    }
//
//    public User updateUser(Long id, UserRequest request, User currentUser) {
//        User user = getUserById(id);
//
//        // Check if email is being changed and already exists
//        if (!user.getEmail().equals(request.getEmail())) {
//            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//            if (existingUser.isPresent()) {
//                throw new BadRequestException("Email already in use");
//            }
//        }
//
//        user.setEmail(request.getEmail());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//
//        // Only allow role changes for non-self users
//        if (!user.getId().equals(currentUser.getId())) {
//            user.setRole(request.getRole());
//        }
//
//        // Update parent if provided and role is STUDENT
//        if (request.getParentId() != null && user.getRole().equals("STUDENT")) {
//            User parent = getUserById(request.getParentId());
//            if (!parent.getRole().equals("PARENT")) {
//                throw new BadRequestException("Parent ID must refer to a user with PARENT role");
//            }
//            user.setParent(parent);
//        }
//
//        user.setUpdatedBy(currentUser);
//
//        return userRepository.save(user);
//    }
//
//    public User deactivateUser(Long id, User currentUser) {
//        User user = getUserById(id);
//
//        // Prevent self-deactivation
//        if (user.getId().equals(currentUser.getId())) {
//            throw new BadRequestException("Cannot deactivate yourself");
//        }
//
//        user.setStatus("INACTIVE");
//        user.setUpdatedBy(currentUser);
//
//        return userRepository.save(user);
//    }
//
//    public void deleteUser(Long id, User currentUser) {
//        User user = getUserById(id);
//
//        // Prevent self-deletion
//        if (user.getId().equals(currentUser.getId())) {
//            throw new BadRequestException("Cannot delete yourself");
//        }
//
//        // Set status to DELETED
//        user.setStatus("DELETED");
//        user.setUpdatedBy(currentUser);
//        userRepository.save(user);
//    }
//}
//
//
//package com.university.reminderapp.service;
//
//import com.university.reminderapp.dto.request.UniversityAdminRequest;
//import com.university.reminderapp.dto.request.UserRequest;
//import com.university.reminderapp.exception.BadRequestException;
//import com.university.reminderapp.exception.ResourceNotFoundException;
//import com.university.reminderapp.model.University;
//import com.university.reminderapp.model.User;
//import com.university.reminderapp.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UniversityService universityService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    public List<User> getUsersByUniversityAndRole(Long universityId, String role) {
//        return userRepository.findByUniversityIdAndRole(universityId, role);
//    }
//
//    public List<User> getStudentsByUniversity(Long universityId) {
//        return userRepository.findByUniversityIdAndRole(universityId, "STUDENT");
//    }
//
//    public List<User> getUniversityAdmins(Long universityId) {
//        return userRepository.findByUniversityIdAndRole(universityId, "UNIVERSITY_ADMIN");
//    }
//
//    public List<User> getStudentsByParent(Long parentId) {
//        return userRepository.findByParentId(parentId);
//    }
//
//    public User getUserById(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
//    }
//
//    public User getUserByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
//    }
//
//    public User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
//            return null;
//        }
//        String email = authentication.getName();
//        return getUserByEmail(email);
//    }
//
//    public User createUser(UserRequest request, User currentUser) {
//        // Check if email already exists
//        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//        if (existingUser.isPresent()) {
//            throw new BadRequestException("Email already in use");
//        }
//
//        // Get university
//        University university = universityService.getUniversityById(request.getUniversityId());
//
//        // Create new user
//        User user = new User();
//        user.setUniversity(university);
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setRole(request.getRole());
//        user.setStatus("ACTIVE");
//
//        // Set parent if provided and role is STUDENT
//        if (request.getParentId() != null && "STUDENT".equals(request.getRole())) {
//            User parent = getUserById(request.getParentId());
//            if (!"PARENT".equals(parent.getRole())) {
//                throw new BadRequestException("Parent ID must refer to a user with PARENT role");
//            }
//            user.setParent(parent);
//        }
//
//        user.setCreatedBy(currentUser);
//        user.setUpdatedBy(currentUser);
//
//        return userRepository.save(user);
//    }
//
//    public User createUniversityAdmin(UniversityAdminRequest request, User currentUser) {
//        // Check if email already exists
//        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//        if (existingUser.isPresent()) {
//            throw new BadRequestException("Email already in use");
//        }
//
//        // Get university
//        University university = universityService.getUniversityById(request.getUniversityId());
//
//        // Create new university admin
//        User admin = new User();
//        admin.setUniversity(university);
//        admin.setEmail(request.getEmail());
//        admin.setPassword(passwordEncoder.encode(request.getPassword()));
//        admin.setFirstName(request.getFirstName());
//        admin.setLastName(request.getLastName());
//        admin.setRole("UNIVERSITY_ADMIN");
//        admin.setStatus("ACTIVE");
//        admin.setCreatedBy(currentUser);
//        admin.setUpdatedBy(currentUser);
//
//        return userRepository.save(admin);
//    }
//
//    public User updateUser(Long id, UserRequest request, User currentUser) {
//        User user = getUserById(id);
//
//        // Check if email is being changed and already exists
//        if (!user.getEmail().equals(request.getEmail())) {
//            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//            if (existingUser.isPresent()) {
//                throw new BadRequestException("Email already in use");
//            }
//            user.setEmail(request.getEmail());
//        }
//
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//
//        // Only allow role changes for non-self users
//        if (!user.getId().equals(currentUser.getId())) {
//            user.setRole(request.getRole());
//        }
//
//        // Update parent if provided and role is STUDENT
//        if (request.getParentId() != null && "STUDENT".equals(user.getRole())) {
//            User parent = getUserById(request.getParentId());
//            if (!"PARENT".equals(parent.getRole())) {
//                throw new BadRequestException("Parent ID must refer to a user with PARENT role");
//            }
//            user.setParent(parent);
//        } else if ("STUDENT".equals(user.getRole())) {
//            user.setParent(null);
//        }
//
//
//        user.setUpdatedBy(currentUser);
//
//        return userRepository.save(user);
//    }
//
//    public User deactivateUser(Long id, User currentUser) {
//        User user = getUserById(id);
//
//        // Prevent self-deactivation
//        if (user.getId().equals(currentUser.getId())) {
//            throw new BadRequestException("Cannot deactivate yourself");
//        }
//
//        user.setStatus("INACTIVE");
//        user.setUpdatedBy(currentUser);
//
//        return userRepository.save(user);
//    }
//
//    public void deleteUser(Long id, User currentUser) {
//        User user = getUserById(id);
//
//        // Prevent self-deletion
//        if (user.getId().equals(currentUser.getId())) {
//            throw new BadRequestException("Cannot delete yourself");
//        }
//
//        // Set status to DELETED
//        user.setStatus("DELETED");
//        user.setUpdatedBy(currentUser);
//        userRepository.save(user);
//    }
//}

package com.university.reminderapp.service;

import com.university.reminderapp.dto.request.UniversityAdminRequest;
import com.university.reminderapp.dto.request.UniversityAdminProfileRequest;
import com.university.reminderapp.dto.request.UniversityAdminUpdateRequest;
import com.university.reminderapp.dto.request.UserRequest;
import com.university.reminderapp.exception.BadRequestException;
import com.university.reminderapp.exception.ResourceNotFoundException;
import com.university.reminderapp.model.Bill;
import com.university.reminderapp.model.Event;
import com.university.reminderapp.model.Review;
import com.university.reminderapp.model.University;
import com.university.reminderapp.model.User;
import com.university.reminderapp.repository.BillRepository;
import com.university.reminderapp.repository.DeliveryLogRepository;
import com.university.reminderapp.repository.EventRepository;
import com.university.reminderapp.repository.NotificationRepository;
import com.university.reminderapp.repository.ReviewRepository;
import com.university.reminderapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityService universityService;

    // The PasswordEncoder is no longer used for encoding but is kept for Spring context
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DeliveryLogRepository deliveryLogRepository;

    @Autowired
    private EventRepository eventRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByUniversityAndRole(Long universityId, String role) {
        return userRepository.findByUniversityIdAndRole(universityId, role);
    }

    public List<User> getUsersByUniversity(Long universityId) {
        return userRepository.findByUniversityIdAndStatusNot(universityId, "DELETED");
    }

    public List<User> getStudentsByUniversity(Long universityId) {
        return userRepository.findByUniversityIdAndRole(universityId, "STUDENT");
    }

    public List<User> getUniversityAdmins(Long universityId) {
        return userRepository.findByUniversityIdAndRole(universityId, "UNIVERSITY_ADMIN");
    }

    public List<User> getStudentsByParent(Long parentId) {
        return userRepository.findByParentId(parentId);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String email = authentication.getName();
        return getUserByEmail(email);
    }

    public User createUser(UserRequest request, User currentUser) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Email already in use");
        }


        Long universityId = currentUser.getRole().equals("COMPANY_ADMIN") ? request.getUniversityId() : currentUser.getUniversity().getId();
        University university = universityService.getUniversityById(universityId);

        User user = new User();
        user.setUniversity(university);
        user.setEmail(request.getEmail());
        // Storing password in plain text
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");

        if (request.getParentId() != null && "STUDENT".equals(request.getRole())) {
            User parent = getUserById(request.getParentId());
            if (!"PARENT".equals(parent.getRole())) {
                throw new BadRequestException("Parent ID must refer to a user with PARENT role");
            }
            user.setParent(parent);
        }

        user.setCreatedBy(currentUser);
        user.setUpdatedBy(currentUser);

        return userRepository.save(user);
    }

    public User createUniversityAdmin(UniversityAdminRequest request, User currentUser) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Email already in use");
        }

        University university = universityService.getUniversityById(request.getUniversityId());

        User admin = new User();
        admin.setUniversity(university);
        admin.setEmail(request.getEmail());
        // Storing password in plain text
        admin.setPassword(request.getPassword());
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setRole("UNIVERSITY_ADMIN");
        admin.setStatus("ACTIVE");
        admin.setCreatedBy(currentUser);
        admin.setUpdatedBy(currentUser);

        return userRepository.save(admin);
    }

    public User updateUniversityAdmin(Long id, UniversityAdminUpdateRequest request, User currentUser) {
        User admin = getUserById(id);

        if (!"UNIVERSITY_ADMIN".equals(admin.getRole())) {
            throw new BadRequestException("Selected user is not a university administrator");
        }

        if (!admin.getUniversity().getId().equals(request.getUniversityId())) {
            throw new BadRequestException("University mismatch for administrator");
        }

        if (!admin.getEmail().equals(request.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new BadRequestException("Email already in use");
            }
            admin.setEmail(request.getEmail());
        }

        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            admin.setPassword(request.getPassword());
        }

        admin.setUpdatedBy(currentUser);
        return userRepository.save(admin);
    }

    public User updateUniversityAdminProfile(User admin, UniversityAdminProfileRequest request) {
        if (!admin.getEmail().equals(request.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new BadRequestException("Email already in use");
            }
            admin.setEmail(request.getEmail());
        }

        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            admin.setPassword(request.getPassword());
        }

        admin.setUpdatedBy(admin);
        return userRepository.save(admin);
    }

    public User updateUser(Long id, UserRequest request, User currentUser) {
        User user = getUserById(id);

        if (!user.getEmail().equals(request.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new BadRequestException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (!user.getId().equals(currentUser.getId())) {
            user.setRole(request.getRole());
        }

        if (request.getParentId() != null && "STUDENT".equals(user.getRole())) {
            User parent = getUserById(request.getParentId());
            if (!"PARENT".equals(parent.getRole())) {
                throw new BadRequestException("Parent ID must refer to a user with PARENT role");
            }
            user.setParent(parent);
        } else if ("STUDENT".equals(user.getRole())) {
            user.setParent(null);
        }

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            user.setStatus(request.getStatus());
        }

        user.setUpdatedBy(currentUser);
        return userRepository.save(user);
    }

    public User deactivateUser(Long id, User currentUser) {
        User user = getUserById(id);
        if (user.getId().equals(currentUser.getId())) {
            throw new BadRequestException("Cannot deactivate yourself");
        }
        user.setStatus("INACTIVE");
        user.setUpdatedBy(currentUser);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id, User currentUser) {
        User user = getUserById(id);
        if (user.getId().equals(currentUser.getId())) {
            throw new BadRequestException("Cannot delete yourself");
        }

        // Check if EVENT_MANAGER has created events
        if ("EVENT_MANAGER".equals(user.getRole())) {
            List<Event> createdEvents = eventRepository.findByCreatedBy(user);
            if (!createdEvents.isEmpty()) {
                throw new BadRequestException("Cannot delete this user because they have created " + 
                    createdEvents.size() + " event(s). Please transfer or delete the events first.");
            }
        }

        // Detach students if deleting a parent
        if ("PARENT".equals(user.getRole())) {
            List<User> children = getStudentsByParent(user.getId());
            for (User child : children) {
                child.setParent(null);
                child.setUpdatedBy(currentUser);
                userRepository.save(child);
            }
        }

        // Remove student-specific data
        if ("STUDENT".equals(user.getRole())) {
            List<Bill> studentBills = billRepository.findByStudent(user);
            billRepository.deleteAll(studentBills);

            List<Review> studentReviews = reviewRepository.findByStudent(user);
            reviewRepository.deleteAll(studentReviews);
        } else if ("PARENT".equals(user.getRole())) {
            List<Bill> parentBills = billRepository.findByParent(user);
            for (Bill bill : parentBills) {
                bill.setParent(null);
                billRepository.save(bill);
            }
        }

        // Remove notifications (and their delivery logs)
        notificationRepository.findByUser(user).forEach(notification -> {
            deliveryLogRepository.deleteByNotification(notification);
        });
        notificationRepository.deleteByUser(user);

        userRepository.delete(user);
    }
}
