package dispatchApp.dao;


import dispatchApp.model.Authorities;
import dispatchApp.model.Carrier;
import dispatchApp.model.Order;
import dispatchApp.utils.Constants;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
//            session.getTransaction().commit();
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
            String sqlDrone = new String("SELECT id FROM Carrier WHERE status = 'Available' AND carrierType = 'DRONE'");
            String sqlRobot = new String("SELECT id FROM Carrier WHERE status = 'Available' AND carrierType = 'ROBOT'");

            String sql = carrierType == "DRONE" ? sqlDrone : sqlRobot;
            Query query = session.createQuery(sql);
            List list = query.list();
//            session.getTransaction().commit();
            //case1: no available carrier
            if (list.isEmpty()) {
                return null;
            }
            //case2: found available carrier
            Integer id = (Integer) list.get(0);
            return id;
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

    public void addCarrier(Carrier carrier) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(carrier);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void addDummyCarrier() {
        //initialize drone


        for (int i = 0; i < 10; i++) {
            Carrier carrier = new Carrier();
            carrier.setCarrierType("DRONE");
            carrier.setStatus("Available");
            carrier.setStationId(0);
            carrier.setSpeed(Constants.DEFAULT_DRONE_SPEED);
            addCarrier(carrier);
        }
        //initialize robot


        for (int i = 0; i < 10; i++) {
            Carrier carrier = new Carrier();
            carrier.setCarrierType("ROBOT");
            carrier.setStatus("Available");
            carrier.setStationId(0);
            addCarrier(carrier);
        }
    }
    public void updateCarrier(Carrier carrier) {
    	Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.saveOrUpdate(carrier);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}
    }
}
