package com.tts.TechTalentTwitter.controller;


import com.tts.TechTalentTwitter.model.Tweet;
import com.tts.TechTalentTwitter.model.TweetDisplay;
import com.tts.TechTalentTwitter.model.User;
import com.tts.TechTalentTwitter.service.TweetService;
import com.tts.TechTalentTwitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;


// here we are injecting the dependencies or
//    you can either autowire both or inject them via a constructor both method
//    the above and this one works the same
//    public TweetController(UserService userService, TweetService tweetService) {
//        this.userService = userService;
//        this.tweetService = tweetService;
//    }


    //We iterate through the list of users that are being followed by
    // the currently logged in user to see if the user whose profile we are viewing is one of them.
    //generate a boolean that indicates whether the profile page that is being returned belongs to
    // the currently logged in user.
    @GetMapping(value = "/users/{username}")
    public String getUser(@PathVariable(value = "username") String username, Model model) {
        User loggedInUser = userService.getLoggedInUser();
        User user = userService.findByUsername(username);
        List<TweetDisplay> tweets = tweetService.findAllByUser(user);
        List<User> following = loggedInUser.getFollowing();
        boolean isFollowing = false;
        for (User followedUser : following) {
            if (followedUser.getUsername().equals(username)) {
                isFollowing = true;
            }
        }
        boolean isSelfPage = loggedInUser.getUsername().equals(username);
        model.addAttribute("isSelfPage", isSelfPage);
        model.addAttribute("following", isFollowing);
        model.addAttribute("tweetList", tweets);
        model.addAttribute("user", user);
        return "user";
    }



  //  this shows a list of all users
//    @GetMapping(value = "/users")
//    public String getUsers(Model model) {
//        List<User> users = userService.findAll();
//        User loggedInUser = userService.getLoggedInUser();
//        List<User> usersFollowing = loggedInUser.getFollowing();
//        SetFollowingStatus(users, usersFollowing, model);
//        model.addAttribute("users", users);
//        SetTweetCounts(users, model);
//        return "users";
//    }

    // this shows a list of all users that only the user follow
    @GetMapping(value = "/users")
    public String getUsers(@RequestParam(value = "filter", required = false) String filter, Model model) {
        List<User> users = new ArrayList<User>();

        User loggedInUser = userService.getLoggedInUser();

        List<User> usersFollowing = loggedInUser.getFollowing();
        List<User> usersFollowers = loggedInUser.getFollowers();
        if (filter == null) {
            filter = "all";
        }
        if (filter.equalsIgnoreCase("followers")) {
            users = usersFollowers;
            model.addAttribute("filter", "followers");
        } else if (filter.equalsIgnoreCase("following")) {
            users = usersFollowing;
            model.addAttribute("filter", "following");
        } else {
            users = userService.findAll();
            model.addAttribute("filter", "all");
        }
        model.addAttribute("users", users);
        SetTweetCounts(users, model);
        SetFollowingStatus(users, usersFollowing, model);

        return "users";
    }




     // this show how many times each user has tweeted
    // this method will take in a list of users and the Model
    // and update the Model to include tweet counts
    // to store tweet counts, we are using a HashMap
    // we iterate through each user, getting a list of their tweets
    // and adding the size of that list to the HashMap
    // can be viewed from  users.html
    private void SetTweetCounts(List<User> users, Model model){
            HashMap<String,Integer> tweetCounts = new HashMap<>();
        for (User user : users) {
            List<TweetDisplay> tweets = tweetService.findAllByUser(user);
            tweetCounts.put(user.getUsername(), tweets.size());
        }
        model.addAttribute("tweetCounts", tweetCounts);
    }


    // this add HashMap to the Model
    // We iterate though each user and check to see whether or not they're being followed.
    // Then we add each result to the HashMap, and add the HashMap to the model.
    private void SetFollowingStatus(List<User> users, List<User> usersFollowing, Model model) {
        HashMap<String, Boolean> followingStatus = new HashMap<>();
        String username = userService.getLoggedInUser().getUsername();
        for (User user : users) {
            if (usersFollowing.contains(user)) {
                followingStatus.put(user.getUsername(), true);
            } else if (!user.getUsername().equals(username)) {
                followingStatus.put(user.getUsername(), false);
            }
        }
        model.addAttribute("followingStatus", followingStatus);
    }


}