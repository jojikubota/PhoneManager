package edu.sjsu.cmpe275.lab2.repositories;

import edu.sjsu.cmpe275.lab2.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by joji on 11/5/16.
 */


public interface UserRepository extends CrudRepository<User, String> {
}
