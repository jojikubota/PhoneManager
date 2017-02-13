package edu.sjsu.cmpe275.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import edu.sjsu.cmpe275.lab2.domain.Address;
import edu.sjsu.cmpe275.lab2.domain.Phone;
import edu.sjsu.cmpe275.lab2.domain.User;
import edu.sjsu.cmpe275.lab2.repositories.PhoneRepository;
import edu.sjsu.cmpe275.lab2.repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by joji on 11/5/16.
 */

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PhoneRepository phoneRepository;

    /* Get User */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String viewUser(@PathVariable String id,
                           @RequestParam(value = "json", required = false) String json,
                           Model model,
                           HttpServletResponse response) throws JsonProcessingException {

        id = id.toUpperCase();
        // Find the user via id
        User user = userRepository.findOne(id);

        // Return 404 if the user does not exists.
        if (user == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("id", id);
            model.addAttribute("dataType", "user");

            return "notfound";
        }

        // Decide the response format, json vs html
        if (json != null && json.equals("true")) { // Json
            /*
            // Convert the user object to json string
            ObjectMapper mapper = new ObjectMapper();
            String userJson = mapper.writeValueAsString(user);
            */

            // Setup the json response.
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("id", user.getId());
            jsonResponse.put("firstname", user.getFirstname());
            jsonResponse.put("lastname", user.getLastname());
            jsonResponse.put("title", user.getTitle());
            // Helper for address.
            JSONObject jsonAddress = new JSONObject();
            jsonAddress.put("street", user.getAddress().getStreet());
            jsonAddress.put("city", user.getAddress().getCity());
            jsonAddress.put("State", user.getAddress().getState());
            jsonAddress.put("zip", user.getAddress().getZip());
            jsonResponse.put("address", jsonAddress);
            // Helper for phone.
            JSONArray jsonPhones = new JSONArray();
            for (Phone phone : user.getPhones()) {
                JSONObject jsonPhone = new JSONObject();
                jsonPhone.put("id", phone.getId());
                jsonPhone.put("number", phone.getNumber());
                jsonPhone.put("description", phone.getDescription());
                jsonPhones.put(jsonPhone);
            }
            jsonResponse.put("phones", jsonPhones);

            // Return json response.
            try {
//                response.getWriter().write(userJson);
                response.getWriter().write(jsonResponse.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        } else { // html
            // Find the user via id and attach the object to the view.
            model.addAttribute("user", user);

            return "useredit";
        }

    }

    /* Update User */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    public String updateUser(@PathVariable String id,
                             @RequestParam(value = "formFirst", required = false) String formFirst,
                             @RequestParam(value = "formLast", required = false) String formLast,
                             @RequestParam(value = "formTitle", required = false) String formTitle,
                             @RequestParam(value = "formStreet", required = false) String formStreet,
                             @RequestParam(value = "formCity", required = false) String formCity,
                             @RequestParam(value = "formState", required = false) String formState,
                             @RequestParam(value = "formZip", required = false) String formZip,
                             @RequestParam(value = "firstname", required = false) String paramFirst,
                             @RequestParam(value = "lastname", required = false) String paramLast,
                             @RequestParam(value = "title", required = false) String paramTitle,
                             @RequestParam(value = "street", required = false) String paramStreet,
                             @RequestParam(value = "city", required = false) String paramCity,
                             @RequestParam(value = "state", required = false) String paramState,
                             @RequestParam(value = "zip", required = false) String paramZip,
                             Model model) {

        id = id.toUpperCase();
        // Find the user
        User user = userRepository.findOne(id);

        // Setup Address object for insert
        Address address = new Address();

        // Updates from query string
        if (paramFirst != null) {
            if (user == null) { // New user
                user = new User();
                user.setId(id);
            }
            user.setFirstname(paramFirst);
            user.setLastname(paramLast);
            user.setTitle(paramTitle);
            address.setStreet(paramStreet);
            address.setCity(paramCity);
            address.setState(paramState);
            address.setZip(paramZip);
        } else { // Updates from html form
            user.setFirstname(formFirst);
            user.setLastname(formLast);
            user.setTitle(formTitle);
            address.setStreet(formStreet);
            address.setCity(formCity);
            address.setState(formState);
            address.setZip(formZip);
        }
        user.setAddress(address);
        userRepository.save(user);

        return "redirect:/user/" + id;
    }

    /* Delete User */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable String id,
                             HttpServletResponse response,
                             Model model) {

        id = id.toUpperCase();
        // User does not exisits.
        if (userRepository.findOne(id) == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("id", id);

            return "notfound";
        }

        userRepository.delete(id);

        return "redirect:/user";
    }

    /* Create User Form */
    @RequestMapping(value = "/user", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String newUser() {

        return "usercreate";
    }

    /* Create User */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String createUser(@RequestParam String id,
                             @RequestParam("fName") String fName,
                             @RequestParam("lName") String lName,
                             @RequestParam("title") String title,
                             @RequestParam("street") String street,
                             @RequestParam("city") String city,
                             @RequestParam("state") String state,
                             @RequestParam("zip") String zip,
                             @RequestParam("pids") String pids,
                             @RequestParam("pnums") String pnums,
                             @RequestParam("pdescs") String pdescs,
                             HttpServletResponse response,
                             Model model) {

        id = id.toUpperCase();
        // Check for existing data
        User user = userRepository.findOne(id);

        // New user
        if (user == null) {

            user = new User();
        }

        // Update the properties
        user.setId(id);
        user.setFirstname(fName);
        user.setLastname(lName);
        user.setTitle(title);
        Address address = new Address(); // Address
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZip(zip);
        user.setAddress(address);
        List<String> pIdList = Arrays.asList(pids.split("\\s*,\\s*"));
        List<String> pNumList = Arrays.asList(pnums.split("\\s*,\\s*"));
        List<String> pDescList = Arrays.asList(pdescs.split("\\s*,\\s*"));
        List<Phone> phones = new ArrayList<Phone>(); // Phones
        for (int i = 0; i < pIdList.size(); i++) {
            Phone phone = new Phone();
            phone.setId(pIdList.get(i));
            phone.setNumber(pNumList.get(i));
            phone.setDescription(pDescList.get(i));
            phone.setAddress(address);

            phones.add(phone);
            phoneRepository.save(phone);
        }
        user.setPhones(phones);
        userRepository.save(user);

        return "redirect:/user/" + id;
    }
}
