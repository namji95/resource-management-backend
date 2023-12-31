package com.douzone.wehago.service;

import com.douzone.wehago.common.S3Uploader;
import com.douzone.wehago.common.exception.BusinessException;
import com.douzone.wehago.common.exception.ErrorCode;
import com.douzone.wehago.domain.Car;
import com.douzone.wehago.domain.User;
import com.douzone.wehago.dto.car.CarDTO;
import com.douzone.wehago.dto.car.CarPageResponseDTO;
import com.douzone.wehago.dto.car.CarResponseDTO;
import com.douzone.wehago.dto.reservation.ReservationDTO;
import com.douzone.wehago.repository.CarRepository;
import com.douzone.wehago.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final S3Uploader s3Uploader;
    @Transactional
    public CarPageResponseDTO findcarList(ReservationDTO reservationDTO , UserDetails userDetails){

        User user = ((UserDetailsImpl) userDetails).getUser();

        if (user == null) {
            throw new BusinessException("토큰이 만료되었거나, 회원정보를 찾을 수 없습니다.", ErrorCode.JWT_INVALID_TOKEN);
        }

        reservationDTO.setCopSeq(user.getCopSeq());
        List<Car> list = carRepository.findcarList(reservationDTO);

        List<CarResponseDTO> carResponseDTOList = new ArrayList<>();

        for (Car car : list) {
            if (car.getCarState()) {
                carResponseDTOList.add(getCarResponseDTO(car));
            }
        }
        return CarPageResponseDTO.builder()
                .list(carResponseDTOList)
                .build();
    }

    @Transactional(readOnly = true)
    public CarPageResponseDTO findAllCar(UserDetails userDetails) {

        System.out.println("userDetails : " + userDetails);
        User user = ((UserDetailsImpl) userDetails).getUser();
        System.out.println("user : " + user);

        if (user == null) {
            throw new BusinessException("토큰이 만료되었거나, 회원정보를 찾을 수 없습니다.", ErrorCode.JWT_INVALID_TOKEN);
        }

        List<Car> list = carRepository.findAll(user.getCopSeq());
        List<CarResponseDTO> carResponseDTOList = new ArrayList<>(user.getCopSeq());

        for (Car car : list) {
                carResponseDTOList.add(getCarResponseDTO(car));
        }

        return CarPageResponseDTO.builder()
                .list(carResponseDTOList)
                .build();
    }

    @Transactional
    public CarResponseDTO saveCar (CarDTO carDTO, MultipartFile image, UserDetails userDetails) throws IOException {

        System.out.println("userDetails : " + userDetails);
        User user = ((UserDetailsImpl) userDetails).getUser();
        System.out.println("CopSeq" + user.getCopSeq());
        String imageUrl = s3Uploader.upload(image, "car/image");

        Car car = Car.builder()
                .carName(carDTO.getCarName())
                .carNumber(carDTO.getCarNumber())
                .carDistance(carDTO.getCarDistance())
                .carYear(carDTO.getCarYear())
                .carImage(imageUrl)
                .carExplain(carDTO.getCarExplain())
                .copSeq(user.getCopSeq())
                .rscSeq(1)
                .build();
        carRepository.save(car);

        return getCarResponseDTO(car);
    }



    @Transactional(readOnly = true)
    public CarPageResponseDTO searchCar (String columnName, String searchString, UserDetails userDetails) {

        User user = ((UserDetailsImpl) userDetails).getUser();

        if (user == null) {
            throw new BusinessException("토큰이 만료되었거나, 회원정보를 찾을 수 없습니다.", ErrorCode.JWT_INVALID_TOKEN);
        }

        List<Car> list = carRepository.searchCar(columnName, searchString);
        System.out.println("service" + columnName + searchString);
        List<CarResponseDTO> carResponseDTOList = new ArrayList<>();

        for (Car car : list) {
            carResponseDTOList.add(getCarResponseDTO(car));
        }

        return CarPageResponseDTO.builder()
                .list(carResponseDTOList)
                .build();
    }

//    @Transactional(readOnly = true)
//    public CarResponseDTO findOneCar (Integer carSeq, UserDetails userDetails) {
//        Car car = carRepository.findOne(carSeq);
//
//        return getCarResponseDTO(car);
//    }

    @Transactional
    public CarResponseDTO updateCar (CarDTO carDTO, MultipartFile image, Integer carSeq, UserDetails userDetails) throws IOException {

        User user = ((UserDetailsImpl) userDetails).getUser();
        String imageUrl = s3Uploader.upload(image, "car/image");

        if (user == null) {
            throw new BusinessException("토큰이 만료되었거나, 회원정보를 찾을 수 없습니다.", ErrorCode.JWT_INVALID_TOKEN);
        }

        if (imageUrl != null) {
            s3Uploader.deleteImage(imageUrl, "car/image");
            imageUrl = s3Uploader.upload(image, "car/image");
        }

        Car car = Car.builder()
                .carSeq(carSeq)
                .carName(carDTO.getCarName())
                .carNumber(carDTO.getCarNumber())
                .carDistance(carDTO.getCarDistance())
                .carYear(carDTO.getCarYear())
                .carImage(imageUrl)
                .carExplain(carDTO.getCarExplain())
                .copSeq(user.getCopSeq())
                .build();

        carRepository.update(car);

        return getCarResponseDTO(car);
    }

    @Transactional
    public Integer deleteCar (Integer carSeq, UserDetails userDetails) {

        User user = ((UserDetailsImpl) userDetails).getUser();

        if (user == null) {
            throw new BusinessException("토큰이 만료되었거나, 회원정보를 찾을 수 없습니다.", ErrorCode.JWT_INVALID_TOKEN);
        }

        Car car = Car.builder()
                .carSeq(carSeq)
                .carState(false)
                .copSeq(user.getCopSeq())
                .carUpdated(new Timestamp(System.currentTimeMillis()))
                .build();

        Car responseCar = carRepository.delete(car);
        return responseCar.getCarSeq();
    }

//    public void deleteCar(Integer carSeq) {
//        carRepository.delete(carSeq);
//    }

    private CarResponseDTO getCarResponseDTO (Car car) {
        return CarResponseDTO.builder()
                .carSeq(car.getCarSeq())
                .carName(car.getCarName())
                .carNumber(car.getCarNumber())
                .carDistance(car.getCarDistance())
                .carImage(car.getCarImage())
                .carExplain(car.getCarExplain())
                .carYear(car.getCarYear())
                .carState(car.getCarState())
                .copSeq(car.getCopSeq())
                .rscSeq(car.getRscSeq())
                .build();
    }
}
