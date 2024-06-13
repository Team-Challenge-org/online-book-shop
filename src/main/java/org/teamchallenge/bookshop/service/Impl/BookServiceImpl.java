package org.teamchallenge.bookshop.service.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.BookMapper;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.CategoryDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.enums.Category;
import org.teamchallenge.bookshop.exception.*;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.service.BookService;
import org.teamchallenge.bookshop.service.DropboxService;
import org.teamchallenge.bookshop.util.ImageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


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
        try {
            dropboxService.createFolder(folderName);
        } catch (DropboxFolderCreationException e) {
            throw new DropboxFolderCreationException();
        }

        try {
            book.setTitleImage(dropboxService.uploadImage(
                    folderName + "/title.png",
                    ImageUtil.base64ToBufferedImage(bookDto.getTitleImage()))
            );
        } catch (ImageUploadException e) {
            throw new ImageUploadException();
        }

        int counter = 1;
        List<String> imageList = new ArrayList<>();
        if (bookDto.getImages() != null && !bookDto.getImages().isEmpty()) {
            for (String s : bookDto.getImages()) {
                try {
                    imageList.add(dropboxService.uploadImage(
                            folderName + "/" + counter++ + ".png",
                            ImageUtil.base64ToBufferedImage(s))
                    );
                } catch (Exception e) {
                    throw new ImageUploadException("" + counter);
                }
            }
        }
        book.setImages(imageList);
        bookRepository.save(book);
    }
    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        List<String> images = book.getImages();
        images.add(0, book.getTitleImage());
        book.setQuantity(1);
        book.setImages(images);
        return bookMapper.entityToDTO(book);
    }

    @Override
    public List<BookInCatalogDto> getBooksForSlider() {
            return bookRepository.findSliderBooks()
                    .stream()
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
    public List<CategoryDto> getAllCategory() {
        return Arrays.stream(Category.values())
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .toList();
    }

    @Override
    public List<BookInCatalogDto> getBookByTitle(String title) {
        return bookRepository.findByCombinedSimilarity(title)
                .stream()
                .map(bookMapper::entityToBookCatalogDTO)
                .toList();
    }

    public Page<BookDto> getAllBooks(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        root.fetch("images", JoinType.LEFT);
        List<Predicate> predicates = getPredicatesForBooks(criteriaBuilder, root);
        query.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Book> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Book> bookList = typedQuery.getResultList();
        List<BookDto> bookDtoList = bookList.stream()
                .map(bookMapper::entityToDTO)
                .toList();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);
        List<Predicate> newPredicates = getPredicatesForBooks(criteriaBuilder, countRoot);
        countQuery.select(criteriaBuilder.count(countRoot)).where(newPredicates.toArray(new Predicate[0]));
        long totalCount = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(bookDtoList, pageable, totalCount);
    }

    @Override
    public Page<BookDto> getSorted(Pageable pageable,
                                   Integer categoryId,
                                   String timeAdded,
                                   String price,
                                   String author,
                                   Float priceMin,
                                   Float priceMax) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        root.fetch("images", JoinType.LEFT);
        List<Predicate> predicates = getPredicatesForFilter(criteriaBuilder, categoryId, author, priceMin, priceMax, root);
        query.where(predicates.toArray(new Predicate[0]));

        List<Order> orders = new ArrayList<>();
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

        TypedQuery<Book> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Book> bookList = typedQuery.getResultList();
        List<BookDto> bookDtoList = bookList.stream()
                .map(bookMapper::entityToDTO)
                .toList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);
        List<Predicate> newPredicates = getPredicatesForFilter(criteriaBuilder, categoryId, author, priceMin, priceMax, countRoot);
        countQuery.select(criteriaBuilder.count(countRoot)).where(newPredicates.toArray(new Predicate[0]));
        long totalCount = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(bookDtoList, pageable, totalCount);
    }


    private List<Predicate> getPredicatesForBooks(CriteriaBuilder criteriaBuilder, Root<Book> root) {
        return List.of(criteriaBuilder.equal(root.get("isThisSlider"), false));
    }

    private static List<Predicate> getPredicatesForFilter(CriteriaBuilder criteriaBuilder,
                                                          Integer categoryId,
                                                          String author,
                                                          Float priceMin,
                                                          Float priceMax,
                                                          Root<Book> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (categoryId != null) {
            Category categoryEnum = Category.getFromId(categoryId);
            Predicate categoryPredicate = criteriaBuilder.equal(root.get("category"), categoryEnum);
            predicates.add(categoryPredicate);
        }
        if (author != null) {
            Predicate authorPredicate = criteriaBuilder.equal(root.get("authors"), author);
            predicates.add(authorPredicate);
        }
        if (priceMin != null) {
            Predicate priceMinPredicate = criteriaBuilder.ge(root.get("price"), priceMin);
            predicates.add(priceMinPredicate);
        }
        if (priceMax != null) {
            Predicate priceMaxPredicate = criteriaBuilder.le(root.get("price"), priceMax);
            predicates.add(priceMaxPredicate);
        }
        return predicates;
    }
};
