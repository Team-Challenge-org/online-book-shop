package org.teamchallenge.bookshop.service.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.BookMapper;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.dto.CreateBookDto;
import org.teamchallenge.bookshop.exception.BookNotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Image;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.service.BookService;
import org.teamchallenge.bookshop.service.DropboxService;
import org.teamchallenge.bookshop.util.ImageUtil;

import java.awt.image.BufferedImage;
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
    public void addBook(CreateBookDto bookDto) {
        Book book = new Book();
        book.setAuthors(bookDto.getAuthor());
        book.setCategory(book.getCategory());
        book.setPrice(bookDto.getPrice());
        book.setFull_description(bookDto.getFull_description());
        book.setTimeAdded(bookDto.getTimeAdded());
        book.setShort_description(book.getShort_description());
        BufferedImage titleImage = ImageUtil.base64ToBufferedImage(bookDto.getTitleImage());
        String folderName = UUID.randomUUID().toString();
        dropboxService.createFolder(folderName);
        Image title = resizeAndUpload(folderName + "/title", titleImage);
        title.setSliderImage(dropboxService.uploadImage(folderName + "/title/slider.png",
                ImageUtil.resizeImageByHeight(titleImage, 560)));
        book.setTitleImage(title);
        int counter = 1;
        List<Image> imageList = new ArrayList<>();
        for (String s : bookDto.getImages()) {
            BufferedImage temp = ImageUtil.base64ToBufferedImage(s);
            imageList.add(resizeAndUpload(folderName + "/image_" + counter++, temp));
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
        if (priceMin != null) {
            predicates.add(criteriaBuilder.ge(root.get("priceMin"), priceMin));
        }
        if (priceMax != null) {
            predicates.add(criteriaBuilder.le(root.get("priceMax"), priceMax));
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

    private Image resizeAndUpload(String path, BufferedImage bufferedImage) {
        Image image = new Image();
        image.setSmallImage(dropboxService.uploadImage(path + "/small.png",
                ImageUtil.resizeImageByHeight(bufferedImage, 360)));
        image.setPageImage(dropboxService.uploadImage(path + "/page.png",
                ImageUtil.resizeImageByHeight(bufferedImage, 640)));
        image.setBigImage(dropboxService.uploadImage(path + "/big.png",
                ImageUtil.resizeImageByHeight(bufferedImage, 850)));
        return image;
    }

}
