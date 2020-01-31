package service;

import DAO.DailyReportDao;
import model.DailyReport;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.DBHelper;

import java.util.List;

public class DailyReportService {

    private static DailyReportService dailyReportService;

    private SessionFactory sessionFactory;

    private DailyReportService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static DailyReportService getInstance() {
        if (dailyReportService == null) {
            dailyReportService = new DailyReportService(DBHelper.getSessionFactory());
        }
        return dailyReportService;
    }

    //Начальство может потребовать отчеты за все дни
    public List<DailyReport> getAllDailyReports() {
        Session session = sessionFactory.openSession();
        List<DailyReport> reports = new DailyReportDao(session).getAllDailyReport();
        session.close();
        return reports;
    }

   //Когда кто-то покупает машину, нужно отразить изменения в текущем дне
    public void updateDailyReportActual(Long priceCar) throws HibernateException {
        Session session = null;
        Transaction transaction = null;
        DailyReport dailyReport = null;

        try {
            session = sessionFactory.openSession();
            DailyReportDao dao = new DailyReportDao(session);
            transaction = session.beginTransaction();

            dailyReport = dao.getTodayDailyReportDAO();
            if (dailyReport == null){
                dailyReport = dao.createDailyReportDAO();
            }
            dao.updateDailyReport(dailyReport, priceCar);
            transaction.commit();
        }catch (HibernateException e){
            transaction.rollback();
            throw new HibernateException("Problem daily report");
        }
    }

    public DailyReport getLastReport() {
        Session session = sessionFactory.openSession();
        DailyReportDao dao = new DailyReportDao(session);
        DailyReport dr = dao.getLastDailyReportDAO();
        if (dr == null){
            dr = new DailyReport();
        }
        session.close();
        return dr;
    }

    //создаем новый день
    public void newDay(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        DailyReportDao dao = new DailyReportDao(session);
        DailyReport dailyReport = dao.createDailyReportDAO();
        transaction.commit();
        session.close();
    }

    public void deleteAllDaily(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        new DailyReportDao(session).deleteAllDailyDAO();
        transaction.commit();
        session.close();
    }
}
