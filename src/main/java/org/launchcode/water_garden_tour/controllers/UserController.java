package org.launchcode.water_garden_tour.controllers;


import org.launchcode.water_garden_tour.models.User;
import org.launchcode.water_garden_tour.models.data.UserRepository;
import org.launchcode.water_garden_tour.models.garden.Garden;
import org.launchcode.water_garden_tour.models.garden.Owner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/list")
    public String displaySearchResults(Model model, @RequestParam(required = false, value = "searchTerm") String searchTerm) {

        Iterable<User> users;
        List<User> allUsers = userRepository.findAll();
        List<User> selectedUsers = new ArrayList<>();

        if (searchTerm == null || searchTerm.isEmpty()) {
            model.addAttribute("results",allUsers);
        } else {
            for (User user : allUsers) {

                if (user.getFname().toLowerCase().equals(searchTerm.toLowerCase())) {
                    selectedUsers.add(user);
                }

                if (user.getLname().toLowerCase().equals(searchTerm.toLowerCase())) {
                    selectedUsers.add(user);
                }
            }
            model.addAttribute("results", selectedUsers);
            model.addAttribute("search", "Results containing first or last name: " + searchTerm);
            //model.addAttribute("searchTerm", searchTerm);
        }
        model.addAttribute("title", "User Search");
        return "users/list";
    }

    @GetMapping("/users/update")
    public String renderUpdateUser(Model model, @RequestParam(value = "userId") int userId) {

        Optional <User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User userToUpdate = (User) optUser.get();
            model.addAttribute("user", userToUpdate);
        }

        model.addAttribute("title", "Update User");

        return "users/update";
    }

    @PostMapping("/users/update")
    public String updateUser(Model model,
                             @Valid @ModelAttribute User updateUser,
                             Errors errors,
                             int userId,
                             String username,
                             String fname,
                             String lname,
                             String role) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Update User");

            Optional <User> optUser = userRepository.findById(userId);
            if (optUser.isPresent()) {
                User userToUpdate = (User) optUser.get();
                model.addAttribute("user", userToUpdate);
            }
            model.addAttribute("errorMsg", "Errors found, please try again");
            model.addAttribute("errorStatus", "true");

            return "/users/update";
        }

        Optional<User> optUser = userRepository.findById(userId);
        User userToUpdate = (User) optUser.get();

        userToUpdate.setFname(fname);
        userToUpdate.setLname(lname);
        userToUpdate.setUsername(username);
        userToUpdate.setRole(role);

        userRepository.save(userToUpdate);

        model.addAttribute("results", userRepository.findAll());
        model.addAttribute("title", "User List");

        return "redirect:list";
    }

    @PostMapping("/users/delete")
    public String deleteUser(Model model, @RequestParam int userId) {

        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User userToDelete = (User) optUser.get();

            userRepository.delete(userToDelete);
        }

        //attributes for return to admin-list
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("title", "User List");

        return "redirect:list";

    }

}

