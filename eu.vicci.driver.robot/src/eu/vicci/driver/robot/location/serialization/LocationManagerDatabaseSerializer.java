package eu.vicci.driver.robot.location.serialization;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.google.gson.reflect.TypeToken;

import eu.vicci.driver.robot.exception.TimeoutException;
import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.driver.robot.location.NamedLocationDatabaseWrapper;
import eu.vicci.driver.robot.util.RosServiceCaller;
import eu.vicci.driver.robot.util.rosmsg.MapMsg;
import eu.vicci.driver.robot.util.rosmsg.ServiceResponseMsg;
import eu.vicci.driver.robot.util.rosmsg.StringMsg;


public class LocationManagerDatabaseSerializer extends
		LocationManagerSerializer {

	private static final String DB_SERVER_IP = "192.168.1.4";
	
	List<NamedLocation> locationsDB = new LinkedList<NamedLocation>();;
	
	private String mapID="";
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<NamedLocation> load() {
		
		mapID = loadMapName();
		
		List<NamedLocation> siteList = new LinkedList<NamedLocation>();
		Configuration configuration = new Configuration();
		configuration.configure("/hibernate.cfg.xml");
		configuration.getProperties().setProperty("hibernate.connection.url", "jdbc:mysql://"+DB_SERVER_IP+"/locations");
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
		
		Session session = sessionFactory.openSession();
		
		try {
			session.beginTransaction();
			
			String SQL_Query = "FROM "+ NamedLocationDatabaseWrapper.class.getSimpleName();
			Query query = session.createQuery(SQL_Query);
			List locations = query.list();
			Iterator itr = locations.iterator();
			while (itr.hasNext()) {
				NamedLocationDatabaseWrapper loc = (NamedLocationDatabaseWrapper) itr
						.next();
				if(loc.getMapID().equals(mapID)){
					System.out.println("equals");
					NamedLocation nl = deserializeLocation(loc.getDescription());
					siteList.add(nl);
				}
			}
			session.getTransaction().commit();
			
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (session != null)
				sessionFactory.close();
		}
		
		locationsDB.clear();
		for(NamedLocation n : siteList){
			locationsDB.add(n);
		}
		
		return siteList;
	}
	
	@SuppressWarnings("unchecked")
	public String loadMapName(){
		Type msgType = new TypeToken<ServiceResponseMsg<StringMsg>>(){}.getType();
		RosServiceCaller caller = new RosServiceCaller(DB_SERVER_IP, msgType);
		ServiceResponseMsg<StringMsg> msg;
		try {
			msg = (ServiceResponseMsg<StringMsg>) caller.call("/get_map_name");
			return msg.getValues().getString();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@Override
	public void save(List<NamedLocation> locations) {
		Configuration configuration = new Configuration();
		configuration.configure("/hibernate.cfg.xml");
		configuration.getProperties().setProperty("hibernate.connection.url", "jdbc:mysql://"+DB_SERVER_IP+"/locations");
		
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
	
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		// add or update Locations in DB
		for (NamedLocation n : locations) {
			NamedLocationDatabaseWrapper loc = new NamedLocationDatabaseWrapper();
			loc.setName(n.getName());
			loc.setDescription(serializeLocation(n));
			loc.setMapID(mapID);
			session.saveOrUpdate(loc);
		}
		
		// remove Locations from DB
		for(NamedLocation locationInDB : locationsDB){
			boolean contains = false;
			for(NamedLocation n : locations){
				if(locationInDB.getName().equals(n.getName())) {
					contains = true;
					break;
				}
			}
			if(!contains) {
				NamedLocationDatabaseWrapper loc = new NamedLocationDatabaseWrapper();
				loc.setName(locationInDB.getName());
				session.delete(loc);
			}
		}
		
		locationsDB.clear();
		for(NamedLocation n : locations){
			locationsDB.add(n);
		}
		
		session.getTransaction().commit();
		sessionFactory.close();
	}

}
