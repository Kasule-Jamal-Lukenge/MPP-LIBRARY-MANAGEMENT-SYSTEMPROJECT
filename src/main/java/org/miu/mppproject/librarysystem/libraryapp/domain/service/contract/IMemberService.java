package org.miu.mppproject.librarysystem.libraryapp.domain.service.contract;

import org.miu.mppproject.librarysystem.libraryapp.data.entities.Member;

import java.util.List;
import java.util.Optional;

public interface IMemberService {

    void addMember(Member member);


    List<Member> getAllMembers();


    Optional<Member> findMemberById(String memberId);

    void editMember(Member member);


}
