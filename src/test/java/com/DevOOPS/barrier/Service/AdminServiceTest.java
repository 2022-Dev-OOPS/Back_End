//package com.DevOOPS.barrier.Service;
//
//import com.DevOOPS.barrier.Domain.admin;
//import com.DevOOPS.barrier.Repository.MemoryAdminRepository;
//import org.aspectj.lang.annotation.After;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//class AdminServiceTest {
//
//    AdminService adminService = new AdminService();
//    MemoryAdminRepository adminRepository = new MemoryAdminRepository();
//
//    @AfterEach
//    public void afterEach() {
//        adminRepository.clearStore();
//    }
//
//
//
//    @Test
//    void join() {
//        //given
//        admin adm = new admin();
//        adm.setAdminId("spring");
//
//        //when
//        int saveId = adminService.join(adm);
//        //then
//        admin findAdmin = adminService.findOne(saveId).get();
//
//        assertThat(adm.getAdminId()).isEqualTo(findAdmin.getAdminId());
//    }
//
//    @Test
//    public void 중복_회원_예외() {
//        //given
//        admin adm1 = new admin();
//        adm1.setAdminId("spring");
//
//        admin adm2 = new admin();
//        adm2.setAdminId("spring");
//        //when
//        adminService.join(adm1);
//        IllegalStateException e = assertThrows(IllegalStateException.class, () -> adminService.join(adm2));
////        try {
////            fail();
////        }catch (IllegalStateException e) {
////            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
////        }
//
//        //then
//    }
//
//    @Test
//    void findAdmin() {
//    }
//
//    @Test
//    void findOne() {
//    }
//}