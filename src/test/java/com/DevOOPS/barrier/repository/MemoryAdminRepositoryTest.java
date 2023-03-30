//package com.DevOOPS.barrier.repository;
//
//import com.DevOOPS.barrier.Domain.admin;
//import com.DevOOPS.barrier.Repository.AdminRepository;
//import com.DevOOPS.barrier.Repository.MemoryAdminRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class MemoryAdminRepositoryTest {
//    MemoryAdminRepository repository = new MemoryAdminRepository();
//
//
//    @AfterEach
//    public void afterEach() {
//        repository.clearStore();
//    }
//
//
//    @Test
//    public void save() {
//        admin adm = new admin();
//        adm.setAdminId("spring");
//
//        repository.save(adm);
//
//        admin result = repository.findById(adm.getIdx()).get();
//        assertThat(adm).isEqualTo(result);
//    }
//
//    @Test
//    public void findByName() {
//        admin adm1 = new admin();
//        adm1.setAdminId("spring1");
//        repository.save(adm1);
//
//        admin adm2 = new admin();
//        adm2.setAdminId("spring2");
//        repository.save(adm2);
//
//        admin result = repository.findByName("spring1").get();
//
//        assertThat(result).isEqualTo(adm1);
//    }
//
//    @Test
//    public void findAll() {
//        admin adm1 = new admin();
//        adm1.setAdminId("spring1");
//        repository.save(adm1);
//
//
//        admin adm2 = new admin();
//        adm2.setAdminId("spring1");
//        repository.save(adm2);
//
//        List<admin> result = repository.findAll();
//
//        assertThat(result.size()).isEqualTo(2);
//
//    }
//
//}
