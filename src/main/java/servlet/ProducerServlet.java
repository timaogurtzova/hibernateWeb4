package servlet;

import model.Car;
import service.CarService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/producer")
public class ProducerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean takeOrNot = false;
        String brand = req.getParameter("brand");
        String model = req.getParameter("model");
        String licensePlate = req.getParameter("licensePlate");
        String price = req.getParameter("price");

        try {
            Long priceCar = Long.parseLong(price);
            Car car = new Car(brand, model, licensePlate, priceCar);
            takeOrNot = CarService.getInstance().takeCar(car);
        }catch (NumberFormatException e ){
            //
        }

        if (takeOrNot){
            resp.getWriter().write("Take a car");
            resp.setStatus(200);
        }
        else {
            resp.getWriter().write("Don't take the car");
            resp.setStatus(403);
        }
    }

}
