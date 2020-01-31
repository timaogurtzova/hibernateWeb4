package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "daily_reports")
public class DailyReport {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "earnings")
    private Long earnings;

    @Column(name = "soldCars")
    private Long soldCars;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date createdDate;

    public DailyReport() {
        this(0L, 0L);
    }

    public DailyReport(Long earnings, Long soldCars) {
        this.earnings = earnings;
        this.soldCars = soldCars;
    }

    public Date getDate() {
        return createdDate;
    }

    public Long getId() {
        return id;
    }

    public Long getEarnings() {
        return earnings;
    }

    public void setEarnings(Long earnings) {
        this.earnings = earnings;
    }

    public Long getSoldCars() {
        return soldCars;
    }

    public void setSoldCars(Long soldCars) {
        this.soldCars = soldCars;
    }

}
