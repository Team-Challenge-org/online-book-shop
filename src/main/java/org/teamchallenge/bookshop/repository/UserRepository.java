package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Optional<Long> findIdByEmail(@Param("email") String email);
    @Query("SELECT u.favourites FROM User u WHERE u.id = :userId")
    List<Book> findFavouritesById(@Param("userId") long userId);
    @Query("SELECT u FROM User u WHERE u.email = :emailOrPhone OR u.phoneNumber = :emailOrPhone")
    Optional<User> findByEmailOrPhoneNumber(@Param("emailOrPhone") String emailOrPhone);



}
