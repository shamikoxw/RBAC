package com.example.rbacdemo;

import com.example.rbacdemo.entity.*;
import com.example.rbacdemo.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PermissionRepository permRepo;
    private final ResourceRepository resourceRepo;

    public DataInitializer(UserRepository userRepo, RoleRepository roleRepo, PermissionRepository permRepo, ResourceRepository resourceRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permRepo = permRepo;
        this.resourceRepo = resourceRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepo.count() > 0) return; // already initialized

        RoleEntity admin = new RoleEntity("ADMIN");
        RoleEntity teacher = new RoleEntity("TEACHER");
        RoleEntity student = new RoleEntity("STUDENT");
        RoleEntity ta = new RoleEntity("TA");

        // create permissions
        PermissionEntity pWildcard = permRepo.save(new PermissionEntity("*","*","*"));
        PermissionEntity pReportCreate = permRepo.save(new PermissionEntity("UI","report:*","create"));
        PermissionEntity pReportRead = permRepo.save(new PermissionEntity("UI","report:*","read"));
        PermissionEntity pReportUpdate = permRepo.save(new PermissionEntity("UI","report:*","update"));
        PermissionEntity pResourceShare = permRepo.save(new PermissionEntity("UI","resource:*","share"));
        PermissionEntity pMessageSend = permRepo.save(new PermissionEntity("UI","message:*","send"));
        PermissionEntity pResourceRead = permRepo.save(new PermissionEntity("UI","resource:*","read"));

        admin.getPermissions().addAll(Set.of(pWildcard, pReportCreate, pReportRead, pReportUpdate, pResourceShare, pMessageSend, pResourceRead));
        teacher.getPermissions().addAll(Set.of(pReportCreate, pReportRead, pReportUpdate, pResourceShare));
        ta.getPermissions().addAll(Set.of(pReportRead, pMessageSend));
        student.getPermissions().addAll(Set.of(pResourceRead, pMessageSend));

        roleRepo.saveAll(Set.of(admin, teacher, student, ta));

        // users
        UserEntity alice = new UserEntity("alice","Alice (管理员)");
        alice.addRole(admin);
        UserEntity bob = new UserEntity("bob","Bob (老师)");
        bob.addRole(teacher);
        UserEntity carol = new UserEntity("carol","Carol (助教/研究生)");
        carol.addRole(ta);
        carol.addRole(student);
        UserEntity dave = new UserEntity("dave","Dave (学生)");
        dave.addRole(student);

        userRepo.saveAll(Set.of(alice, bob, carol, dave));

        // resources
        ResourceItemEntity r1 = new ResourceItemEntity("res1","bob");
        ResourceItemEntity r2 = new ResourceItemEntity("res2","dave");
        r2.setConfidential(true);
        resourceRepo.saveAll(Set.of(r1, r2));

        System.out.println("[DataInitializer] 初始化完成");
    }
}
