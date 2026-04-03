package com.filmrental.controller;

import com.filmrental.service.BackendGateway;
import com.filmrental.service.BackendGateway.BackendGatewayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;

@Controller
public class WebPageController {

    private final BackendGateway backendGateway;

    @Value("${frontend.backend.base-url}")
    private String backendBaseUrl;

    public WebPageController(BackendGateway backendGateway) {
        this.backendGateway = backendGateway;
    }

    @GetMapping("/")
    public String home(Model model) {
        page(model, "home", "Film Rental Store");
        fetchList(model, "topFilms", () -> backendGateway.getTopRentedFilms());
        return "index";
    }

    @GetMapping("/films")
    public String films(Model model) {
        page(model, "films", "Films");
        fetchList(model, "topFilms", () -> backendGateway.getTopRentedFilms());
        return "films";
    }

    @GetMapping("/films/open")
    public String openFilm(@RequestParam Integer filmId) {
        return "redirect:/films/" + filmId;
    }

    @GetMapping("/films/category/open")
    public String openCategory(@RequestParam Integer categoryId) {
        return "redirect:/films/category/" + categoryId;
    }

    @GetMapping("/films/actor/open")
    public String openFilmsByActor(@RequestParam Integer actorId) {
        return "redirect:/films/actor/" + actorId;
    }

    @GetMapping("/films/category/{categoryId}")
    public String filmsByCategory(@PathVariable Integer categoryId, Model model) {
        page(model, "films-category", "Collection");
        model.addAttribute("categoryId", categoryId);
        fetchList(model, "films", () -> backendGateway.getFilmsByCategory(categoryId));
        return "films-category";
    }

    @GetMapping("/films/actor/{actorId}")
    public String filmsByActor(@PathVariable Integer actorId, Model model) {
        page(model, "films-actor", "Films By Actor");
        model.addAttribute("actorId", actorId);
        fetchList(model, "films", () -> backendGateway.getFilmsByActor(actorId));
        return "films-actor";
    }

    @GetMapping("/films/{filmId}")
    public String filmDetail(@PathVariable Integer filmId, Model model) {
        page(model, "film-detail", "Film Detail");
        model.addAttribute("filmId", filmId);
        fetchMap(model, "film", () -> backendGateway.getFilm(filmId));
        return "film-detail";
    }

    @GetMapping("/rentals")
    public String rentals(Model model) {
        page(model, "rentals", "Rentals");
        return "rentals";
    }

    @GetMapping("/rentals/open")
    public String openCustomerRentals(@RequestParam Integer customerId) {
        return "redirect:/rentals/customer/" + customerId;
    }

    @GetMapping("/rentals/customer/{customerId}")
    public String rentalsByCustomer(@PathVariable Integer customerId, Model model) {
        page(model, "rentals-customer", "Rental History");
        model.addAttribute("customerId", customerId);
        fetchList(model, "rentals", () -> backendGateway.getRentalsByCustomer(customerId));
        return "rentals-customer";
    }

    @GetMapping("/customers")
    public String customers(Model model) {
        page(model, "customers", "Customers");
        return "customers";
    }

    @GetMapping("/customers/open")
    public String openCustomer(@RequestParam Integer customerId) {
        return "redirect:/customers/" + customerId;
    }

    @GetMapping("/customers/{customerId}")
    public String customerDetail(@PathVariable Integer customerId, Model model) {
        page(model, "customer-detail", "Customer");
        model.addAttribute("customerId", customerId);
        fetchMap(model, "customer", () -> backendGateway.getCustomer(customerId));
        fetchList(model, "rentals", () -> backendGateway.getCustomerRentals(customerId));
        fetchList(model, "payments", () -> backendGateway.getCustomerPayments(customerId));
        return "customer-detail";
    }

    @GetMapping("/actors")
    public String actorsHub(Model model) {
        page(model, "actors", "Actors");
        return "actors";
    }

    @GetMapping("/actors/open")
    public String openActor(@RequestParam Integer actorId) {
        return "redirect:/actors/" + actorId + "/films";
    }

    @GetMapping("/actors/{actorId}/films")
    public String actorFilms(@PathVariable Integer actorId, Model model) {
        page(model, "actors-films", "Actor Spotlight");
        model.addAttribute("actorId", actorId);
        fetchList(model, "films", () -> backendGateway.getActorFilms(actorId));
        return "actors-films";
    }

    @GetMapping("/stores")
    public String storesHub(Model model) {
        page(model, "stores", "Stores");
        return "stores";
    }

    @GetMapping("/stores/open")
    public String openStore(@RequestParam Integer storeId) {
        return "redirect:/stores/" + storeId;
    }

    @GetMapping("/stores/inventory/open")
    public String openStoreInventory(@RequestParam Integer storeId) {
        return "redirect:/stores/" + storeId + "/inventory";
    }

    @GetMapping("/stores/manager/open")
    public String openStoreManager(@RequestParam Integer managerStaffId) {
        return "redirect:/stores/manager/" + managerStaffId;
    }

    @GetMapping("/stores/{storeId}")
    public String storeDetail(@PathVariable Integer storeId, Model model) {
        page(model, "store-detail", "Store");
        model.addAttribute("storeId", storeId);
        fetchMap(model, "store", () -> backendGateway.getStore(storeId));
        return "store-detail";
    }

    @GetMapping("/stores/manager/{managerStaffId}")
    public String storeByManager(@PathVariable Integer managerStaffId, Model model) {
        page(model, "store-manager", "Store By Manager");
        model.addAttribute("managerStaffId", managerStaffId);
        fetchMap(model, "store", () -> backendGateway.getStoreByManager(managerStaffId));
        return "store-manager";
    }

    @GetMapping("/stores/{storeId}/inventory")
    public String storeInventory(@PathVariable Integer storeId, Model model) {
        page(model, "store-inventory", "Store Inventory");
        model.addAttribute("storeId", storeId);
        fetchList(model, "items", () -> backendGateway.getStoreInventory(storeId));
        return "store-inventory";
    }

    @GetMapping("/inventory")
    public String inventoryHub(Model model) {
        page(model, "inventory", "Inventory");
        return "inventory";
    }

    @GetMapping("/inventory/open")
    public String openInventory(@RequestParam Integer inventoryId) {
        return "redirect:/inventory/" + inventoryId;
    }

    @GetMapping("/inventory/store/open")
    public String openInventoryStore(@RequestParam Integer storeId) {
        return "redirect:/inventory/store/" + storeId;
    }

    @GetMapping("/inventory/film/open")
    public String openInventoryFilm(@RequestParam Integer filmId) {
        return "redirect:/inventory/film/" + filmId;
    }

    @GetMapping("/inventory/{inventoryId}")
    public String inventoryDetail(@PathVariable Integer inventoryId, Model model) {
        page(model, "inventory-detail", "Inventory");
        model.addAttribute("inventoryId", inventoryId);
        fetchMap(model, "inventory", () -> backendGateway.getInventory(inventoryId));
        return "inventory-detail";
    }

    @GetMapping("/inventory/store/{storeId}")
    public String inventoryByStore(@PathVariable Integer storeId, Model model) {
        page(model, "inventory-store", "Store Inventory");
        model.addAttribute("storeId", storeId);
        fetchList(model, "inventories", () -> backendGateway.getInventoryByStore(storeId));
        return "inventory-store";
    }

    @GetMapping("/inventory/film/{filmId}")
    public String inventoryByFilm(@PathVariable Integer filmId, Model model) {
        page(model, "inventory-film", "Film Inventory");
        model.addAttribute("filmId", filmId);
        fetchList(model, "inventories", () -> backendGateway.getInventoryByFilm(filmId));
        return "inventory-film";
    }

    private void page(Model model, String pageKey, String pageTitle) {
        model.addAttribute("backendBaseUrl", backendBaseUrl);
        model.addAttribute("pageKey", pageKey);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("errorMessage", null);
    }

    private void fetchMap(Model model, String attribute, SupplierWithException<Map<String, Object>> supplier) {
        try {
            model.addAttribute(attribute, supplier.get());
        } catch (BackendGatewayException ex) {
            model.addAttribute(attribute, Collections.emptyMap());
            model.addAttribute("errorMessage", friendlyMessage(ex));
        }
    }

    private void fetchList(Model model, String attribute, SupplierWithException<?> supplier) {
        try {
            model.addAttribute(attribute, supplier.get());
        } catch (BackendGatewayException ex) {
            model.addAttribute(attribute, Collections.emptyList());
            model.addAttribute("errorMessage", friendlyMessage(ex));
        }
    }

    private String friendlyMessage(BackendGatewayException ex) {
        return "The frontend could not load data from the backend right now. Check that the backend is running on the configured base URL.";
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get();
    }
}
