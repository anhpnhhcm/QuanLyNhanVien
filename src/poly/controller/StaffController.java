package poly.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.entity.Departs;
import poly.entity.Staffs;


@Transactional
@Controller
@RequestMapping("admin/staff/")
public class StaffController {
	@Autowired
	SessionFactory factory;
	
	@RequestMapping("showstaff")
	public String index(ModelMap model) {
		model.addAttribute("staff", new Staffs());
		model.addAttribute("staffs", getStaff());
		return "admin/staff/show-staff";
	}
	
	@SuppressWarnings("unchecked")
	public List<Staffs> getStaff() {
		Session session = factory.getCurrentSession();
		String hql = "FROM Staffs";
		Query query = session.createQuery(hql);
		List<Staffs> list = query.list();
		return list;
	}
	
	@RequestMapping("addstaff")
	public String InsertStaff(ModelMap model) {
		model.addAttribute("staff", new Staffs());
		return "admin/staff/add-staff";
	}
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, editor);
    }
	
	@RequestMapping("insertstaff")
	public String insert(ModelMap model, @ModelAttribute("staff") Staffs staff) {
		Session session = factory.openSession();
		try {
			session.getTransaction().begin();
			session.save(staff);
			session.getTransaction().commit();
			model.addAttribute("message", "Thêm thành công");
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			model.addAttribute("message", "Thêm thất bại");
		} finally {
			session.close();
		}
		return "admin/staff/add-staff";
	}
	
	@ModelAttribute("listdepart")
	@SuppressWarnings("unchecked")
	public List<Departs> listdepart(ModelMap model) {
		Session session = factory.getCurrentSession();
		String hql = "FROM Departs";
		Query query = session.createQuery(hql);
		List<Departs> list = query.list();
		return list;
	}
	@RequestMapping("delete/{s}")
	public String delete(ModelMap model, @PathVariable("s") int Id) {
		Session session = factory.getCurrentSession();
		Staffs staff = (Staffs) session.get(Staffs.class, Id);
		session.delete(staff);
		model.addAttribute("message", "Xóa thành công !");
		model.addAttribute("staffs", new Staffs());
		model.addAttribute("staffs", getStaff());
		return "admin/staff/show-staff";
	}
	@RequestMapping("update/{s}")
	public String update(ModelMap model, @PathVariable("s") int Id) {
		Session session = factory.getCurrentSession();
		Staffs staff = (Staffs) session.get(Staffs.class, Id);
		model.addAttribute("staff", staff);
		model.addAttribute("staffs", getStaff());
		return "admin/staff/update-staff";
	}
	@RequestMapping("updatestaff")
	public String update(ModelMap model, @ModelAttribute("staff") Staffs staff) {
		Session session = factory.openSession();
		try {
			session.getTransaction().begin();
			session.update(staff);
			session.getTransaction().commit();
			model.addAttribute("message", "Update thành công");
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			model.addAttribute("message", "Update thất bại");
		} finally {
			session.close();
		}
		model.addAttribute("staff", staff);
		return "admin/staff/update-staff";
	}
}
