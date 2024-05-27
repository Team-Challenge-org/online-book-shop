package org.teamchallenge.bookshop.service.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.BookMapper;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.dto.CatalogDto;
import org.teamchallenge.bookshop.exception.BookNotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.service.BookService;
import org.teamchallenge.bookshop.service.DropboxService;
import org.teamchallenge.bookshop.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final DropboxService dropboxService;
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addBook(BookDto bookDto) {
        Book book = bookMapper.dtoToEntity(bookDto);
        String folderName = "/" + UUID.randomUUID();
        dropboxService.createFolder(folderName);
        book.setTitleImage(dropboxService.uploadImage(
                folderName + "/title.png",
                ImageUtil.base64ToBufferedImage(book.getTitleImage()))
        );
        int counter = 1;
        List<String> imageList = new ArrayList<>();
        if (!book.getImages().isEmpty()) {
            for (String s : book.getImages()) {
                imageList.add(dropboxService.uploadImage(
                        folderName + "/" + counter++ + ".png",
                        ImageUtil.base64ToBufferedImage(s))
                );
            }
        }
        book.setImages(imageList);
        bookRepository.save(book);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return bookMapper.entityToDTO(book);
    }

    @Override
    public List<BookInCatalogDto> getBooksForSlider() {
        return bookRepository.findAll()
                .stream()
                .filter(Book::getIsThisNotSlider)
                .map(bookMapper::entityToBookCatalogDTO)
                .toList();
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId()).orElseThrow(BookNotFoundException::new);
        Book updatedBook = Book.builder()
                .id(bookDto.getId())
                .title(book.getTitle())
                .price(bookDto.getPrice())
                .timeAdded(book.getTimeAdded())
                .titleImage(bookDto.getTitleImage())
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
        return bookRepository.findAll()
                .stream()
                .filter(book -> book.getIsThisNotSlider() == null || !book.getIsThisNotSlider())
                .map(bookMapper::entityToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<CatalogDto> getAllCategory() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::entityToCatalogDto)
                .collect(Collectors.toList());
    }
    @Override
    public BookInCatalogDto getBookByTitle(String title) {
        Book book = bookRepository.findByTitleIgnoreCase(title).orElseThrow(BookNotFoundException::new);
        return bookMapper.entityToBookCatalogDTO(book);
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
};
