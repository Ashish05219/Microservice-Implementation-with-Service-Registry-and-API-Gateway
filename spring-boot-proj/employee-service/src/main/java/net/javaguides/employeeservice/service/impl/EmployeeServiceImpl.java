package net.javaguides.employeeservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import net.javaguides.employeeservice.dto.APIResponseDto;
import net.javaguides.employeeservice.dto.DepartmentDto;
import net.javaguides.employeeservice.dto.EmployeeDto;
import net.javaguides.employeeservice.dto.OrganizationDto;
import net.javaguides.employeeservice.entity.Employee;
import net.javaguides.employeeservice.mapper.EmployeeMapper;
import net.javaguides.employeeservice.repository.EmployeeRepository;
import net.javaguides.employeeservice.service.APIClient;
import net.javaguides.employeeservice.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger LOGGER=LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	private EmployeeRepository employeeRepository;
	// private RestTemplate restTemplate;
	private WebClient webClient;
	private APIClient apiclient;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, APIClient apiclient) {
		super();
		this.employeeRepository = employeeRepository;
		this.apiclient = apiclient;
	}

	@Override
	public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
		// TODO Auto-generated method stub
		/*
		 * Employee employee=new Employee( employeeDto.getId(),
		 * employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getEmail()
		 * ); Employee savedEmployee=employeeRepository.save(employee);
		 * 
		 * EmployeeDto savedEmployeeDto=new EmployeeDto( savedEmployee.getId(),
		 * savedEmployee.getFirstName(), savedEmployee.getLastName(),
		 * savedEmployee.getEmail() );
		 */
		Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

		Employee saveDEmployee = employeeRepository.save(employee);

		EmployeeDto savedEmployeeDto = EmployeeMapper.mapToEmployeeDto(saveDEmployee);

		return savedEmployeeDto;

	}

	//@CircuitBreaker(name="${spring.application.name}",fallbackMethod="getDefaultDepartment")
	@Retry(name="${spring.application.name}",fallbackMethod="getDefaultDepartment")
	@Override
	public APIResponseDto getEmployeeById(Long employeeId) {
		// TODO Auto-generated method stub
		/*
		 * Employee employee=employeeRepository.findById(employeeId).get(); EmployeeDto
		 * employeeDto=new EmployeeDto( employee.getId(), employee.getFirstName(),
		 * employee.getLastName(), employee.getEmail() ); return employeeDto;
		 */
		LOGGER.info("inside getEmployeeById() method");
		
		Employee employee = employeeRepository.findById(employeeId).get();
		/*
		 * ResponseEntity<DepartmentDto>responseEntity=restTemplate.getForEntity(
		 * "http://localhost:8080/api/departments/" +
		 * employee.getDepartmentCode(),DepartmentDto.class); DepartmentDto
		 * departmentDto=responseEntity.getBody();
		 */

		DepartmentDto departmentDto = webClient.get()
				.uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode()).retrieve()
				.bodyToMono(DepartmentDto.class).block();
				
		//DepartmentDto departmentDto=apiclient.getDepartment(employee.getDepartmentCode());
		
		
		OrganizationDto organizationDto = webClient.get()
                .uri("http://localhost:8083/api/organizations/" + employee.getOrganizationCode())
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();
		
		EmployeeDto employeeDto = EmployeeMapper.mapToEmployeeDto(employee);

		APIResponseDto apiResponseDto = new APIResponseDto();
		apiResponseDto.setEmployee(employeeDto);
		apiResponseDto.setDepartment(departmentDto);
		apiResponseDto.setOrganization(organizationDto);

		// return employeeDto;
		return apiResponseDto;
	}
	public APIResponseDto getDefaultDepartment(Long employeeId,Exception exception) {
		// TODO Auto-generated method stub
				/*
				 * Employee employee=employeeRepository.findById(employeeId).get(); EmployeeDto
				 * employeeDto=new EmployeeDto( employee.getId(), employee.getFirstName(),
				 * employee.getLastName(), employee.getEmail() ); return employeeDto;
				 */
		        LOGGER.info("inside getDefaultDepartment method");
				Employee employee = employeeRepository.findById(employeeId).get();
				
				DepartmentDto departmentDto=new DepartmentDto();
				departmentDto.setDepartmentName("R&D Department");
				departmentDto.setDepartmentCode("RD001");
				departmentDto.setDepartmentDescription("Research and Dev Dep");
				
				EmployeeDto employeeDto = EmployeeMapper.mapToEmployeeDto(employee);

				APIResponseDto apiResponseDto = new APIResponseDto();
				apiResponseDto.setEmployee(employeeDto);
				apiResponseDto.setDepartment(departmentDto);

				// return employeeDto;
				return apiResponseDto;
	}

}
