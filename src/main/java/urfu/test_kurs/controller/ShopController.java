package urfu.test_kurs.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import urfu.test_kurs.config.AuthenticationFacade;
import urfu.test_kurs.entity.Shop;
import urfu.test_kurs.repository.ShopRepository;
import urfu.test_kurs.service.UserActionLogService;

import java.util.Optional;
@Slf4j
@Controller
public class ShopController {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserActionLogService userActionLogService;

    @GetMapping("/list-shops")
    public ModelAndView getAllBooks() {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("list-shops");
        mav.addObject("shops", shopRepository.findAll());
        return mav;
    }

    @GetMapping("/addShopForm")
    public ModelAndView addShopForm() {
        if (authenticationFacade.getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("READ_ONLY"))) {
            return new ModelAndView("redirect:/list-shops");
        } else {
            ModelAndView mav = new ModelAndView("add-shop-form");
            Shop shop = new Shop();
            mav.addObject("shop", shop);
            return mav;
        }
    }
    @PostMapping("/saveShop")
    public String saveBook(@ModelAttribute Shop shop) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        shop.setCreated(currentPrincipalName);
        shopRepository.save(shop);
        userActionLogService.logAction(currentPrincipalName, "Создание магазина");
        return "redirect:/list-shops";
    }
    @GetMapping("/showUpdateFormShop")
    public ModelAndView showUpdateForm(@RequestParam Long shopId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();
            if (authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ADMIN")) ||
                    (shop.getCreated().equals(currentPrincipalName))) {
                ModelAndView mav = new ModelAndView("add-shop-form");
                mav.addObject("shop", shop);
                userActionLogService.logAction(currentPrincipalName, "Изменение магазина");
                return mav;
            } else {
                return new ModelAndView("redirect:/list-shops");
            }
        } else {
            return new ModelAndView("redirect:/list-shops");
        }
    }
    @GetMapping("/deleteShop")
    public String deleteShop(@RequestParam Long shopId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();
            if (authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ADMIN")) ||
                    (shop.getCreated().equals(currentPrincipalName))) {
                shopRepository.deleteById(shopId);
                userActionLogService.logAction(currentPrincipalName, "Удаление магазина");
            }
        }
        return "redirect:/list-shops";
    }
}