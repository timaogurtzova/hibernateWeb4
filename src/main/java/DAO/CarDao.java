package DAO;

import model.Car;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import java.util.List;

public class CarDao {

    private Session session;

    public CarDao(Session session) {
        this.session = session;
    }

    //Покупатели могут запросить список имеющихся  машин
    public List<Car> getAllCarsDAO() throws HibernateException{
        List<Car> cars = null;
        Query query = session.createQuery("SELECT car FROM Car car");
        cars = query.list();
        return cars;
    }

    /*Покупатели могут, передав параметры марки машины, названия и госномера, купить машину,
    этот метод нужен, чтобы узнать, есть ли у нас машина с такими параметрами(нужна только 1 шт)
    */
    public Car getCarWithParameterDAO(String brand, String model, String licensePlate) throws HibernateException{
        Car car = null;
        Query query = session.createQuery(
                "SELECT cars FROM Car cars WHERE cars.brand =:carsBrand AND cars.model =:carsModel AND cars.licensePlate =:licensePlate"
        )
                .setParameter("carsBrand", brand)
                .setParameter("carsModel", model)
                .setParameter("licensePlate", licensePlate)
                .setMaxResults(1);
        car = (Car)query.uniqueResult();
        return car ;
    }

    /*Когда кто-то покупает машину, в бд её нужно удалить.
    Удаляем по id т.к., если мы ранее нашли нужную машину, она сейчас в состоянии MANAGED
    */
    public void deleteCarById(Long id) throws HibernateException {
        Query query = session.createQuery("DELETE FROM Car WHERE id =:param");
        query.setParameter("param", id);
        query.executeUpdate();
    }

    //Машин одной марки может быть не более 10-ти штук, перед добавлением машины в БД проверяем, сколько таких
    public long getCountCarsThisBrand(String brand){
        Query query = session.createQuery("SELECT COUNT (car) FROM Car car WHERE car.brand =:parameter")
                .setParameter("parameter", brand);
        long result = (Long)query.uniqueResult();
        return result;
    }

    //Новые поступления происходят в течение дня
    public void addCar(Car car) throws HibernateException{
        session.persist(car);
    }

    //удалить все машины
    public void deleteAllCars(){
     Query query = session.createQuery("DELETE FROM Car");
        query.executeUpdate();
    }

}
