package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      Car car = user.getCar();

      if (car != null) {
         sessionFactory.getCurrentSession().save(car);
      }

      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public Optional<User> getUserByCar(String name, int series) {
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(
              "select u " +
                 "from User u " +
                 "join u.car c " +
                 "where c.name = :name and c.series = :series", User.class);
      query.setParameter("name", name);
      query.setParameter("series", series);

      return query.getResultStream().findFirst();
   }
}
