package com.douzone.wehago.service;


import com.douzone.wehago.common.Response;
import com.douzone.wehago.domain.Reservation;

import com.douzone.wehago.dto.reservation.*;
import com.douzone.wehago.repository.ReservationRepository;
import com.douzone.wehago.security.UserDetailsImpl;
import com.github.pagehelper.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.douzone.wehago.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationService{

    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;

    protected static final String APP_TYPE_URL_ENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
    protected static final String APP_TYPE_JSON = "application/json;charset=UTF-8";


    // todo :: 겹치는거 리팩토링

    @Transactional(readOnly = true)
    public ReservationPageResponseDTO reservationList(Integer pageNo , Integer pageSize, UserDetails userDetails){
        User user = ((UserDetailsImpl) userDetails).getUser();

        String rsvId = user.getUserId();
        System.out.println(rsvId);


        List<Reservation> list = reservationRepository.reservationList(pageNo,pageSize,rsvId);
        Object total =((Page) list).getPages();
        List<ResponseReservationDTO> responseReservationDTOList = new ArrayList<>();

        for(Reservation reservation : list){
            if(reservation.getRsvState()){
                responseReservationDTOList.add(getResponseReservationDTO(reservation));
            }
        }
        return ReservationPageResponseDTO.builder()
                .list(responseReservationDTOList)
                .total(total)
                .build();
    }

    @Transactional
    public void scheduledReservation() {
        reservationRepository.updateReservation();
    }



    private ResponseReservationDTO getResponseReservationDTO (Reservation reservation){
        return ResponseReservationDTO.builder()
                .rsvSeq(reservation.getRsvSeq())
                .copSeq(reservation.getCopSeq())
                .rsvDetail(reservation.getRsvDetail())
                .rsvId(reservation.getRsvId())
                .rsvName(reservation.getRsvName())
                .rsvNum(reservation.getRsvNum())
                .rsvExplain(reservation.getRsvExplain())
                .rsvParti(reservation.getRsvParti())
                .rsvTitle(reservation.getRsvTitle())
                .rsvStart(reservation.getRsvStart())
                .rsvEnd(reservation.getRsvEnd())
                .rsvState(reservation.getRsvState())
                .build();
    }


    // 예약 등록 서비스
    @Transactional
    public Response reservationEvent(ReservationDTO reservationDTO, UserDetails userDetails) {

        User user = ((UserDetailsImpl) userDetails).getUser(); // 토큰이 유효한 토큰이라면 유저 정보를 가지고 온다.

        Map<String, Object> message = new HashMap<>();

        if (user == null) {
            String errorMessage = "토큰이 만료되었거나, 회원정보를 찾을 수 없습니다.";
            return Response.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message(errorMessage)
                    .build();
        }

        try {

            Reservation reservation = modelMapper.map(reservationDTO, Reservation.class);
            reservationRepository.registrationEvent(reservation);

        } catch (Exception e) {
            Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("예약 모달에 빈 곳이 없는지 확인해주세요.")
                    .build();
        }

        return Response.builder()
                .status(HttpStatus.OK)
                .message("예약 등록이 완료되었습니다.")
                .build();
    }
    public Map<Integer, Integer> getMonthlyReservationCounts(Integer copSeq) {
        List<MonthlyCountDTO> monthlyCounts = reservationRepository.getMonthlyReservationCounts(copSeq);
        Map<Integer, Integer> resultMap = new HashMap<>();

        for (MonthlyCountDTO dto : monthlyCounts) {
            resultMap.put(dto.getMonth(), dto.getCount());
        }

        return resultMap;
    }
    public Map<Integer, Integer> getMonthlyReservationCountsCar(ReservationChartDTO reservationChartDTO) {
        List<MonthlyCountDTO> monthlyCounts = reservationRepository.getMonthlyReservationCountsCar(reservationChartDTO);
        Map<Integer, Integer> resultMap = new HashMap<>();

        for (MonthlyCountDTO dto : monthlyCounts) {
            resultMap.put(dto.getMonth(), dto.getCount());
        }

        return resultMap;
    }
    public Map<Integer, Integer> getMonthlyReservationCountsDevice(ReservationChartDTO reservationChartDTO) {
        List<MonthlyCountDTO> monthlyCounts = reservationRepository.getMonthlyReservationCountsDevice(reservationChartDTO);
        Map<Integer, Integer> resultMap = new HashMap<>();

        for (MonthlyCountDTO dto : monthlyCounts) {
            resultMap.put(dto.getMonth(), dto.getCount());
        }

        return resultMap;
    }
    public Map<Integer, Integer> getMonthlyReservationCountsSpace(ReservationChartDTO reservationChartDTO) {
        List<MonthlyCountDTO> monthlyCounts = reservationRepository.getMonthlyReservationCountsSpace(reservationChartDTO);
        Map<Integer, Integer> resultMap = new HashMap<>();

        for (MonthlyCountDTO dto : monthlyCounts) {
            resultMap.put(dto.getMonth(), dto.getCount());
        }

        return resultMap;
    }

    // 캘린더 회사 전체 예약 정보 가져오기
    @Transactional
    public Response allAvailableReservation(UserDetails userDetails) {

        User user = ((UserDetailsImpl) userDetails).getUser(); // 토큰이 유효한 토큰이라면 유저 정보를 가지고 온다.

        Map<String, Object> message = new HashMap<>();

        if (user == null) {
            String errorMessage = "토큰이 만료되었거나, 회원정보를 찾을 수 없습니다.";
            return Response.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message(errorMessage)
                    .build();
        }

        try {
            List<AvailableReservationDTO> availableReservationDTOS =
                    reservationRepository.findAllAvailableReservation(user.getCopSeq());

            System.out.println(availableReservationDTOS.get(0).getRsvNum());

            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("조회 성공")
                    .data(availableReservationDTOS)
                    .build();

        } catch (Exception e) {

            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("조회 성공")
                    .build();
        }
    }
}
