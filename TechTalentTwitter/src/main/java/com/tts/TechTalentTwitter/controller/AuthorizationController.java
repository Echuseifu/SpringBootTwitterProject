package com.tts.TechTalentTwitter.controller;

import com.tts.TechTalentTwitter.model.User;
import com.tts.TechTalentTwitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthorizationController {

    // injecting our UserService
    @Autowired
    private UserService userService;

    @GetMapping(value="/login")
    public String login(){
        return "login";
    }

    // serve as a signup page(GET)
    @GetMapping(value="/signup")
    public String registration(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    // this method allows us to have anew user posted our database
    // serve to handle form submissions when a user hits submit on the signup page(Post)
    @PostMapping(value = "/signup")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
        User userExists = userService.findByUsername(user.getUsername());
        if (userExists != null) {
            bindingResult.rejectValue("username", "error.user", "Username is already taken");
        }

        // ensuring there's no errors with the binding results
        // if there is no errors, we can go ahead and continue
        if (!bindingResult.hasErrors()) {
            userService.saveNewUser(user);
            model.addAttribute("success", "Sign up successful!");
            model.addAttribute("user", new User());
        }

        // return a reference to a template
        return "registration";
    }

}