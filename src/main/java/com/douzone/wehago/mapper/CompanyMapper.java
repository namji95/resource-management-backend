package com.douzone.wehago.mapper;

import com.douzone.wehago.domain.Company;
import com.douzone.wehago.dto.CompanyDTO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompanyMapper {
    CompanyDTO save(Company company);

    List<Company> findAll();

    Company findOne();

    void update(Company company);

    void delete(Integer copSeq);
}
