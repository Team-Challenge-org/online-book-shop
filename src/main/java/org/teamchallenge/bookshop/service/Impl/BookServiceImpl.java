package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
                .category(bookDto.getCategory())
                .price(bookDto.getPrice())
                .imageUrl(bookDto.getImageUrl())
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
            Book book = bookRepository.findByTitle(title).orElseThrow(BookNotFoundException::new);
            return bookMapper.entityToCatalogDTO(book);
        }

    @Override
    public List<BookDto> getSorted(String category, String timeAdded, String price, String author, Float priceMin, Float priceMax) {
        List<Sort.Order> orderList = new ArrayList<>();
        if (timeAdded != null) {
            if (timeAdded.equals("ASC")) {
                orderList.add(new Sort.Order(Sort.Direction.ASC, "timeAdded"));
            } else {
                orderList.add(new Sort.Order(Sort.Direction.DESC, "timeAdded"));
            }
        }
        if (price != null) {
            if (price.equals("ASC")) {
                orderList.add(new Sort.Order(Sort.Direction.ASC, "price"));
            } else {
                orderList.add(new Sort.Order(Sort.Direction.DESC, "price"));
            }
        }
        return bookRepository.findSorted(category, timeAdded, price, priceMax, priceMin)
                .stream().map(bookMapper::entityToDTO).toList();
    }

}
