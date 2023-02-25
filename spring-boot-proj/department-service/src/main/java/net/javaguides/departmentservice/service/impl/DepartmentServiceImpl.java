package net.javaguides.departmentservice.service.impl;

import org.springframework.stereotype.Service;

import net.javaguides.departmentservice.dto.DepartmentDto;
import net.javaguides.departmentservice.entity.Department;
import net.javaguides.departmentservice.mapper.DepartmentMapper;
import net.javaguides.departmentservice.repository.DepartmentRepository;
import net.javaguides.departmentservice.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService{
	
	private DepartmentRepository departmentRepository;

	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
		super();
		this.departmentRepository = departmentRepository;
	}

	@Override
	public DepartmentDto saveDepartment(DepartmentDto departmentDto) {
		//convert department dto department jpa entity
	 /*	Department department=new Department(
				departmentDto.getId(),
				departmentDto.getDepartmentName(),
				departmentDto.getDepartmentDescription(),
				departmentDto.getDepartmentCode()
				);
		Department savedDepartment=departmentRepository.save(department);
		
		DepartmentDto savedDepartmentDto=new DepartmentDto(
				savedDepartment.getId(),
				savedDepartment.getDepartmentName(),
				savedDepartment.getDepartmentDescription(),
				savedDepartment.getDepartmentCode()
				); */
		Department department=DepartmentMapper.mapToDepartment(departmentDto);
		Department savedDepartment=departmentRepository.save(department);
		DepartmentDto savedDepartmentDto=DepartmentMapper.mapToDepartmentDto(savedDepartment);
		return savedDepartmentDto;
	}

	@Override
	public DepartmentDto getDepartmentByCode(String departmentcode) {
		// TODO Auto-generated method stub
	/*Department department=departmentRepository.findByDepartmentCode(departmentcode);
		DepartmentDto departmentDto=new DepartmentDto(
		  department.getId(),
		  department.getDepartmentName(),
		  department.getDepartmentDescription(),
		  department.getDepartmentCode()
		);
		*/
		Department department=departmentRepository.findByDepartmentCode(departmentcode);
		DepartmentDto departmentDto=DepartmentMapper.mapToDepartmentDto(department);
		return departmentDto;
	}

	
}
