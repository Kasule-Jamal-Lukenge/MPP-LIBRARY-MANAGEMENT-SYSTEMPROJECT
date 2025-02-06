package org.miu.mppproject.librarysystem.libraryapp.backend.data.repositories;

import org.miu.mppproject.librarysystem.libraryapp.backend.data.dao.MemberDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.Member;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.IMemberRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MemberRepositoryImpl implements IMemberRepository {


    private final MemberDao memberDao;


    public MemberRepositoryImpl(MemberDao memberDao) {
        this.memberDao = memberDao;
    }




    @Override
    public Optional<Member> findByMemberId(String memberId) throws SQLException {
        return memberDao.findByMemberId(memberId);
    }

    @Override
    public void addLibraryMember(Member member) throws SQLException {
        memberDao.addLibraryMember(member);
    }

    @Override
    public List<Member> getAllMembers() throws SQLException {
        return memberDao.getAllMembers();
    }



    @Override
    public void editMember(Member member) throws SQLException {
        memberDao.editMember(member);
    }


}
