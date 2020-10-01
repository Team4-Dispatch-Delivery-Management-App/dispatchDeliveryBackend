package dispatchApp.dao;


import dispatchApp.model.Carrier;
import dispatchApp.model.Order;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

@Repository
public class CarrierDao {

    @Autowired
    private SessionFactory sessionFactory;
    //need to add a fixed amount of drone and robot in db


    public String getStatus(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Carrier carrier = (Carrier) session.get(Carrier.class, id);
            session.getTransaction().commit();
            String status  = carrier.getStatus();
            session.close();

            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStatus(int id, String status) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Carrier carrier = (Carrier) session.get(Carrier.class, id);
            session.getTransaction().commit();
            carrier.setStatus(status);
            session.update(carrier);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Integer getAvailableCarrierId(String carrierType) {
        try (Session session = sessionFactory.openSession()) {
            String sql = String.format("SELECT * From Carrier WHERE status = %s AND carrierType = %s", "Available", carrierType);
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity(Carrier.class);
            List list = query.list();
            Carrier carrier = null;
            for (Iterator iterator = list.iterator(); iterator.hasNext();){
                carrier = (Carrier) iterator.next();
                //set status to busy to lock 10 minutes
                //add into heap
                break;
            }
            session.getTransaction().commit();
            return carrier.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Carrier getCarrierById(Integer carrierId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Carrier carrier = (Carrier) session.get(Carrier.class, carrierId);
            session.getTransaction().commit();
            return carrier;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
