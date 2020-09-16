package hiber.dao;

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
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("FROM User u JOIN FETCH u.car");
      return query.getResultList();
   }

   @Override
   public Optional<User> getUserByCar(String name, int series) {
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(
              "SELECT u " +
                 "FROM User u " +
                 "JOIN u.car c " +
                 "WHERE c.name = :name AND c.series = :series", User.class);
      query.setParameter("name", name);
      query.setParameter("series", series);

      return query.getResultStream().findFirst();
   }
}