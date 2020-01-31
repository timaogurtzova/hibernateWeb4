package servlet;

import com.google.gson.Gson;
import model.DailyReport;
import service.CarService;
import service.DailyReportService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/report/*")
public class DailyReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo().contains("all")) {
            List<DailyReport> dailyReport = DailyReportService.getInstance().getAllDailyReports();
            Gson gson = new Gson();
            String json = gson.toJson(dailyReport);
            resp.getWriter().write(json);
            resp.setStatus(200);
        } else if (req.getPathInfo().contains("last")) {
            DailyReport dailyReportLast = DailyReportService.getInstance().getLastReport();
            Gson gson = new Gson();
            String json = gson.toJson(dailyReportLast);
            resp.getWriter().write(json);
            resp.setStatus(200);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
          CarService.getInstance().deleteAllCar();
          DailyReportService.getInstance().deleteAllDaily();
            resp.setStatus(200);
        }
    }

}