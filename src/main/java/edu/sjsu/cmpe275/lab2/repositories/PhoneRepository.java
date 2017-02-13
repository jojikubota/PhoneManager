package edu.sjsu.cmpe275.lab2.repositories;

import edu.sjsu.cmpe275.lab2.domain.Phone;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * Created by joji on 11/5/16.
 */

public interface PhoneRepository extends CrudRepository<Phone, String> {
}
