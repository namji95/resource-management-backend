package com.douzone.wehago.repository;

import com.douzone.wehago.domain.Car;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CarRepository {

    private final SqlSession sqlSession;
  
    public void save(Car car) {
        sqlSession.insert("com.douzone.wehago.mapper.CarMapper.save", car);
    }

    public List<Car> findAll(){
        return sqlSession.selectList("com.douzone.wehago.mapper.CarMapper.findAll");
    }

    public Car findOne(String car_number) {
        return sqlSession.selectOne("com.douzone.wehago.mapper.CarMapper.findOne", car_number);
    }

    public int update(Car car){
        return sqlSession.update("com.douzone.wehago.mapper.CarMapper.update", car);
    }

    public void delete(String car_number) {
        sqlSession.delete("com.douzone.wehago.mapper.CarMapper.delete", car_number);
    }

}
