package service;

import DAO.CarDao;
import model.Car;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.DBHelper;
import java.util.List;

public class CarService {

    private static CarService carService;

    private SessionFactory sessionFactory;

    private CarService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static CarService getInstance() {
        if (carService == null) {
            carService = new CarService(DBHelper.getSessionFactory());
        }
        return carService;
    }

    //Покупатели могут запросить список имеющихся  машин
    public List<Car> getAllCars(){
        Session session = sessionFactory.openSession();
        List<Car> cars = new CarDao(session).getAllCarsDAO();
        session.close();
        return cars;
    }

    /*Если машину хотят купить, то
        1. Надо узнать, есть ли у нас такая машина
        2. Если мы её нашли, то она будет в состоянии managed, удаляем из табл.cars по id
        3. Отразить изменения в табл. daily_reports +soldCars и +earnings
         */
    public boolean buyCar(String brand, String model, String licensePlate){
        boolean rezult = false;
        Session session = null;
        Transaction transaction = null;
        Car car = null;

        try {
            session = sessionFactory.openSession();
            CarDao carDao = new CarDao(session);
            transaction = session.beginTransaction();

            car = carDao.getCarWithParameterDAO(brand, model, licensePlate);
            if (car != null){
                Long carId = car.getId();
                Long carPrice = car.getPrice();
                carDao.deleteCarById(carId);
                DailyReportService.getInstance().updateDailyReportActual(carPrice);
                transaction.commit();
                rezult = true;
            }
            session.close();
        }catch (HibernateException e){
            transaction.rollback();
        }

        return rezult;
    }

    /*Новые поступления происходят в течение дня
    1. Машин одной марки может быть не более 10ти штук
    2. Если принимаем машину, то добавляем её в табл. cars
     */
    public boolean takeCar (Car car){
        boolean rezult = false;
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            CarDao carDao = new CarDao(session);
            transaction = session.beginTransaction();

            long carCountBrand = carDao.getCountCarsThisBrand(car.getBrand());
            if (carCountBrand <10){
                carDao.addCar(car);
                rezult = true;
                transaction.commit();
            }
            session.close();
        }catch (HibernateException e){
            transaction.rollback();
        }

        return rezult;
    }

    public void deleteAllCar(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        new CarDao(session).deleteAllCars();
        transaction.commit();
        session.close();
    }

}
