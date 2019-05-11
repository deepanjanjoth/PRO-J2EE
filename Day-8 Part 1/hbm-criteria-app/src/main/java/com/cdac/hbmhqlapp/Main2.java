package com.cdac.hbmhqlapp;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import com.cdac.hbmhqlapp.dto.Employee;

public class Main2 {
	   private static SessionFactory sf;
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.configure();
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
		ssrb.applySettings(cfg.getProperties());
		ServiceRegistry sr = ssrb.build();
		sf = cfg.buildSessionFactory(sr);
		
		boolean flag = true;
		Scanner sc = new Scanner(System.in);
	    do {
	    	System.out.println("0 to exit");
	    	System.out.println("1 to select all");
	    	System.out.println("2 to select order by");
	    	System.out.println("3 to select where");
	    	System.out.println("4 to select name");
	    	System.out.println("5 to select name and salary");
	    	System.out.println("6 to select distinct");
	    	System.out.println("7 to select sum");
	    	System.out.println("8 to select sum group by");
	    	System.out.println("9 to select sum group by having");
	    	System.out.println("10 to select name like");
	    	System.out.println("11 to select and");
	    	byte ch = sc.nextByte();
	    	switch (ch) {
			case 0 :
				flag = false;
				break;
			case 1 :
				selectAll();
				break;
			case 2 :
				selectAllOrderBy();
				break;	
			case 3 :
				selectAllWhere();
				break;	
			case 4 :
				selectNames();
				break;
			case 5 :
				selectNameAndSalary();
				break;	
			case 6 :
				selectDistinct();
				break;	
			case 7 :
				selectSum();
				break;
			case 8 :
				selectSumGroupBy();
				break;	
			case 9 :
				selectSumGroupByHaving();
				break;	
			case 10 :
				selectNamesLike();
				break;	
			case 11 :
				selectAllWhereAnd();
				break;
			default:
				break;
			}
	    }while(flag);
		
		
		sf.close();	
	}
	
	private static void selectAll() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Criteria q = s.createCriteria(Employee.class);
		List<Employee> list = q.list();
		for(Employee emp : list) {
			System.out.println(emp);
		}
		t.commit();
		s.flush();
		s.close();
	}
	private static void selectAllOrderBy() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Criteria q = s.createCriteria(Employee.class);
		q.addOrder(Order.desc("empName"));
		List<Employee> list = q.list();
		for(Employee emp : list) {
			System.out.println(emp);
		}
		t.commit();
		s.flush();
		s.close();
	}
	private static void selectAllWhere() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Criteria q = s.createCriteria(Employee.class);
		q.add(Restrictions.eq("dept", "cs"));
		List<Employee> list = q.list();
		for(Employee emp : list) {
			System.out.println(emp);
		}
		t.commit();
		s.flush();
		s.close();
	}
	
	private static void selectNames() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Criteria q = s.createCriteria(Employee.class);
		
		ProjectionList plist = Projections.projectionList();
		plist.add(Projections.property("empName"));
		q.setProjection(plist);
		
		List<String> list = q.list();
		for(String emp : list) {
			System.out.println(emp);
		}
		t.commit();
		s.flush();
		s.close();
	}
	
	private static void selectNameAndSalary() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Criteria q = s.createCriteria(Employee.class);
		
		ProjectionList plist = Projections.projectionList();
		plist.add(Projections.property("empName"));
		plist.add(Projections.property("basicSalary"));
		q.setProjection(plist);
		
		List<Object[]> list = q.list();
		for(Object[] emp : list) {
			System.out.println(emp[0]+" "+emp[1]);
		}
		t.commit();
		s.flush();
		s.close();
	}
	
	private static void selectDistinct() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Criteria q = s.createCriteria(Employee.class);
		
		ProjectionList plist = Projections.projectionList();
		plist.add(Projections.distinct(Projections.property("dept")));
		
		q.setProjection(plist);
		
		List<String> list = q.list();
		for(String emp : list) {
			System.out.println(emp);
		}
		t.commit();
		s.flush();
		s.close();
	}
	private static void selectSum() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Criteria q = s.createCriteria(Employee.class);
		
		ProjectionList plist = Projections.projectionList();
		plist.add(Projections.sum("basicSalary"));
		
		q.setProjection(plist);
		List<Double> list = q.list();
		for(Double emp : list) {
			System.out.println(emp);
		}
		t.commit();
		s.flush();
		s.close();
	}
	
	private static void selectSumGroupBy() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Criteria q = s.createCriteria(Employee.class);
		
		ProjectionList plist = Projections.projectionList();
		plist.add(Projections.groupProperty("dept"));
		plist.add(Projections.sum("basicSalary")); 
		
		q.setProjection(plist);
		List<Object[]> list = q.list();
		for(Object[] emp : list) {
			System.out.println(emp[0]+" "+emp[1]);
		}
		t.commit();
		s.flush();
		s.close();
	}
	
	private static void selectSumGroupByHaving() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Query q = s.createQuery("select dept,sum(basicSalary) from Employee group by dept having sum(basicSalary) > ?");
		q.setDouble(0, 200000.50);
		List<Object[]> list = q.list();
		for(Object[] emp : list) {
			System.out.println(emp[0]+" "+emp[1]);
		}
		t.commit();
		s.flush();
		s.close();
	}
	
	private static void selectNamesLike() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		
		Criteria q = s.createCriteria(Employee.class);
		q.add(Restrictions.like("empName", "a%"));
		
		ProjectionList plist = Projections.projectionList();
		plist.add(Projections.property("empName"));
		q.setProjection(plist);
		
		List<String> list = q.list();
		for(String emp : list) {
			System.out.println(emp);
		}
		t.commit();
		s.flush();
		s.close();
	}
	
	private static void selectAllWhereAnd() {
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		Criteria q = s.createCriteria(Employee.class);
		q.add(Restrictions.and(Restrictions.eq("dept", "cs"),Restrictions.ge("basicSalary", 200000.50)));
		
		
		List<Employee> list = q.list();
		for(Employee emp : list) {
			System.out.println(emp);
		}
		t.commit();
		s.flush();
		s.close();
	}
	
}
