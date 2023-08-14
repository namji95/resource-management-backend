package com.douzone.wehago.service;


import com.douzone.wehago.common.Response;
import com.douzone.wehago.domain.Reservation;
import com.douzone.wehago.dto.reservation.*;
import com.douzone.wehago.repository.ReservationRepository;
import com.douzone.wehago.security.UserDetailsImpl;
import com.github.pagehelper.Page;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.douzone.wehago.domain.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationService{

    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;
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

        System.out.println("========= start ===========");
        System.out.println(reservationDTO.getCopSeq());
        System.out.println(reservationDTO.getRsvDetail());
        System.out.println(reservationDTO.getRsvId());
        System.out.println(reservationDTO.getRsvName());
        System.out.println(reservationDTO.getRsvTitle());
        System.out.println(reservationDTO.getRsvParti());
        System.out.println(reservationDTO.getRsvExplain());
        System.out.println(reservationDTO.getRsvStart());
        System.out.println(reservationDTO.getRsvEnd());
        System.out.println("=========  end  ===========");

        Reservation reservation = modelMapper.map(reservationDTO, Reservation.class);

        reservationRepository.registrationEvent(reservation);

        return null;
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

}
