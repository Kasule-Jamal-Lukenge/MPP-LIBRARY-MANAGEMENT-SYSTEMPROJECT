package org.miu.mppproject.librarysystem.libraryapp.domain.repositories;

import org.miu.mppproject.librarysystem.libraryapp.data.entities.Member;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IMemberRepository {



    Optional<Member> findByMemberId(String memberId) throws SQLException;

    void addLibraryMember(Member member) throws SQLException;

    List<Member> getAllMembers() throws SQLException;

    Long deleteMember(String memberId) throws SQLException;

    void editMember(Member member) throws SQLException;


}
