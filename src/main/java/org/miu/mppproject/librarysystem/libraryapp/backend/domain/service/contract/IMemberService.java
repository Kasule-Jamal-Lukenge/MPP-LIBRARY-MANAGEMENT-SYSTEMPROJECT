package org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract;

import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.Member;

import java.util.List;
import java.util.Optional;

public interface IMemberService {

    void addMember(Member member);


    List<Member> getAllMembers();


    Optional<Member> findMemberById(String memberId);

    void editMember(Member member);


}
