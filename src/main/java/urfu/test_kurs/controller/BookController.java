package urfu.test_kurs.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import urfu.test_kurs.config.AuthenticationFacade;
import urfu.test_kurs.entity.Book;
import urfu.test_kurs.repository.BookRepository;
import urfu.test_kurs.service.UserActionLogService;

import java.util.Optional;

@Slf4j
@Controller
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserActionLogService userActionLogService;

    @GetMapping("/list-books")
    public ModelAndView getAllBooks() {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("list-books");
        mav.addObject("books", bookRepository.findAll());
        return mav;
    }

    @GetMapping("/addBookForm")
    public ModelAndView addBookForm() {
        if (authenticationFacade.getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("READ_ONLY"))) {
            return new ModelAndView("redirect:/list-books");
        } else {
            ModelAndView mav = new ModelAndView("add-book-form");
            Book book = new Book();
            mav.addObject("book", book);
            return mav;
        }
    }

    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute Book book) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        book.setCreated(currentPrincipalName);
        bookRepository.save(book);
        userActionLogService.logAction(currentPrincipalName, "Создание книги");
        return "redirect:/list-books";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ADMIN")) ||
                    (book.getCreated().equals(currentPrincipalName))) {
                ModelAndView mav = new ModelAndView("add-book-form");
                mav.addObject("book", book);
                userActionLogService.logAction(currentPrincipalName, "Изменение книги");
                return mav;
            } else {
                return new ModelAndView("redirect:/list-books");
            }
        } else {
            return new ModelAndView("redirect:/list-books");
        }
    }

    @GetMapping("/deleteBook")
    public String deleteBook(@RequestParam Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ADMIN")) ||
                    (book.getCreated().equals(currentPrincipalName))) {
                bookRepository.deleteById(bookId);
                userActionLogService.logAction(currentPrincipalName, "Удаление книги");
            }
        }
        return "redirect:/list-books";
    }
}