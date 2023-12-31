package com.douzone.wehago.controller;

import com.douzone.wehago.common.Response;
import com.douzone.wehago.domain.Employee;
import com.douzone.wehago.dto.employee.EmployeeDTO;
import com.douzone.wehago.dto.employee.EmployeePageResponseDTO;
import com.douzone.wehago.dto.employee.EmployeeResponseDTO;
import com.douzone.wehago.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Object> saveEmployee(@RequestBody EmployeeDTO employeeDTO,
                                               @AuthenticationPrincipal UserDetails userDetails) {

        EmployeeResponseDTO employeeResponseDTO = employeeService.saveEmployee(employeeDTO, userDetails);
        Response response = new Response(HttpStatus.CREATED, "사원 등록 성공", employeeResponseDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/add")
    public ResponseEntity<Object> addEmployee(@RequestBody EmployeeDTO employeeDTO,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        EmployeeResponseDTO employeeResponseDTO = employeeService.addEmployee(employeeDTO, userDetails);
        Response response = new Response(HttpStatus.CREATED, "사원 등록 성공", employeeResponseDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "3") Integer pageSize,
                                          @AuthenticationPrincipal UserDetails userDetails){

        EmployeePageResponseDTO employeePageResponseDTO = employeeService.findAll(pageNum, pageSize, userDetails);
        System.out.println(employeePageResponseDTO.getPageNum() +""+ employeePageResponseDTO.getPageSize() +""+ employeePageResponseDTO.getTotal());
        Response response = new Response(HttpStatus.OK, "사원 전체 조회 성공", employeePageResponseDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 사원 검색
    @GetMapping("/search")
    public ResponseEntity<Object> searchEmployee(@RequestParam(value = "type") String type,
                                                 @RequestParam(value = "keyword") String keyword,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        log.info(type + keyword);
        EmployeePageResponseDTO employeePageResponseDTO = employeeService.searchEmployee(type, keyword, userDetails);
        Response response = new Response(HttpStatus.OK, "사원 검색 성공", employeePageResponseDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/{empSeq}")
    public ResponseEntity<Object> findOne(@PathVariable Integer empSeq) {
        EmployeeResponseDTO employeeResponseDTO = employeeService.findOne(empSeq);
        Response response = new Response(HttpStatus.OK, "사원 조회 성공", employeeResponseDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 사원 수정
    @PostMapping("/{empSeq}")
    public ResponseEntity<Object> updateEmployee(@RequestBody EmployeeDTO employeeDTO,
                                                 @PathVariable Integer empSeq,
                                                 @AuthenticationPrincipal UserDetails userDetails) {

        EmployeeResponseDTO employeeResponseDTO = employeeService.updateEmployee(employeeDTO, empSeq, userDetails);
        Response response = new Response(HttpStatus.OK, "회사 수정 성공",employeeResponseDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/del/{empSeq}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable Integer empSeq, @AuthenticationPrincipal UserDetails userDetails) {

        employeeService.deleteEmployee(empSeq, userDetails);
        Response response = new Response(HttpStatus.OK, "회사 삭제 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @DeleteMapping("/{empSeq}")
//    public ResponseEntity<Object> deleteEmployee(@PathVariable Integer empSeq) {
//        employeeService.deleteEmployee(empSeq);
//        String message = "삭제 성공";
//
//        return new ResponseEntity<>(message, HttpStatus.OK);
//    }


}