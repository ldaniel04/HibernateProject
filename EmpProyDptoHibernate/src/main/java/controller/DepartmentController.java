package controller;

import java.util.List;

import dao.DepartmentDao;
import dao.WorkerDao;
import exceptions.OurExceptions;
import jakarta.persistence.EntityManager;
import library.IO;
import main.Main;
import models.Department;
import models.Worker;
import view.DepartmentView;

public class DepartmentController {

	private final DepartmentView departmentMenuView;
	private final DepartmentDao departmentDao;
	private final WorkerDao workerDao;

	public DepartmentController() {
		departmentMenuView = new DepartmentView();
		departmentDao = new DepartmentDao();
		workerDao = new WorkerDao();
	}

	public void menu() {
		Integer option;

		option = 0;

		while (option != -1) {

			option = departmentMenuView.getOption();

			switch (option) {

			case 1:
				// Create / Add
				add(Main.em);
				break;
			case 2:
				show(Main.em);
				break;
			case 3:
				update();
				break;
			case 4:
				deleteDepartment();
				break;
			case 5:
				findById();
				break;
			case 6:
				addBossToDepartment();
			case -1:
				break;
			default:
				// return string ??

			}

		}

	}

	public void add(EntityManager em) {

		Department department;
		try {
			department = departmentMenuView.create();
			departmentDao.add(department, em);
		} catch (OurExceptions e) {
			// TODO Auto-generated catch block
			IO.println(e.getMessage());
		}

		

	}

	public void show(EntityManager em) {
		List<Department> allDepartments = departmentDao.show(em);
		departmentMenuView.show(allDepartments);
	}

	public void findById() {
		Integer id = departmentMenuView.findDepartmentById();
		Department department = departmentDao.findById(id);
		departmentMenuView.readDepartment(department);
	}

	public void update() {

		Integer id = departmentMenuView.findDepartmentById();

		Department department = departmentDao.findById(id);
		
		if(department == null) {
			departmentMenuView.error("That department does not exist! Returning to main menu");
			return;
		}

		department = departmentMenuView.updateDepartment(department);

		departmentDao.update(department);

	}

	public void addBossToDepartment() {

		Integer id = departmentMenuView.returnGenericIdToAddBoss("worker", false); //Search worker to be inserted as a boss

		Worker worker = workerDao.findById(id);
		
		if(worker == null) {
			departmentMenuView.error("That worker does not exist! Returning to main menu");
			return;
		}
		Department department = departmentDao.findById(departmentMenuView.returnGenericIdToAddBoss("department", true)); //Search department in which boss is going to be inserted
		
		if(department == null) {
			departmentMenuView.error("That department does not exist! Returning to main menu");
			return;
		}
		department = departmentMenuView.addBossToDepartment(department, worker); // Returns department with boss

		departmentDao.update(department);

	}
	
	public void deleteDepartment() {
		
		Integer id = departmentMenuView.returnGenericIdToDelete();
		
		Department department = departmentDao.findById(id);
		
		if(department == null) {
			departmentMenuView.error("That department does not exist! Returning to main menu");
			return;
		}
		
		List<Worker> workers = workerDao.show(Main.em);
		departmentDao.delete(department, workers);
		
		
		
		
		
	}

}
