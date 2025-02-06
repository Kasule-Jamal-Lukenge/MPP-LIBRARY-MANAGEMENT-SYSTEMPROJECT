package org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.impl;

import org.miu.mppproject.librarysystem.libraryapp.backend.core.utlis.exception.MemberException;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.Member;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.IMemberRepository;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract.IMemberService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements IMemberService {


    private final IMemberRepository memberRepository;

    public MemberServiceImpl(IMemberRepository bookRepository) {
        this.memberRepository = bookRepository;
    }


    @Override
    public void addMember(Member member) {
        try {
            memberRepository.addLibraryMember(member);
        } catch (SQLException e) {
            throw new MemberException("Unable to add Library member");
        }
    }





    @Override
    public List<Member> getAllMembers() {
        try {
            return memberRepository.getAllMembers();
        } catch (SQLException e) {
            throw new MemberException("Unable to fetch Library members");
        }
    }



    @Override
    public Optional<Member> findMemberById(String memberId) {
        try {
            return memberRepository.findByMemberId(memberId);
        } catch (SQLException e) {
            throw new MemberException("Unable to find member ");
        }

    }

    @Override
    public void editMember(Member member) {
        try {
            memberRepository.editMember(member);
        } catch (SQLException e) {
            throw new MemberException("Error occurred updating member info");
        }
    }


}
