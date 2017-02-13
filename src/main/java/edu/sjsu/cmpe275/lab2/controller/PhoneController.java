package edu.sjsu.cmpe275.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sjsu.cmpe275.lab2.domain.Address;
import edu.sjsu.cmpe275.lab2.domain.Phone;
import edu.sjsu.cmpe275.lab2.domain.User;
import edu.sjsu.cmpe275.lab2.repositories.PhoneRepository;
import edu.sjsu.cmpe275.lab2.repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
public class PhoneController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PhoneRepository phoneRepository;

    /* Get phone */
    @RequestMapping(value = "/phone/{id}", method = RequestMethod.GET)
    public String viewPhone(@PathVariable String id,
                            @RequestParam(value = "json", required = false) String json,
                            Model model,
                            HttpServletResponse response) throws JsonProcessingException {

        id = id.toUpperCase();
        // Find the phone via id
        Phone phone = phoneRepository.findOne(id);

        // Return 404 if the phone does not exists.
        if (phone == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("id", id);
            model.addAttribute("dataType", "phone");

            return "notfound";
        }

        // Decide the response format, json vs html
        if (json != null && json.equals("true")) { // Json
            /*
            // Convert the phone object to json string
            ObjectMapper mapper = new ObjectMapper();
            String phoneJson = mapper.writeValueAsString(phone);
            */
            // Setup the json response.
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("id", phone.getId());
            jsonResponse.put("number", phone.getNumber());
            jsonResponse.put("description", phone.getDescription());
            // Helper for address.
            JSONObject jsonAddress = new JSONObject();
            jsonAddress.put("street", phone.getAddress().getStreet());
            jsonAddress.put("city", phone.getAddress().getCity());
            jsonAddress.put("State", phone.getAddress().getState());
            jsonAddress.put("zip", phone.getAddress().getZip());
            jsonResponse.put("address", jsonAddress);
            // Helper for user.
            JSONArray jsonUsers = new JSONArray();
            for (User user : phone.getUsers()) {
                JSONObject jsonUser = new JSONObject();
                jsonUser.put("id", user.getId());
                jsonUser.put("firstname", user.getFirstname());
                jsonUser.put("lastname", user.getLastname());
                jsonUsers.put(jsonUser);
            }
            jsonResponse.put("users", jsonUsers);

            // Return json response.
            try {
//                response.getWriter().write(phoneJson);
                response.getWriter().write(jsonResponse.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        } else { // html
            // Find the phone via id and attach the object to the view.
            model.addAttribute("phone", phone);

            return "phoneedit";
        }

    }

    /* Update Phone */
    @RequestMapping(value = "/phone/{id}", method = RequestMethod.POST)
    public String updatePhone(@PathVariable String id,
                              @RequestParam(value = "formNum", required = false) String formNum,
                              @RequestParam(value = "formDesc", required = false) String formDesc,
                              @RequestParam(value = "formStreet", required = false) String formStreet,
                              @RequestParam(value = "formCity", required = false) String formCity,
                              @RequestParam(value = "formState", required = false) String formState,
                              @RequestParam(value = "formZip", required = false) String formZip,
                              @RequestParam(value = "number", required = false) String paramNum,
                              @RequestParam(value = "description", required = false) String paramDesc,
                              @RequestParam(value = "street", required = false) String paramStreet,
                              @RequestParam(value = "city", required = false) String paramCity,
                              @RequestParam(value = "state", required = false) String paramState,
                              @RequestParam(value = "zip", required = false) String paramZip,
                              //@RequestParam HashMap<String, String > allRequestParams,
                              @RequestParam(value="users[]") String[] users,
                              Model model) {

        id = id.toUpperCase();
        // Find the phone
        Phone phone = phoneRepository.findOne(id);

        // Setup Address object for insert
        Address address = new Address();

        // Updates from query string
        if (paramNum != null) {
            if (phone == null) { // New user
                phone = new Phone();
                phone.setId(id);
            }
            phone.setNumber(paramNum);
            phone.setDescription(paramDesc);
            address.setStreet(paramStreet);
            address.setCity(paramCity);
            address.setState(paramState);
            address.setZip(paramZip);

            //if (allRequestParams.keySet().contains("users[]")) { // user data is attached
                //User user = new User();
                User user = null;
                //for (Map.Entry<String, String> entry : allRequestParams.entrySet()) {
                //System.out.println(entry.getKey() + " " + entry.getValue());
                //if (entry.getKey().equals("users[]")) {
                if(users!=null){
                    for(int i=0; i < users.length; i++){
                        if(userRepository.findOne(users[i])!=null){ //user exists
                            user = userRepository.findOne(users[i]);
                            phone.getUsers().add(user);
                            user.getPhones().add(phone);
                            userRepository.save(user);
                        }
                    }
                }


                    //}
                    /*if (entry.getKey().equals("userid")) {
                        user.setId(entry.getValue());
                    }
                    if (entry.getKey().equals("firstname")) {
                        user.setFirstname(entry.getValue());
                    }
                    if (entry.getKey().equals("lastName")) {
                        user.setFirstname(entry.getValue());
                    }*/
               // }
            //}
        } else { // Updates from html form
            phone.setNumber(formNum);
            phone.setDescription(formDesc);
            address.setStreet(formStreet);
            address.setCity(formCity);
            address.setState(formState);
            address.setZip(formZip);
        }
        phone.setAddress(address);
        phoneRepository.save(phone);

        return "redirect:/phone/" + id;
    }

    /* Delete Phone */
    @RequestMapping(value = "/phone/{id}", method = RequestMethod.DELETE)
    public String deletePhone(@PathVariable String id,
                              HttpServletResponse response,
                              Model model) {
        id = id.toUpperCase();
        // Find the phone
        Phone phone = phoneRepository.findOne(id);

        // Phone does not exist.
        if (phone == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("id", id);

            return "notfound";
        }

        // No active users
        if (phone.getUsers() == null || phone.getUsers().size() == 0) {
            phoneRepository.delete(id);

            return "redirect:/phone";
        }

        // User assigned to the phone, cannot delete
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            response.getWriter().write("Cannot delete phones with active users: Status "
                    + HttpStatus.BAD_REQUEST.value());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* Create Phone Form */
    @RequestMapping(value = "/phone", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String newPhone() {

        return "phonecreate";
    }

    /* Delete user from phone */
    @RequestMapping(value = "/phone/user/{uid}/{pid}", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteUser(@PathVariable("uid") String uid,
                             @PathVariable("pid") String pid) {
        uid = uid.toUpperCase();
        pid = pid.toUpperCase();
        userRepository.delete(uid);

        return "redirect:/phone/" + pid;
    }

    /* Add user for phone */
    @RequestMapping(value = "/phone/user/{pid}", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("pid") String pid,
                             @RequestParam("addId") String addId,
                             @RequestParam("addFirst") String addFirst,
                             @RequestParam("addLast") String addLasst) {

        pid = pid.toUpperCase();
        // Find phone
        Phone phone = phoneRepository.findOne(pid);

        // Find user
        User user = userRepository.findOne(addId);

        // New User
        if (user == null) {
            user = new User();
            user.setId(addId);
        }

        // Existing user
        user.setFirstname(addFirst);
        user.setLastname(addLasst);

        // Add user to the phone
        phone.getUsers().add(user);

        // Add phone ot the user
        user.getPhones().add(phone);

        // Save
        phoneRepository.save(phone);
        userRepository.save(user);

        return "redirect:/phone/" + pid;
    }

    /* Create Phone */
    @RequestMapping(value = "/phone", method = RequestMethod.POST)
    public String createPhone(@RequestParam("id") String id,
                              @RequestParam("number") String number,
                              @RequestParam("description") String description,
                              @RequestParam("street") String street,
                              @RequestParam("city") String city,
                              @RequestParam("state") String state,
                              @RequestParam("zip") String zip,
                              @RequestParam("uids") String uids,
                              @RequestParam("uFirsts") String uFirsts,
                              @RequestParam("uLasts") String uLasts,
                              HttpServletResponse response,
                              Model model) {

        id = id.toUpperCase();
        // Check for existing data
        Phone phone = phoneRepository.findOne(id);

        // New phone
        if (phone == null) {

            phone = new Phone();
            phone.setId(id);
        }

        // Update the properties
        phone.setNumber(number);
        phone.setDescription(description);
        Address address = new Address(); // Address
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZip(zip);
        phone.setAddress(address);
        List<String> uIdList = Arrays.asList(uids.split("\\s*,\\s*"));
        List<String> uFirstList = Arrays.asList(uFirsts.split("\\s*,\\s*"));
        List<String> uLastList = Arrays.asList(uLasts.split("\\s*,\\s*"));
        for (int i = 0; i < uIdList.size(); i++) {
            User user = new User();
            user.setId(uIdList.get(i));
            user.setFirstname(uFirstList.get(i));
            user.setLastname(uLastList.get(i));
            user.setAddress(address);

            user.getPhones().add(phone);
            phone.getUsers().add(user);
            userRepository.save(user);
        }
        phoneRepository.save(phone);

        return "redirect:/phone/" + id;
    }
}
