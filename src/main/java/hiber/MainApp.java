package hiber;

import hiber.config.AppConfig;
import hiber.model.Car;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainApp {
   public static void main(String[] args) throws SQLException {
      AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);

      UserService userService = context.getBean(UserService.class);

      userService.add(new User("User1", "Lastname1", "user1@mail.ru"));
      userService.add(new User("User2", "Lastname2", "user2@mail.ru", new Car("Car1", 111)));
      userService.add(new User("User3", "Lastname3", "user3@mail.ru"));
      userService.add(new User("User4", "Lastname4", "user4@mail.ru", new Car("Car2", 222)));
      userService.add(new User("User5", "Lastname5", "user5@mail.ru", new Car("Car3", 333)));

      List<User> users = userService.listUsers();
      for (User user : users) {
         System.out.println("Id = " + user.getId());
         System.out.println("First Name = " + user.getFirstName());
         System.out.println("Last Name = " + user.getLastName());
         System.out.println("Email = " + user.getEmail());
         user.getCar().ifPresent(car -> System.out.println("Car = " + car));
         System.out.println();
      }

      Map<String, Integer> searchCars = new HashMap<>();
      searchCars.put("Car2", 222);
      searchCars.put("Car3", 331);
      searchCars.put("Car1", 111);
      searchCars.put("Car4", 111);

      searchCars.keySet().stream()
              .peek(key -> System.out.println("Searching for user with car '" + key + ", " + searchCars.get(key) + "'..."))
              .map(key -> userService.getUserByCar(key, searchCars.get(key)))
              .forEach(optUser ->
                      optUser.ifPresentOrElse(user -> System.out.printf("...found user '%s %s'%n%n",
                              user.getFirstName(), user.getLastName()),
                      () -> System.out.printf("...user not found%n%n")
              ));

      context.close();
   }
}
