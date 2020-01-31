package DAO;

import model.DailyReport;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import java.util.List;

public class DailyReportDao {

    private Session session;

    public DailyReportDao(Session session) {
        this.session = session;
    }

    //Отчеты за все дни
    public List<DailyReport> getAllDailyReport()throws HibernateException {
        List<DailyReport> dailyReports = session.createQuery("FROM DailyReport").list();
        return dailyReports;
    }

    //Обновляем earnings и soldCars
    public void updateDailyReport(DailyReport dailyReport, Long carPrice) throws HibernateException{
        Long updatePrice = null;
        Long updateCount = null;

        updatePrice = dailyReport.getEarnings() + carPrice;
        if (carPrice > 0){
            updateCount = dailyReport.getSoldCars() + 1;
            dailyReport.setSoldCars(updateCount);
        }
        dailyReport.setEarnings(updatePrice);
    }

    //Отчет на сегодня. Это последний созданный отчет. Его дата создания самая поздняя.
    public DailyReport getTodayDailyReportDAO() throws HibernateException {
        DailyReport dr = null;

        Query query = session.createQuery("SELECT dr FROM DailyReport dr ORDER BY dr.createdDate DESC")
                .setFirstResult(0)
                .setMaxResults(1);
        dr = (DailyReport)query.uniqueResult();
        return dr;
    }

    //Отчет за прошедший день идёт за сегодняшним, если сортировка по убыванию
    public DailyReport getLastDailyReportDAO()throws HibernateException{
        DailyReport dr = null;

        Query query = session.createQuery("SELECT dr FROM DailyReport dr ORDER BY dr.createdDate DESC")
                .setFirstResult(1)
                .setMaxResults(1);
        dr = (DailyReport)query.uniqueResult();
        return dr;
    }

    //Когда меняем день, создается новая строка в табл.отчетов
    public DailyReport createDailyReportDAO() throws HibernateException{
        DailyReport dailyReport = new DailyReport();
        session.persist(dailyReport);
        return dailyReport;
    }

    public void deleteAllDailyDAO(){
      Query query = session.createQuery("DELETE FROM DailyReport");
        query.executeUpdate();
    }
}
