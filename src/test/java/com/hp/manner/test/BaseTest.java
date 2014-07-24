package com.hp.manner.test;


import com.hp.manner.config.AppConfig;
import com.hp.manner.config.WebMvcConfig;
import com.hp.manner.model.Item;
import com.hp.manner.model.User;
import com.hp.manner.repository.ItemRepository;
import com.hp.manner.repository.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { AppConfig.class, WebMvcConfig.class })
public class BaseTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void resetMongoDB() {
        userRepository.deleteAll();
        itemRepository.deleteAll();

        User user1 = new User();
        user1.setFirstName("Yao");
        user1.setLastName("Xiao");
        user1.setEmail("yao.xiao@hp.com");
        user1.setCommonName("Jason");
        user1.setPassword(encoder.encode("123456"));
        user1.setRole(User.Role.ADMIN);

        Map<String, String> groups1 = new HashMap<>();
        groups1.put("BMI", "Lead");
        groups1.put("Watson", "Member");
        user1.setGroups(groups1);

        User user2 = new User();
        user2.setFirstName("John");
        user2.setLastName("Smith");
        user2.setEmail("john.smith@example.com");
        user2.setCommonName("John");
        user2.setPassword(encoder.encode("123456"));
        user2.setRole(User.Role.USER);

        Map<String, String> groups2 = new HashMap<>();
        groups2.put("BMI", "Member");
        user2.setGroups(groups2);

        userRepository.save(user1);
        userRepository.save(user2);

        User user = userRepository.findByEmail("yao.xiao@hp.com");

        Item item1 = new Item();
        item1.setContent("Learn Spring Data Mongodb");
        item1.setPriority(Item.Priority.HIGH);
        item1.setDueDate(new Date());// or new Date(1404172800000L) 2014-07-01 00:00:00
        item1.setStatus(Item.Status.NEW);
        item1.setPercentage(0);
        item1.setOwner(user);

        Item item2 = new Item();
        item2.setContent("Learn Spring Security");
        item2.setPriority(Item.Priority.MEDIUM);
        item2.setDueDate(new Date()); // or new Date(1404172800000L) 2014-07-01 00:00:00
        item2.setStatus(Item.Status.WORK_IN_PROGRESS);
        item2.setPercentage(50);
        item2.setOwner(user);

        itemRepository.save(item1);
        itemRepository.save(item2);
    }

}
