package org.teamchallenge.bookshop.service.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.BookMapper;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.exception.BookNotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.service.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addBook(BookDto bookDto) {
        Book book = bookMapper.dtoToEntity(bookDto);
        bookRepository.save(book);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return bookMapper.entityToDTO(book);
    }

    @Override
    public List<BookDto> getRandomByCount(Integer count) {
        return bookRepository.getRandom(count).stream().map(bookMapper::entityToDTO).toList();
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId()).orElseThrow(BookNotFoundException::new);
        Book updatedBook = Book.builder()
                .id(bookDto.getId())
                .title(book.getTitle())
                .price(bookDto.getPrice())
                .timeAdded(book.getTimeAdded())
                .build();
        bookRepository.save(updatedBook);
        return bookMapper.entityToDTO(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().
                stream()
                .map(bookMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookInCatalogDto getBookByTitle(String title) {
        Book book = bookRepository.findByTitleIgnoreCase(title).orElseThrow(BookNotFoundException::new);
        return bookMapper.entityToCatalogDTO(book);
    }

    @Override
    public List<BookDto> getSorted(String category,
                                   String timeAdded,
                                   String price,
                                   String author,
                                   Float priceMin,
                                   Float priceMax) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        List<Order> orders = new ArrayList<>();
        List<Predicate> predicates = new ArrayList<>();
        if (category != null) {
            predicates.add(criteriaBuilder.equal(root.get("category"), category));
        }
        if (author != null) {
            predicates.add(criteriaBuilder.equal(root.get("category"), category));
        }
        if (priceMin != null) {
            predicates.add(criteriaBuilder.ge(root.get("price"), priceMin));
        }
        if (priceMax != null) {
            predicates.add(criteriaBuilder.le(root.get("price"), priceMax));
        }
        query.where(predicates.toArray(new Predicate[0]));
        if (timeAdded != null) {
            if (timeAdded.equalsIgnoreCase("asc")) {
                orders.add(criteriaBuilder.asc(root.get("timeAdded")));
            } else {
                orders.add(criteriaBuilder.desc(root.get("timeAdded")));
            }
        }
        if (price != null) {
           if (price.equalsIgnoreCase("asc")) {
                orders.add(criteriaBuilder.asc(root.get("price")));
            } else {
                orders.add(criteriaBuilder.desc(root.get("price")));
            }
        }

        query.orderBy(orders);
        return entityManager.createQuery(query)
                    .getResultList()
                    .stream()
                    .map(bookMapper::entityToDTO)
                    .toList();
    }

}
