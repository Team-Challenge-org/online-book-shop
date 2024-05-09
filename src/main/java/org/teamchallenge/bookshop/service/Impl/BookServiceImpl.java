package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.BookMapper;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.exception.BookNotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.service.BookService;

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
    public BookDto updateBook(BookDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId()).orElseThrow(BookNotFoundException::new);
        Book updatedBook = Book.builder()
                .id(bookDto.getId())
                .title(book.getTitle())
                .description(bookDto.getDescription())
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
    public Page<BookDto> getAllBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        List<BookDto> bookDtoList = bookPage.getContent().stream()
                .map(bookMapper::entityToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(bookDtoList, pageable, bookPage.getTotalElements());
    }

        @Override
        public BookDto findBooksByTitle(String title) {
            Book book = bookRepository.findByTitle(title).orElseThrow(BookNotFoundException::new);
            return bookMapper.entityToDTO(book);
        }

}
