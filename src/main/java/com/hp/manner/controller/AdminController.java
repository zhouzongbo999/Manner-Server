package com.hp.manner.controller;

import com.hp.manner.exception.UserExistsException;
import com.hp.manner.model.User;
import com.hp.manner.service.UserServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final Logger logger = Logger.getLogger(getClass());

    public static final String ADMIN_HOME_PAGE = "admin/home.html";
    public static final String ADMIN_USER_MANAGEMENT_PAGE = "admin/user-manage.html";

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping({ "/", "/index", "/home" })
    public String renderAdminHomePage(ModelMap modelMap) {
        logger.info("rendering Admin home page - " + ADMIN_HOME_PAGE);
        modelMap.addAttribute("message", "This is Admin home Page");
        return ADMIN_HOME_PAGE;
    }

    @ModelAttribute("allUsers")
    public List<User> allUsers() {
        return userService.listAllUsers();
    }

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    @RequestMapping("/users")
    public String renderUserManagementPage() {
        logger.info("rendering Admin users management page - " + ADMIN_USER_MANAGEMENT_PAGE);
        return ADMIN_USER_MANAGEMENT_PAGE;
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute("user") User user, ModelMap modelMap, BindingResult bindingResult, HttpServletRequest req) {
        logger.info("add new user.");
        /*
        * check if there is any binding result errors first.
        * If yes, return to same page with validation error messages.
        * If no, then proceed.
        */
        if (bindingResult.hasErrors()) {
            return ADMIN_USER_MANAGEMENT_PAGE;
        }

        if (req.getParameter("admin-user") != null ) {
            user.setRole(User.Role.ADMIN);
        } else {
            user.setRole(User.Role.USER);
        }

        try {
            userService.addUser(user);
        } catch (UserExistsException e) {
            modelMap.addAttribute("userExistsError", e.getMessage());
            logger.error(e.getStackTrace());
            return ADMIN_USER_MANAGEMENT_PAGE;
        }
        return "redirect:/admin/users";
    }

}
